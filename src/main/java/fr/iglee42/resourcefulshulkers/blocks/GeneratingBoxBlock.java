package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModComponents;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GeneratingBoxBlock extends Block implements EntityBlock {


    private final ResourceLocation id;

    public GeneratingBoxBlock(ResourceLocation id) {
        super(Properties.of().noOcclusion().strength(2.0F).dynamicShape().isSuffocating(GeneratingBoxBlock::statePredicate).isViewBlocking(GeneratingBoxBlock::statePredicate).pushReaction(PushReaction.DESTROY).isRedstoneConductor((blockState, blockGetter, blockPos) -> true));
        this.id = id;
    }

    private static boolean statePredicate(BlockState blockState,BlockGetter blockGetter,BlockPos blockPos){
        BlockEntity blockentity = blockGetter.getBlockEntity(blockPos);
        if (blockentity instanceof GeneratingBoxBlockEntity be) {
            return be.isClosed();
        } else {
            return true;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack stack) {
        super.setPlacedBy(level, pos, p_49849_, p_49850_, stack);
        if (level.isClientSide) return;
        if (stack == null) return;
        if (level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be) {
            be.setDurability(GeneratingBoxItem.getDurability(stack));
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        if (level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be) stack.set(ModComponents.DURABILITY.get(),be.getRemainingDurability());
        return stack;
    }



    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

    public VoxelShape getShape(BlockState p_56257_, BlockGetter p_56258_, BlockPos p_56259_, CollisionContext p_56260_) {
        BlockEntity blockentity = p_56258_.getBlockEntity(p_56259_);
        return blockentity instanceof GeneratingBoxBlockEntity ? Shapes.create(((GeneratingBoxBlockEntity)blockentity).getBoundingBox(p_56257_)) : Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState p_56227_, Level p_56228_, BlockPos p_56229_, Player p_56230_, BlockHitResult p_56232_) {
        if (p_56228_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (p_56230_.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity blockentity = p_56228_.getBlockEntity(p_56229_);
            if (blockentity instanceof GeneratingBoxBlockEntity) {
                GeneratingBoxBlockEntity be = (GeneratingBoxBlockEntity)blockentity;
                ((ServerPlayer)p_56230_).openMenu(be,w->w.writeBlockPos(p_56229_));
                PiglinAi.angerNearbyPiglins(p_56230_, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratingBoxBlockEntity(pos,state,id);
    }
    public RenderShape getRenderShape(BlockState p_56255_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
        return type.equals(ModBlockEntities.GENERATING_BOX_BLOCK_ENTITY.get()) ? ((level, blockPos, blockState, t) -> GeneratingBoxBlockEntity.tick(level,blockPos,blockState, (GeneratingBoxBlockEntity) t)) : null;
    }


    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60516_.getBlockEntity(p_60517_) instanceof GeneratingBoxBlockEntity be) be.dropContent();
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }
}
