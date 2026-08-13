package fr.iglee42.resourcefulshulkers.blocks.structure;

import fr.iglee42.resourcefulshulkers.blocks.entites.structure.StructuredBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.StructureBlockEntity;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StructuredBlock extends Block implements EntityBlock {
    public StructuredBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!validatePlacement(ctx, false)) {
            if (!ctx.getLevel().isClientSide && ctx.getPlayer() != null)
                ctx.getPlayer().displayClientMessage(Component.translatable("message.resoucefulshulkers.obstructed_block").withStyle(ChatFormatting.RED), true);
            return null;
        }
        if (!validatePlacement(ctx, true)) {
            if (!ctx.getLevel().isClientSide && ctx.getPlayer() != null)
                ctx.getPlayer().displayClientMessage(Component.translatable("message.resoucefulshulkers.obstructed_entity").withStyle(ChatFormatting.RED), true);
            return null;
        }
        return defaultBlockState();
    }

    private boolean validatePlacement(BlockPlaceContext ctx, boolean entity) {
        AABB box = new AABB(ctx.getClickedPos()).inflate(1).move(0, 1, 0);
        if (entity) {
            return ctx.getLevel().getEntities((Entity) null, box, e -> true).isEmpty();
        }
        return BlockPos.betweenClosedStream(box).allMatch(pos -> ctx.getLevel().getBlockState(pos).canBeReplaced(ctx));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof StructuredBlockEntity be) {
            BlockPos midPos = pos.above();
            BlockPos topPos = midPos.above();

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                Direction counterClockwise = direction.getCounterClockWise();
                placeStructure(level, pos.relative(direction), StructureBlock.Part.BOTTOM_EDGE, direction, be);
                placeStructure(level, midPos.relative(direction), StructureBlock.Part.MIDDLE_FACE, direction, be);
                placeStructure(level, topPos.relative(direction), StructureBlock.Part.TOP_EDGE, direction, be);
                placeStructure(level, pos.relative(direction).relative(counterClockwise), StructureBlock.Part.BOTTOM_CORNER, direction, be);
                placeStructure(level, midPos.relative(direction).relative(counterClockwise), StructureBlock.Part.MIDDLE_EDGE, direction, be);
                placeStructure(level, topPos.relative(direction).relative(counterClockwise), StructureBlock.Part.TOP_CORNER, direction, be);
            }

            placeStructure(level, topPos, StructureBlock.Part.TOP_FACE, Direction.NORTH, be);
        }
    }

    private void placeStructure(Level level, BlockPos pos, StructureBlock.Part part, Direction rotation, StructuredBlockEntity main) {
        level.setBlock(pos, RSBlocks.STRUCTURE.get().defaultBlockState().setValue(StructureBlock.PART, part).setValue(StructureBlock.FACING, rotation), EndCityBlock.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof StructureBlockEntity be) {
            be.setMain(main);
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player.isSpectator()) return InteractionResult.CONSUME;
        if (!(level.getBlockEntity(pos) instanceof StructuredBlockEntity be)) return InteractionResult.PASS;
        player.openMenu(be, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean piston) {
        if (state.getBlock() != newState.getBlock()) {

            AABB box = new AABB(pos).inflate(1).move(0, 1, 0);
            BlockPos.betweenClosedStream(box).forEach(p -> {
                if (level.getBlockState(p).is(RSBlocks.STRUCTURE))
                    level.destroyBlock(p, false);
            });
        }
        if (level.getBlockEntity(pos) instanceof StructuredBlockEntity be)
            be.preRemoveSideEffects();

        super.onRemove(state, level, pos, newState, piston);
    }


    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
