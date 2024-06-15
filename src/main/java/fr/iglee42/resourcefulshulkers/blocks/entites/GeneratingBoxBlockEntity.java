package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModDataComponents;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import fr.iglee42.resourcefulshulkers.network.data.GeneratingBoxSyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.ItemSyncPayload;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GeneratingBoxBlockEntity extends SecondBlockEntity implements MenuProvider {

    public static final int MAX_DURABILITY = 256;
    public static final int SHELL_DURABILITY_ADDED = 4;
    private int openCount;
    private ShulkerBoxBlockEntity.AnimationStatus animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    private float progress;
    private float progressOld;

    private ResourceLocation id;

    private ItemStackHandler inventory = new ItemStackHandler(10) {
        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot > 0 && slot < 10 ? stack : (slot==0 && stack.is(ModItems.getShellById(id)) ?super.insertItem(slot,stack,simulate) : stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if(!level.isClientSide()) {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) level,new ItemSyncPayload(getBlockPos(),slot,getStackInSlot(slot)));
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

    private int durability;
    private int generatingTick;
    private boolean explosing;


    public GeneratingBoxBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(ModBlockEntities.GENERATING_BOX_BLOCK_ENTITY.get(), pos,state );
        this.id = id;
    }

    public GeneratingBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos,blockState,new ResourceLocation(ResourcefulShulkers.MODID,"empty"));
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState,GeneratingBoxBlockEntity entity){
        SecondBlockEntity.tick(level,blockPos,blockState,entity);
        entity.updateAnimation(level,blockPos,blockState);
        entity.applyComponents(entity.components(), DataComponentPatch.builder().set(ModDataComponents.DURABILITY.get(),entity.durability).build());
        ModsUtils.debugSign(level,blockPos,entity.generatingTick+"",entity.inventory.getStackInSlot(1).getCount() + "",entity.getDurability() + "/"+ MAX_DURABILITY);
        if (!level.isClientSide && entity.getResourceGenerated() != null && entity.getResourceGenerated().getItem() != Items.AIR) {
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level,new GeneratingBoxSyncPayload(blockPos,entity.generatingTick, entity.durability));
            if (entity.durability > 0 && !entity.isInventoryFull()){
                int slotWithSpeed = Upgrade.getFirstInventoryIndexWithUpgrade(entity.upgrades,Upgrade.SPEED);
                entity.generatingTick += (1 + (slotWithSpeed == -1 ? 0 : entity.upgrades.getStackInSlot(slotWithSpeed).getCount()));
            }
            if (entity.generatingTick / 20 == 5 && !entity.isInventoryFull()){
                entity.addItems();
                entity.generatingTick = 0;
                int slotWithDurability = Upgrade.getFirstInventoryIndexWithUpgrade(entity.upgrades,Upgrade.DURABILITY);
                if (entity.durability > 0 && new Random().nextInt(5) < (5 - (slotWithDurability == -1 ? 0 : entity.upgrades.getStackInSlot(slotWithDurability).getCount()))){
                    entity.durability--;
                }
            }
            else if (entity.generatingTick / 20 > 5 && !entity.explosing){
                AABB aabb = new AABB(Vec3.atLowerCornerOf(blockPos.offset(-2,-2,-2)),Vec3.atLowerCornerWithOffset(blockPos.offset(2,2,2),1,1,1));
                level.getNearbyPlayers(TargetingConditions.forNonCombat(),null,aabb).forEach(p->p.displayClientMessage(Component.literal("You change manually the time of generating box so it will explode in 3 seconds!").withStyle(ChatFormatting.RED),false));
                entity.explosing = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.explode(null,blockPos.getX(),blockPos.getY(),blockPos.getZ(), 12,false, Level.ExplosionInteraction.BLOCK);
                    }
                },1);

            }
        }
    }


    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity be) {
        if (!level.isClientSide && getResourceGenerated() != null && getResourceGenerated().getItem() != Items.AIR){
            if (durability <= (MAX_DURABILITY - SHELL_DURABILITY_ADDED)){
                if(!inventory.getStackInSlot(0).isEmpty()){
                    durability = durability + SHELL_DURABILITY_ADDED;
                    inventory.getStackInSlot(0).setCount(inventory.getStackInSlot(0).getCount() - 1);
                }
            }
        }

    }

    private void addItems() {
        ShulkerType res = ShulkerType.getById(id);
        if (res != null) {
            int slotWithQuantity = Upgrade.getFirstInventoryIndexWithUpgrade(upgrades,Upgrade.QUANTITY);
            int count = 1;
            if (slotWithQuantity != -1) count = switch (upgrades.getStackInSlot(slotWithQuantity).getCount()){
                case 1 -> 2;
                case 2 -> 4;
                case 3 -> 6;
                case 4 -> 8;
                default -> 1;
            };
            ItemStack stack = new ItemStack(res.getItem(),count);
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.id = ResourceLocation.tryParse(tag.getString("resourceId"));
        this.inventory.deserializeNBT(provider,tag.getCompound("inventory"));
        this.upgrades.deserializeNBT(provider,tag.getCompound("upgrades"));
        applyComponents(components(), DataComponentPatch.builder().set(ModDataComponents.DURABILITY.get(),tag.getInt("durability")).build());
        this.durability = tag.getInt("durability");
        this.generatingTick = tag.getInt("generatingTick");
        this.explosing = tag.getBoolean("isExplosing");
    }

    @Override
    protected void saveAdditional(CompoundTag tag,HolderLookup.Provider provider) {
        super.saveAdditional(tag,provider);
        tag.putString("resourceId",this.id.toString());
        tag.put("inventory", this.inventory.serializeNBT(provider));
        tag.put("upgrades", this.upgrades.serializeNBT(provider));
        applyComponents(components(), DataComponentPatch.builder().set(ModDataComponents.DURABILITY.get(),durability).build());
        tag.putInt("durability",this.durability);
        tag.putInt("generatingTick", this.generatingTick);
        tag.putBoolean("isExplosing",this.explosing);
    }

    private void updateAnimation(Level p_155680_, BlockPos p_155681_, BlockState p_155682_) {
        this.progressOld = this.progress;
        switch (this.animationStatus) {
            case CLOSED -> this.progress = 0.0F;
            case OPENING -> {
                this.progress += 0.1F;
                if (this.progress >= 1.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }
                this.moveCollidedEntities(p_155680_, p_155681_, p_155682_);
            }
            case CLOSING -> {
                this.progress -= 0.1F;
                if (this.progress <= 0.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    doNeighborUpdates(p_155680_, p_155681_, p_155682_);
                }
            }
            case OPENED -> this.progress = 1.0F;
        }

    }

    public ShulkerBoxBlockEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState p_59667_) {
        return Shulker.getProgressAabb(1.0F,Direction.UP, 0.5F * this.getProgress(1.0F));
    }

    private void moveCollidedEntities(Level p_155684_, BlockPos p_155685_, BlockState p_155686_) {
        if (p_155686_.getBlock() instanceof ShulkerBoxBlock) {
            Direction direction = p_155686_.getValue(ShulkerBoxBlock.FACING);
            AABB aabb = Shulker.getProgressDeltaAabb(1.0F, direction, this.progressOld, this.progress).move(p_155685_);
            List<Entity> list = p_155684_.getEntities(null, aabb);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(
                                MoverType.SHULKER_BOX,
                                new Vec3(
                                        (aabb.getXsize() + 0.01) * (double)direction.getStepX(),
                                        (aabb.getYsize() + 0.01) * (double)direction.getStepY(),
                                        (aabb.getZsize() + 0.01) * (double)direction.getStepZ()
                                )
                        );
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
    }

    public void startOpen(Player p_59692_) {
        if (!p_59692_.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1) {
                this.level.gameEvent(p_59692_, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound((Player)null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void stopOpen(Player p_59688_) {
        if (!p_59688_.isSpectator()) {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
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

    public int getGeneratingTick() {
        return generatingTick;
    }

    public ShulkerType getResourceGenerated(){
        return ShulkerType.getById(id);
    }

    public int getDurability() {
        return durability;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void setGeneratingTicks(int ticks) {
        generatingTick = ticks;
    }

    public void setDurability(int durability) {
        this.durability = durability;
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

    public ItemStackHandler getUpgrades() {
        return upgrades;
    }
}

