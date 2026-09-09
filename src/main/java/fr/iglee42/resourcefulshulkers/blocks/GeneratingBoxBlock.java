package fr.iglee42.resourcefulshulkers.blocks;

import com.mojang.serialization.MapCodec;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GeneratingBoxBlock extends BaseEntityBlock {

    private final IShulkerDefinition shulker;

    public GeneratingBoxBlock(IShulkerDefinition shulker) {
        super(Properties.of()
                .noOcclusion()
                .strength(2.0F)
                .dynamicShape()
                .isSuffocating(GeneratingBoxBlock::statePredicate)
                .isViewBlocking(GeneratingBoxBlock::statePredicate)
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((blockState, blockGetter, blockPos) -> true));
        this.shulker = shulker;
    }

    private static boolean statePredicate(BlockState blockState,BlockGetter blockGetter,BlockPos blockPos){
        BlockEntity blockentity = blockGetter.getBlockEntity(blockPos);
        if (blockentity instanceof GeneratingBoxBlockEntity be) {
            return be.isClosed();
        }
        return true;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (!(level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be)) return Shapes.block();
        return Shapes.create(be.getBoundingBox(state));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player.isSpectator()) return InteractionResult.CONSUME;
        if (!(level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be)) return InteractionResult.PASS;
        player.openMenu(be, pos);
        PiglinAi.angerNearbyPiglins(player, true);
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratingBoxBlockEntity(pos,state,shulker);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    public RenderShape getRenderShape(BlockState p_56255_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
        return createTickerHelper(type, RSBlockEntities.GENERATING_BOX.get(), GeneratingBoxBlockEntity::tick);
    }

    public IShulkerDefinition getDefinition() {
        return shulker;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_60519_) {
        if (level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be) be.preRemoveSideEffects();
        super.onRemove(state, level, pos, newState, p_60519_);
    }
}
