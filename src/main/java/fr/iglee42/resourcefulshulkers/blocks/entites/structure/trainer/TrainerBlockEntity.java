package fr.iglee42.resourcefulshulkers.blocks.entites.structure.trainer;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.resourcefulshulkers.api.shulkers.IRegisteredShulker;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.entites.handlers.UpgradeHandler;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.StructuredBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.GeneratingBoxHandler;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.menu.EndCityMenu;
import fr.iglee42.resourcefulshulkers.menu.TrainerMenu;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkerDefinition;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import fr.iglee42.resourcefulshulkers.utils.acceleration.AccelerationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrainerBlockEntity extends StructuredBlockEntity {

    private int tier;
    private final ItemStackHandler outputInventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };
    private final ShulkerHandler shulkers = new ShulkerHandler(4){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (getStackInSlot(slot).isEmpty())
                progressPerSlot.remove(slot);
            setChanged();
        }
    };
    private final UpgradeHandler upgrades = new UpgradeHandler(2,Set.of(Upgrade.SPEED, Upgrade.QUANTITY)) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private final Map<Integer, Integer> progressPerSlot = new HashMap<>();

    private boolean accelerated;

    public TrainerBlockEntity(BlockPos pos, BlockState state) {
        super(RSBlockEntities.TRAINER.get(), pos, state);
    }


    public static void staticTick(Level level, BlockPos pos, BlockState state, TrainerBlockEntity blockEntity) {
        SecondBlockEntity.tick(level, pos, state, blockEntity);
        if (level == null) return;
        if (level.isClientSide) return;
        blockEntity.tick();
    }

    private void tick(){
        this.accelerated = AccelerationUtils.isAccelerated(level, getBlockPos());
        if (isAccelerated()){
            setChanged();
            return;
        }
        for (int slot = 0; slot < shulkers.getSlots(); slot++){
            if (shulkers.getStackInSlot(slot).isEmpty()) continue;
            IShulkerDefinition definition = shulkers.getShulkerDefinitionForSlot(slot);
            if (definition.equals(ShulkerDefinition.EMPTY)) continue;
            if (!definition.hasItem()) continue;
            if (!hasSpaceFor(generatedStackForSlot(slot))) continue;

            int progress = progressPerSlot.getOrDefault(slot, 0);
            progress += 1 + upgrades.getUpgradeCount(Upgrade.SPEED);

            if (progress >= RSServerConfig.TRAINER_MAX_PROGRESS.get()) {
                if (addItems(slot))
                    progress = 0;
            }
            progressPerSlot.put(slot, progress);
            setChanged();
        }
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity secondBlockEntity) {}

    public void preRemoveSideEffects() {
        IItemHandler[] handlers = new IItemHandler[]{outputInventory, shulkers, upgrades};
        for (IItemHandler handler : handlers) {
            Container container = new SimpleContainer(handler.getSlots());
            for (int i = 0; i < handler.getSlots(); i++) {
                container.setItem(i, handler.getStackInSlot(i));
            }
            Containers.dropContents(level, worldPosition, container);
        }
    }

    private boolean addItems(int shulkerSlot) {
        ItemStack generatedStack = generatedStackForSlot(shulkerSlot).copy();
        if (generatedStack.isEmpty()) return false;
        int slot = getFirstSlotFor(generatedStack);
        if (slot > -1) {
            outputInventory.insertItem(slot, generatedStack, false);
            return true;
        }
        return false;
    }

    private boolean hasSpaceFor(ItemStack stack) {
        return getFirstSlotFor(stack) > -1;
    }

    private int getFirstSlotFor(ItemStack stack) {
        for (int slot = 0; slot < outputInventory.getSlots(); slot++){
            if (outputInventory.insertItem(slot, stack.copy(), true).isEmpty())
                return slot;
        }
        return -1;
    }

    public ItemStack generatedStackForSlot(int slot){
        IRegisteredShulker shulker = shulkers.getShulkerDefinitionForSlot(slot).registration();
        ItemStack stack = !shulker.definition().hasItem() || shulker.shell() == null || shulker.shell().get() == null ? ItemStack.EMPTY : shulker.shell().get().getDefaultInstance();
        if (stack.isEmpty()) return ItemStack.EMPTY;
        int count = Mth.ceil(RSServerConfig.TRAINER_BASE_AMOUNT.get() + upgrades.getUpgradeCount(Upgrade.QUANTITY) * RSServerConfig.QUANTITY_MULTIPLIER.get());
        return stack.copyWithCount(count);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag inventory = tag.getCompound("inventory");
        this.outputInventory.deserializeNBT(inventory.getCompound("output"));
        this.shulkers.deserializeNBT(inventory.getCompound("shulkers"));
        this.upgrades.deserializeNBT(inventory.getCompound("upgrades"));

        if (tag.contains("accelerated", CompoundTag.TAG_BYTE))
            this.accelerated = tag.getBoolean("accelerated");
        if (tag.contains("progress",CompoundTag.TAG_LIST)){
            progressPerSlot.clear();
            ListTag progressList = tag.getList("progress", 10);
            for (int i = 0; i < progressList.size(); i++) {
                CompoundTag progressTag = progressList.getCompound(i);
                int slot = progressTag.getInt("slot");
                int progress = progressTag.getInt("progress");
                progressPerSlot.put(slot, progress);
            }
        }
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag, false);
    }

    private void save(CompoundTag tag, boolean forSync){
        CompoundTag inventory = new CompoundTag();
        inventory.put("output", this.outputInventory.serializeNBT());
        inventory.put("shulkers", this.shulkers.serializeNBT());
        inventory.put("upgrades", this.upgrades.serializeNBT());
        tag.put("inventory", inventory);

        if (forSync) {
            tag.putBoolean("accelerated", this.accelerated);
            ListTag progressList = new ListTag();
            for (Map.Entry<Integer, Integer> entry : progressPerSlot.entrySet()) {
                CompoundTag progressTag = new CompoundTag();
                progressTag.putInt("slot", entry.getKey());
                progressTag.putInt("progress", entry.getValue());
                progressList.add(progressTag);
            }
            tag.put("progress", progressList);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        save(tag, true);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level == null || level.isClientSide) return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public ItemStackHandler getOutputInventory() {
        return outputInventory;
    }

    public ShulkerHandler getShulkers() {
        return shulkers;
    }

    public UpgradeHandler getUpgrades() {
        return upgrades;
    }

    public Map<Integer, Integer> getProgressPerSlot() {
        return progressPerSlot;
    }

    public boolean isAccelerated() {
        return accelerated && RSServerConfig.ACCELERATION_PROTECTION.get();
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TrainerMenu(id, inventory, this);
    }

}
