package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.network.data.ItemSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class ShulkerPedestalBlockEntity extends BlockEntity {

    private ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            if(!level.isClientSide()) {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) level,new ItemSyncPayload(getBlockPos(),slot,getStackInSlot(slot)));
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };


    public ShulkerPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHULKER_PEDESTAL_BLOCK_ENTITY.get(), pos,state);
    }




    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag,provider);
        tag.put("inventory",inventory.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        inventory.deserializeNBT(provider,tag.getCompound("inventory"));
    }

    public ItemStack getStack() {
        return inventory.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        this.inventory.setStackInSlot(0,stack);
        if (!level.isClientSide()) level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),Block.UPDATE_CLIENTS);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag,provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        this.loadAdditional(tag,provider);
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
