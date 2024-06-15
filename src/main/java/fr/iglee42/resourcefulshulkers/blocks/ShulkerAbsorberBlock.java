package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerAbsorberBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShulkerAbsorberBlock extends Block implements EntityBlock {



    public ShulkerAbsorberBlock() {
        super(Properties.of().strength(1.5F,6.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerAbsorberBlockEntity(pos,state);
    }
    public RenderShape getRenderShape(BlockState p_56255_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
        return type == ModBlockEntities.SHULKER_ABSORBER_BLOCK_ENTITY.get() ? (lvl,pos,blockState,be) -> ShulkerAbsorberBlockEntity.tick(lvl,pos,blockState, (ShulkerAbsorberBlockEntity) be) : null;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape shape = Shapes.empty();
       //shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 1, 1), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(-0.5625, 0.4375, 0.4375, 0, 0.5625, 0.5625), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(-0.6875, 0.5625, 0.3125, -0.3125, 0.625, 0.6875), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(0.4375, 0.4375, -0.5625, 0.5625, 0.5625, 0), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, -0.6875, 0.6875, 0.625, -0.3125), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(1, 0.4375, 0.4375, 1.5625, 0.5625, 0.5625), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(1.3125, 0.5625, 0.3125, 1.6875, 0.625, 0.6875), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(0.4375, 0.4375, 1, 0.5625, 0.5625, 1.5625), BooleanOp.OR);
       //shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 1.3125, 0.6875, 0.625, 1.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.3125, 0.4375, 0.875, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.5625, 0.4375, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.5625, 0.6875, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.3125, 0.6875, 0.875, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.3125, 0.6875, 0.625, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.875, 0.3125, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);

        return shape;
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable Item.TooltipContext p_49817_, List<Component> components, TooltipFlag p_49819_) {
        components.add(Component.literal("Warning: Don't put too many in a chunk, it can produce lags !").withStyle(ChatFormatting.YELLOW));
        super.appendHoverText(p_49816_, p_49817_, components, p_49819_);
    }
}
