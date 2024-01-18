package fr.iglee42.techresourcesshulker.blocks;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.blocks.entites.EnergyInserterBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShulkerPedestalBlock extends BaseEntityBlock {
    public ShulkerPedestalBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).noOcclusion());
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity be){
            if (be.getStack().isEmpty()){
                ItemStack copy = player.getMainHandItem().copy();
                ItemStack baseStack = player.getMainHandItem().copy();
                copy.setCount(1);
                be.setStack(copy);
                player.getMainHandItem().setCount(player.getMainHandItem().getCount()-1);
                level.sendBlockUpdated(pos,state,state,Block.UPDATE_CLIENTS);
                if (baseStack.getCount() != player.getMainHandItem().getCount()) level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            } else {
                ItemEntity item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), be.getStack());
                item.setNoPickUpDelay();
                level.addFreshEntity(item);
                be.setStack(ItemStack.EMPTY);
                level.sendBlockUpdated(pos,state,state,Block.UPDATE_CLIENTS);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, p_60507_, p_60508_);
    }
}
