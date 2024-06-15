package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShulkerPedestalBlock extends Block implements EntityBlock {
    public ShulkerPedestalBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion().strength(1.5F,6.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerPedestalBlockEntity(pos,state);
    }


    public boolean propagatesSkylightDown(BlockState p_49100_, BlockGetter p_49101_, BlockPos p_49102_) {
        return true;
    }


    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.0625, 0.3125, 0.6875, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 1, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.6875, 0.8125, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.3125, 0.3125, 1, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.875, 0.3125, 0.8125, 1, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.875, 0.375, 0.625, 0.9375, 0.625), BooleanOp.OR);

        return shape;
    }

    public float getShadeBrightness(BlockState p_49094_, BlockGetter p_49095_, BlockPos p_49096_) {
        return 1.0F;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_316595_, BlockHitResult p_316140_) {
        if (level.isClientSide) return ItemInteractionResult.sidedSuccess(true);
        if (level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity be){
            if (be.getStack().isEmpty()){
                ItemStack copy = stack.copy();
                copy.setCount(1);
                be.setStack(copy);
                player.getMainHandItem().setCount(player.getMainHandItem().getCount()-1);
                level.sendBlockUpdated(pos,state,state,Block.UPDATE_CLIENTS);
                if (stack.getCount() != player.getMainHandItem().getCount()) level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.CONSUME;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, p_316595_, p_316140_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity be){
            if (!be.getStack().isEmpty()) {
                ItemEntity item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), be.getStack());
                item.setNoPickUpDelay();
                level.addFreshEntity(item);
                be.setStack(ItemStack.EMPTY);
                level.sendBlockUpdated(pos,state,state,Block.UPDATE_CLIENTS);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, level, pos, player, p_60508_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60516_.getBlockEntity(p_60517_) instanceof ShulkerPedestalBlockEntity be)be.dropContent();
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }
}
