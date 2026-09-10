package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.GeneratingBoxBlock;
import fr.iglee42.resourcefulshulkers.blocks.entites.handlers.ShellHandler;
import fr.iglee42.resourcefulshulkers.blocks.entites.handlers.UpgradeHandler;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSNBT;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkerDefinition;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import fr.iglee42.resourcefulshulkers.utils.acceleration.AccelerationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GeneratingBoxBlockEntity extends SecondBlockEntity implements MenuProvider {

    private IShulkerDefinition shulker;
    private final ShellHandler shell;
    private final ItemStackHandler outputInventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };
    private final UpgradeHandler upgrades = new UpgradeHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private int openCount;
    private ShulkerBoxBlockEntity.AnimationStatus animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    private float animationProgress;
    private float animationProgressOld;

    private int durability;
    private int itemIndex = 0;
    private boolean accelerated;
    private int progress;


    public GeneratingBoxBlockEntity(BlockPos pos, BlockState state, IShulkerDefinition shulker) {
        super(RSBlockEntities.GENERATING_BOX.get(), pos, state);
        this.shulker = shulker;
        this.shell = new ShellHandler(1, Set.of(shulker)){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }
        };
    }

    public GeneratingBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState, ShulkerDefinition.EMPTY);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, GeneratingBoxBlockEntity entity) {
        SecondBlockEntity.tick(level, blockPos, blockState, entity);
        entity.updateAnimation(level, blockPos, blockState);
        entity.tick(level, blockPos, blockState);
    }

    private void tick(Level level, BlockPos blockPos, BlockState state) {
        if (level.isClientSide) return;
        if (currentItem().isEmpty()) return;
        if (!hasSpaceFor(getGeneratedItemStack())) return;
        if (durability <= 0) return;

        this.accelerated = AccelerationUtils.isAccelerated(level, blockPos);
        if (isAccelerated()){
            setChanged();
            return;
        }

        progress += 1 + upgrades.getUpgradeCount(Upgrade.SPEED);

        if (progress >= RSServerConfig.BOX_MAX_PROGRESS.get()) {
            addItems();
            progress = 0;
            if (level.random.nextFloat() >= upgrades.getUpgradeCount(Upgrade.DURABILITY) * RSServerConfig.DURABILITY_REDUCTION.get()) {
                durability--;
            }
        }

        setChanged();
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity be) {
        if (level.isClientSide) return;
        if (currentItem().isEmpty()) return;
        if (shell.extractItem(0,1,true).isEmpty()) return;

        int addedDurability = getAddedDurability();
        if (durability <= RSServerConfig.getMaxDurability() - addedDurability) {
            durability = durability + addedDurability;
            shell.extractItem(0, 1, false);
        }

    }

    public void preRemoveSideEffects() {
        IItemHandler[] handlers = new IItemHandler[]{outputInventory, shell, upgrades};
        for (IItemHandler handler : handlers) {
            Container container = new SimpleContainer(handler.getSlots());
            for (int i = 0; i < handler.getSlots(); i++) {
                container.setItem(i, handler.getStackInSlot(i));
            }
            Containers.dropContents(level, worldPosition, container);
        }
    }


    public int getAddedDurability() {
        return calculateAddedDurability(upgrades.getUpgradeCount(Upgrade.SHELL));
    }


    public static int calculateAddedDurability(int shellUpgradeCount) {
        return Mth.ceil(RSServerConfig.BOX_SHELL_DURABILITY.get() * (1 + shellUpgradeCount * RSServerConfig.SHELL_MULTIPLIER.get()));
    }

    private void addItems() {
        if (currentItem().isEmpty()) return;
        ItemStack stack = getGeneratedItemStack();
        int slot = getFirstSlotFor(stack);
        if (slot > -1) {
            outputInventory.insertItem(slot, stack, false);
        }
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

    private ItemStack getGeneratedItemStack() {
        if (currentItem().isEmpty()) return ItemStack.EMPTY;
        int count = generatedAmount(upgrades.getUpgradeCount(Upgrade.QUANTITY));
        return currentItem().copyWithCount(count);
    }

    public static int generatedAmount(int quantityUpgradeCount){
        return Mth.ceil(RSServerConfig.BOX_BASE_AMOUNT.get() + quantityUpgradeCount * RSServerConfig.QUANTITY_MULTIPLIER.get());
    }


    // MenuProvider

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new GeneratingBoxMenu(id, playerInv, this);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    // Serialization

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        CompoundTag inventory = tag.getCompound("inventory");
        this.outputInventory.deserializeNBT(inventory.getCompound("output"));
        this.shell.deserializeNBT(inventory.getCompound("shell"));
        this.upgrades.deserializeNBT(inventory.getCompound("upgrades"));

        this.durability = tag.getInt("durability");

        if (shulker.id().equals(ShulkerDefinition.EMPTY.id())){
            ShulkerDefinition def = ShulkersManager.getDefinition(ResourceLocation.tryParse(tag.getString("shulker_id")));
            this.shulker = def != null ? def : ShulkerDefinition.EMPTY;
            this.shell.clearSupportedShells();
            this.shell.addSupportedShell(shulker);
        }

        this.itemIndex = Mth.clamp(tag.getInt("item_index"), 0, availableItems().size() - 1);

        if (tag.contains("progress", CompoundTag.TAG_INT)) this.progress = tag.getInt("progress");
        if (tag.contains("accelerated", CompoundTag.TAG_BYTE)) this.accelerated = tag.getBoolean("accelerated");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag, false);
    }

    public void save(CompoundTag tag, boolean forSync) {
        CompoundTag inventory = new CompoundTag();
        inventory.put("output", this.outputInventory.serializeNBT());
        inventory.put("shell", this.shell.serializeNBT());
        inventory.put("upgrades", this.upgrades.serializeNBT());
        tag.put("inventory", inventory);

        tag.putInt("durability", this.durability);
        tag.putInt("item_index", this.itemIndex);

        tag.putString("shulker_id", this.shulker.id().toString());

        if (forSync) {
            tag.putBoolean("accelerated", this.accelerated);
            tag.putInt("progress", this.progress);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
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
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    /*@Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(RSNBT.DURABILITY, durability);
        builder.set(RSNBT.ITEM_INDEX, itemIndex);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        this.durability = input.getOrDefault(RSNBT.DURABILITY, RSServerConfig.getMaxDurability());
        this.itemIndex = GeneratingBoxItem.validateIndex(definition(), input.getOrDefault(RSNBT.ITEM_INDEX,0));
    }*/

    // Opening/Closing Animation
    private void updateAnimation(Level level, BlockPos pos, BlockState state) {
        this.animationProgressOld = this.animationProgress;
        switch (this.animationStatus.ordinal()) {
            case 0:
                this.animationProgress = 0.0F;
                break;
            case 1:
                this.animationProgress += 0.1F;
                if (this.animationProgressOld == 0.0F) {
                    doNeighborUpdates(level, pos, state);
                }

                if (this.animationProgress >= 1.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.animationProgress = 1.0F;
                    doNeighborUpdates(level, pos, state);
                }

                this.moveCollidedEntities(level, pos, state);
                break;
            case 2:
                this.animationProgress = 1.0F;
                break;
            case 3:
                this.animationProgress -= 0.1F;
                if (this.animationProgressOld == 1.0F) {
                    doNeighborUpdates(level, pos, state);
                }

                if (this.animationProgress <= 0.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.animationProgress = 0.0F;
                    doNeighborUpdates(level, pos, state);
                }
        }
    }

    public ShulkerBoxBlockEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState state) {
        return Shulker.getProgressAabb(Direction.UP, 0.5F * this.getAnimationProgress(1.0F));
    }

    private void moveCollidedEntities(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof GeneratingBoxBlock) {
            Direction direction = Direction.UP;
            AABB aabb = Shulker.getProgressDeltaAabb(direction, this.animationProgressOld, this.animationProgress).move(pos);
            List<Entity> list = level.getEntities(null, aabb);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (AccelerationUtils.isAccelerationEntity(entity)) continue;
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(MoverType.SHULKER_BOX, new Vec3((aabb.getXsize() + 0.01D) * (double) direction.getStepX(), (aabb.getYsize() + 0.01D) * (double) direction.getStepY(), (aabb.getZsize() + 0.01D) * (double) direction.getStepZ()));
                    }
                }

            }
        }
    }

    public boolean triggerEvent(int event, int data) {
        if (event == 1) {
            this.openCount = data;
            if (data == 0) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSING;
                doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            if (data == 1) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENING;
                doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            return true;
        } else {
            return super.triggerEvent(event, data);
        }
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            triggerEvent(1, openCount);
            if (this.openCount == 1) {
                this.level.gameEvent(player, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.openCount;
            triggerEvent(1, openCount);
            if (this.openCount <= 0) {
                this.level.gameEvent(player, GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public float getAnimationProgress(float scale) {
        return Mth.lerp(scale, this.animationProgressOld, this.animationProgress);
    }

    private static void doNeighborUpdates(Level level, BlockPos pos, BlockState state) {
        state.updateNeighbourShapes(level, pos, 3);
        level.updateNeighborsAt(pos, state.getBlock());
    }

    public boolean isClosed() {
        return animationStatus == ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }

    // Getters, Setters and Utils

    public List<ItemStack> availableItems() {
        return Arrays.asList(definition().item().getIngredient().getItems());
    }

    public ItemStack currentItem() {
        if (!definition().hasItem()) return ItemStack.EMPTY;
        List<ItemStack> items = availableItems();
        this.itemIndex = Mth.clamp(itemIndex, 0, items.size() - 1);
        return items.get(itemIndex);
    }

    public IShulkerDefinition definition() {
        return shulker;
    }

    public int getProgress() {
        return progress;
    }

    public int getDurability() {
        return durability;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public boolean isAccelerated() {
        return accelerated && RSServerConfig.ACCELERATION_PROTECTION.get();
    }

    public ShellHandler getShell() {
        return shell;
    }

    public ItemStackHandler getOutputInventory() {
        return outputInventory;
    }

    public UpgradeHandler getUpgrades() {
        return upgrades;
    }

    public void setItemIndex(int index) {
        this.itemIndex = Mth.clamp(index, 0, availableItems().size()-1);
        setChanged();
    }
    public void setDurability(int durability) {
        this.durability = durability;
    }
}

