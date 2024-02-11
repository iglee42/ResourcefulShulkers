package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.network.ModMessages;
import fr.iglee42.resourcefulshulkers.network.packets.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShulkerPedestalBlockEntity extends BlockEntity {

    private ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(getStackInSlot(slot),slot, worldPosition));
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private LazyOptional<ItemStackHandler> optionalInventory = LazyOptional.empty();


    public ShulkerPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHULKER_PEDESTAL_BLOCK_ENTITY.get(), pos,state);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? optionalInventory.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalInventory.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        optionalInventory = LazyOptional.of(()->inventory);
    }



    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory",inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
    }

    public ItemStack getStack() {
        return inventory.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        this.inventory.setStackInSlot(0,stack);
        if (!level.isClientSide()) level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),Block.UPDATE_CLIENTS);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("inventory",inventory.serializeNBT());
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dropContent() {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots();i++){
            container.setItem(i,inventory.getStackInSlot(i));
        }
        Containers.dropContents(level,worldPosition,container);
    }
}
