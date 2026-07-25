package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import fr.iglee42.resourcefulshulkers.blocks.GeneratingBoxBlock;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModComponents;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import fr.iglee42.resourcefulshulkers.network.data.ItemStackSyncPayload;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.TIABUtils;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class GeneratingBoxBlockEntity extends SecondBlockEntity implements MenuProvider {

    public static final int MAX_DURABILITY = 256;
    public static final int SHELL_DURABILITY_ADDED = 4;
    private int openCount;
    private ShulkerBoxBlockEntity.AnimationStatus animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    private float progress;
    private float progressOld;
    private boolean isTimeInBottled;
    private int generatedIndex = 0;

    private ResourceLocation id;

    private ItemStackHandler inventory = new ItemStackHandler(10) {
        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot > 0 && slot < 10 ? stack : ((slot==0 /*&& stack.is(RSItems.getShellById(id)*/) ?super.insertItem(slot,stack,simulate) : stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if(!level.isClientSide()) {
                PacketDistributor.sendToAllPlayers(new ItemStackSyncPayload(worldPosition,slot,getStackInSlot(slot)));
            }
        }
    };
    private ItemStackHandler upgrades = new ItemStackHandler(4){
        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.getItem() instanceof UpgradeItem upg) {
                if (Upgrade.inventoryContainsUpgrade(this,upg.getUpgrade())) {
                    if (Upgrade.getFirstInventoryIndexWithUpgrade(inventory,upg.getUpgrade()) == slot){
                        if (stack.getCount() < Upgrade.MAX) {
                            return super.insertItem(slot,stack,simulate);
                        } else return stack;
                    } else return stack;
                } else return super.insertItem(slot,stack,simulate);
            }
            return stack;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Upgrade.MAX;
        }
    };
    private int remainingDurability;
    private int generatingTick;


    public GeneratingBoxBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(ModBlockEntities.GENERATING_BOX_BLOCK_ENTITY.get(), pos,state );
        this.id = id;
    }

    public GeneratingBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos,blockState,ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"empty"));
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState,GeneratingBoxBlockEntity entity){
        SecondBlockEntity.tick(level,blockPos,blockState,entity);
        entity.updateAnimation(level,blockPos,blockState);
        entity.tick(level,blockPos,blockState);
    }

    private void tick(Level level,BlockPos blockPos,BlockState blockState){
        if (!level.isClientSide && getResourceGenerated() != null && getResourceGenerated().hasItem() && getResourceGenerated().getItems().get(generatedIndex) != Items.AIR) {
            level.sendBlockUpdated(blockPos,blockState,blockState, GeneratingBoxBlock.UPDATE_CLIENTS);
            if (ModList.get().isLoaded("tiab") && ResourcefulShulkersConfig.TIAB_PROTECTION.get()){
                isTimeInBottled = TIABUtils.checkTIAB(level,blockPos);
            } else {
                isTimeInBottled = false;
            }
            if (isTimeInBottled) return;
            if (remainingDurability > 0 && !isInventoryFull()){
                int slotWithSpeed = Upgrade.getFirstInventoryIndexWithUpgrade(upgrades,Upgrade.SPEED);
                generatingTick += (1 + (slotWithSpeed == -1 ? 0 : upgrades.getStackInSlot(slotWithSpeed).getCount()));
            }
            if (generatingTick / 20 == 5 && !isInventoryFull()){
                addItems();
                generatingTick = 0;
                int slotWithDurability = Upgrade.getFirstInventoryIndexWithUpgrade(upgrades,Upgrade.DURABILITY);
                if (remainingDurability > 0 &&
                        new Random().nextInt(5) < (5 - (slotWithDurability == -1 ? 0 : upgrades.getStackInSlot(slotWithDurability).getCount()))){
                    remainingDurability--;
                }
            }
        }
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity be) {
        if (!level.isClientSide && getResourceGenerated() != null && getResourceGenerated().hasItem()){
            int addedDurability = calculateAddedDurability();
            if (remainingDurability <= (MAX_DURABILITY - addedDurability)){
                if(!inventory.getStackInSlot(0).isEmpty()){
                    remainingDurability = remainingDurability + addedDurability;
                    inventory.getStackInSlot(0).setCount(inventory.getStackInSlot(0).getCount() - 1);
                }
            }
        }

    }

    public int calculateAddedDurability(){
        if ( getResourceGenerated() == null || !getResourceGenerated().hasItem()) return SHELL_DURABILITY_ADDED;
        int slotWithUpgrade = Upgrade.getFirstInventoryIndexWithUpgrade(upgrades,Upgrade.SHELL);
        if ( slotWithUpgrade == -1 )return ResourcefulShulkersConfig.BASE_SHELL.get();
        return (int) (ResourcefulShulkersConfig.BASE_SHELL.get() * ( 1 + upgrades.getStackInSlot(slotWithUpgrade).getCount() * ResourcefulShulkersConfig.SHELL_UPGRADE_MODIFIER.get()));
    }

    private void addItems() {
        ShulkerType res = ShulkerType.getById(id);
        if (res != null) {
            int slotWithQuantity = Upgrade.getFirstInventoryIndexWithUpgrade(upgrades,Upgrade.QUANTITY);
            int count = ResourcefulShulkersConfig.BASE_GENERATION.get();
            if (slotWithQuantity != -1) count = ResourcefulShulkersConfig.BASE_GENERATION.get() + ((upgrades.getStackInSlot(slotWithQuantity).getCount()) * ResourcefulShulkersConfig.QUANTITY_UPGRADE_MODIFIER.get());
            ItemStack stack = new ItemStack(res.getItems().get(generatedIndex),count);
            int slot = getFirstSlotNotFull(stack.getItem());
            if (slot == -1) return;
            if (inventory.getStackInSlot(slot).isEmpty()) {
                inventory.setStackInSlot(slot, stack);
            } else {
                ItemStack newStack = stack.copy();
                newStack.setCount(inventory.getStackInSlot(slot).getCount() + newStack.getCount());
                inventory.setStackInSlot(slot, newStack);
            }
        }

    }

    private boolean isInventoryFull(){
        for (int i = 1; i < 10; i++){
            if (!inventory.getStackInSlot(i).isEmpty() &&!inventory.getStackInSlot(i).getItem().equals(getResourceGenerated().getItems().get(generatedIndex)))
                continue;
            int maxSlotStackSize = inventory.getStackInSlot(i).getMaxStackSize();
            if (inventory.getStackInSlot(i).getCount() < maxSlotStackSize) return false;
        } 
        return true;
    }

    public int getFirstSlotNotFull(Item item){
        for (int slot = 1; slot < inventory.getSlots(); slot++){
            if (inventory.getStackInSlot(slot).isEmpty() || (inventory.getStackInSlot(slot).is(item) && inventory.getStackInSlot(slot).getCount() < inventory.getStackInSlot(slot).getMaxStackSize())) return slot;
        }
        return -1;
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new GeneratingBoxMenu(id,playerInv,this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        this.id = ResourceLocation.tryParse(tag.getString("resourceId"));
        this.inventory.deserializeNBT(provider,tag.getCompound("inventory"));
        this.upgrades.deserializeNBT(provider,tag.getCompound("upgrades"));
        this.remainingDurability = tag.getInt("remainingDurability");
        this.generatingTick = tag.getInt("generatingTick");
        this.isTimeInBottled = tag.getBoolean("isTimeInBottled");
        this.generatedIndex = tag.getInt("generatedIndex") >= getResourceGenerated().getItems().size() ? 0 : tag.getInt("generatedIndex");
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag,provider);
        save(tag,provider);
    }

    public void save(CompoundTag tag, HolderLookup.Provider provider){
        tag.putString("resourceId",this.id.toString());
        tag.put("inventory", this.inventory.serializeNBT(provider));
        tag.put("upgrades", this.upgrades.serializeNBT(provider));
        tag.putInt("remainingDurability",this.remainingDurability);
        tag.putInt("generatingTick", this.generatingTick);
        tag.putBoolean("isTimeInBottled",this.isTimeInBottled);
        tag.putInt("generatedIndex",this.generatedIndex);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        save(tag,provider);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void updateAnimation(Level p_155680_, BlockPos p_155681_, BlockState p_155682_) {
        this.progressOld = this.progress;
        switch (this.animationStatus.ordinal()) {
            case 0:
                this.progress = 0.0F;
                break;
            case 1:
                this.progress += 0.1F;
                if (this.progressOld == 0.0F) {
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }

                if (this.progress >= 1.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }

                this.moveCollidedEntities(p_155680_, p_155681_, p_155682_);
                break;
            case 2:
                this.progress = 1.0F;
                break;
            case 3:
                this.progress -= 0.1F;
                if (this.progressOld == 1.0F) {
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }

                if (this.progress <= 0.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }
        }
    }

    public ShulkerBoxBlockEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState p_59667_) {
        return Shulker.getProgressAabb(1.0F, Direction.UP, 0.5F * this.getProgress(1.0F));
    }

    private void moveCollidedEntities(Level p_155684_, BlockPos p_155685_, BlockState p_155686_) {
        if (p_155686_.getBlock() instanceof GeneratingBoxBlock) {
            Direction direction = Direction.UP;
            AABB aabb = Shulker.getProgressDeltaAabb(1.0F, direction, this.progressOld, this.progress).move(p_155685_);
            List<Entity> list = p_155684_.getEntities((Entity)null, aabb);
            if (ModList.get().isLoaded("tiab"))
                TIABUtils.removeTIABEntities(list);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(MoverType.SHULKER_BOX, new Vec3((aabb.getXsize() + 0.01D) * (double) direction.getStepX(), (aabb.getYsize() + 0.01D) * (double) direction.getStepY(), (aabb.getZsize() + 0.01D) * (double) direction.getStepZ()));
                    }
                }

            }
        }
    }


    public boolean triggerEvent(int p_59678_, int p_59679_) {
        if (p_59678_ == 1) {
            this.openCount = p_59679_;
            if (p_59679_ == 0) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSING;
                doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            if (p_59679_ == 1) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENING;
                doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            return true;
        } else {
            return super.triggerEvent(p_59678_, p_59679_);
        }
    }

    private static void doNeighborUpdates(Level p_155688_, BlockPos p_155689_, BlockState p_155690_) {
        p_155690_.updateNeighbourShapes(p_155688_, p_155689_, 3);
        p_155688_.updateNeighborsAt(p_155689_, p_155690_.getBlock());
    }


    public void startOpen(Player p_59692_) {
        if (!this.remove && !p_59692_.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            triggerEvent(1,openCount);
            if (this.openCount == 1) {
                this.level.gameEvent(p_59692_, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound((Player)null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void stopOpen(Player p_59688_) {
        if (!p_59688_.isSpectator()) {
            --this.openCount;
            triggerEvent(1,openCount);
            if (this.openCount <= 0) {
                this.level.gameEvent(p_59688_, GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound((Player)null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }
    public float getProgress(float p_59658_) {
        return Mth.lerp(p_59658_, this.progressOld, this.progress);
    }

    public PushReaction getPistonPushReaction(BlockState p_56265_) {
        return PushReaction.DESTROY;
    }

    public VoxelShape getShape(BlockState p_56257_, BlockGetter p_56258_, BlockPos p_56259_, CollisionContext p_56260_) {
        BlockEntity blockentity = p_56258_.getBlockEntity(p_56259_);
        return blockentity instanceof GeneratingBoxBlockEntity ? Shapes.create(((GeneratingBoxBlockEntity)blockentity).getBoundingBox(p_56257_)) : Shapes.block();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(ModComponents.DURABILITY,remainingDurability);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        this.remainingDurability = input.get(ModComponents.DURABILITY);

    }


    public int getGeneratingTick() {
        return generatingTick;
    }

    public ShulkerType getResourceGenerated(){
        return ShulkerType.getById(id);
    }

    public int getRemainingDurability() {
        return remainingDurability;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void setGeneratingTicks(int ticks) {
        generatingTick = ticks;
    }

    public void setDurability(int durability) {
        this.remainingDurability = durability;
    }

    public void dropContent() {
        SimpleContainer container = new SimpleContainer(inventory.getSlots() + upgrades.getSlots());
        for (int i = 0; i < inventory.getSlots();i++){
            container.setItem(i,inventory.getStackInSlot(i));
        }
        for (int i = 0; i < upgrades.getSlots();i++){
            container.setItem((inventory.getSlots() -1) + i,upgrades.getStackInSlot(i));
        }
        Containers.dropContents(level,worldPosition,container);
    }

    public boolean isClosed(){
        return animationStatus == ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }

    public boolean isTimeInABottled() {
        return isTimeInBottled;
    }

    public ItemStackHandler getUpgrades() {
        return upgrades;
    }

    public int getGeneratedIndex() {
        return generatedIndex;
    }

    public void setGeneratedIndex(int index) {
        this.generatedIndex = index;
    }
}

