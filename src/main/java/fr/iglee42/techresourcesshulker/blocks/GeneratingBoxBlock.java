package fr.iglee42.techresourcesshulker.blocks;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class GeneratingBoxBlock extends BaseEntityBlock {

    private final int id;

    public GeneratingBoxBlock(int id) {
        super(Properties.copy(Blocks.SHULKER_BOX));
        this.id = id;
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
    public InteractionResult use(BlockState p_56227_, Level p_56228_, BlockPos p_56229_, Player p_56230_, InteractionHand p_56231_, BlockHitResult p_56232_) {
        if (p_56228_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (p_56230_.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity blockentity = p_56228_.getBlockEntity(p_56229_);
            if (blockentity instanceof GeneratingBoxBlockEntity) {
                GeneratingBoxBlockEntity be = (GeneratingBoxBlockEntity)blockentity;
                if (canOpen(p_56227_, p_56228_, p_56229_, be)) {
                    NetworkHooks.openGui((ServerPlayer) p_56230_,be,buf->buf.writeBlockPos(p_56229_));
                    PiglinAi.angerNearbyPiglins(p_56230_, true);
                }

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }


    private static boolean canOpen(BlockState p_154547_, Level p_154548_, BlockPos p_154549_, GeneratingBoxBlockEntity p_154550_) {
        if (p_154550_.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
            return true;
        } else {
            AABB aabb = Shulker.getProgressDeltaAabb(Direction.UP, 0.0F, 0.5F).move(p_154549_).deflate(1.0E-6D);
            return p_154548_.noCollision(aabb);
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
        return createTickerHelper(type,ModContent.GENERATING_BOX_BLOCK_ENTITY.get(),GeneratingBoxBlockEntity::tick);
    }

    public int getId() {
        return id;
    }
}
