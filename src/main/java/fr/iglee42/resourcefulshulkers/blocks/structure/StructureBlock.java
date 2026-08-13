package fr.iglee42.resourcefulshulkers.blocks.structure;

import com.mojang.serialization.MapCodec;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.StructureBlockEntity;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureBlock extends BaseEntityBlock {

    private static final Map<Part, VoxelShape> PART_SHAPES = Util.make(new EnumMap<>(Part.class),map->{
        map.put(Part.BOTTOM_CORNER,
                Shapes.or(
                        box(0,8,0,8,16,8),
                        box(0,0,0,16,8,16),
                        box(8,8,2,16,16,6),
                        box(2,8,8,6,16,16)
                ));
        map.put(Part.BOTTOM_EDGE,
                Shapes.or(
                        box(0,0,0,16,8,16),
                        box(0,8,2,16,16,6)
                ));
        map.put(Part.MIDDLE_FACE,box(0,0,2,16,16,6));
        map.put(Part.MIDDLE_EDGE,
                Shapes.or(
                        box(0,0,0,8,16,8),
                        box(8,0,2,16,16,6),
                        box(2,0,8,6,16,16)
                ));

        map.put(Part.TOP_CORNER,
                Shapes.or(
                        box(8,8,0,16,16,8),
                        box(0,8,8,8,16,16),
                        box(8,10,8,16,14,16),
                        box(0,0,0,8,16,8),
                        box(8,0,2,16,8,6),
                        box(2,0,8,6,8,16)
                ));
        map.put(Part.TOP_EDGE,
                Shapes.or(
                        box(0,8,0,16,16,8),
                        box(0,0,2,16,8,6),
                        box(0,10,8,16,14,16)
                ));
        map.put(Part.TOP_FACE,box(0,10,0,16,14,16));
    });

    private static final Map<Direction, Map<Part, VoxelShape>> SHAPES = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.NORTH, PART_SHAPES);
        map.put(Direction.EAST, rotate(PART_SHAPES, 90));
        map.put(Direction.SOUTH, rotate(PART_SHAPES, 180));
        map.put(Direction.WEST, rotate(PART_SHAPES, 270));
    });

    private static Map<Part,VoxelShape> rotate(Map<Part,VoxelShape> map, int rot) {
        return Util.make(new EnumMap<>(Part.class), res -> map.forEach((part, shape) -> res.put(part, rotateY(shape, rot))));
    }

    private static VoxelShape rotateY(VoxelShape shape, int rotation) {
        List<VoxelShape> rotatedShapes = new ArrayList<>();
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            x1 = (x1 * 16) - 8; x2 = (x2 * 16) - 8;
            z1 = (z1 * 16) - 8; z2 = (z2 * 16) - 8;

            switch (rotation) {
                case 90 -> rotatedShapes.add(boxSafe(8 - z1, y1 * 16, 8 + x1, 8 - z2, y2 * 16, 8 + x2));
                case 180 -> rotatedShapes.add(boxSafe(8 - x1, y1 * 16, 8 - z1, 8 - x2, y2 * 16, 8 - z2));
                case 270 -> rotatedShapes.add(boxSafe(8 + z1, y1 * 16, 8 - x1, 8 + z2, y2 * 16, 8 - x2));
                default -> throw new IllegalArgumentException("Invalid rotation : " + rotation);
            }
        });
        return rotatedShapes.stream().reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(shape).optimize();
    }

    private static VoxelShape boxSafe(double pMinX, double pMinY, double pMinZ, double pMaxX, double pMaxY, double pMaxZ) {
        double x1 = Math.min(pMinX, pMaxX);
        double x2 = Math.max(pMinX, pMaxX);
        double y1 = Math.min(pMinY, pMaxY);
        double y2 = Math.max(pMinY, pMaxY);
        double z1 = Math.min(pMinZ, pMaxZ);
        double z2 = Math.max(pMinZ, pMaxZ);
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    private static final MapCodec<StructureBlock> CODEC = simpleCodec(StructureBlock::new);

    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StructureBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(PART, Part.BOTTOM_CORNER).setValue(FACING,Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean piston) {
        if (level.getBlockEntity(pos) instanceof StructureBlockEntity be){
            be.getMain().ifPresent(main->level.destroyBlock(main.getBlockPos(), true));
        }
        super.onRemove(state, level, pos, newState, piston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
       builder.add(PART).add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        Direction dir = state.getValue(FACING);
        Part part = state.getValue(PART);
        return SHAPES.get(dir).get(part);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new StructureBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player.isSpectator()) return InteractionResult.CONSUME;
        if (!(level.getBlockEntity(pos) instanceof StructureBlockEntity be)) return InteractionResult.PASS;
        be.getMain().ifPresent(main-> player.openMenu(main, main.getBlockPos()));
        return InteractionResult.CONSUME;
    }


    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof StructureBlockEntity be) {
            return be.getMain().map(main -> main.getBlockState().getBlock().asItem().getDefaultInstance()).orElse(RSBlocks.END_CITY.get().asItem().getDefaultInstance());
        }
        return RSBlocks.END_CITY.get().asItem().getDefaultInstance();
    }

    public enum Part implements StringRepresentable {
        BOTTOM_CORNER,
        BOTTOM_EDGE,
        MIDDLE_FACE,
        MIDDLE_EDGE,
        TOP_EDGE,
        TOP_FACE,
        TOP_CORNER;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
