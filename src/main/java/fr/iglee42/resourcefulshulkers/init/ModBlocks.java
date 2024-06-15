package fr.iglee42.resourcefulshulkers.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.*;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.SkullTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ResourcefulShulkers.MODID);


    public static final DeferredBlock<Block> SHULKER_INFUSER = createBlock("shulker_infuser", ShulkerInfuserBlock::new);
    public static final DeferredBlock<Block> SHULKER_PEDESTAL = createBlock("shulker_pedestal", ShulkerPedestalBlock::new);
    public static final DeferredBlock<Block> SHULKER_ABSORBER = createBlock("shulker_absorber", ShulkerAbsorberBlock::new);

    public static final DeferredBlock<Block> PURPUR_TARGET = createBlock("purpur_target", PurpurTargetBlock::new);
    //public static final DeferredBlock<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));

    public static final DeferredBlock<Block> SHULKER_HEAD = createBlockWithoutItem("shulker_head",()-> new SkullBlock(SkullTypes.SHULKER, BlockBehaviour.Properties.of().strength(1.0f)){
        @Override
        public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });
    public static final DeferredBlock<Block> WALL_SHULKER_HEAD = createBlockWithoutItem("wall_shulker_head",()-> new WallSkullBlock(SkullTypes.SHULKER, BlockBehaviour.Properties.of().strength(1.0f).lootFrom(SHULKER_HEAD)){
        @Override
        public VoxelShape getShape(BlockState p_58114_, BlockGetter p_58115_, BlockPos p_58116_, CollisionContext p_58117_) {
            Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.0, 4.0, 9.0, 11.0, 10.0, 15.0),
                    Direction.SOUTH, Block.box(5.0, 4.0, 1.0, 11.0, 10.0, 7.0),
                    Direction.EAST, Block.box(1.0, 4.0, 5.0, 7.0, 10.0, 11.0),
                    Direction.WEST, Block.box(9.0, 4.0, 5.0, 15.0, 10.0, 11.0)));

            return AABBS.get(p_58114_.getValue(FACING));
        }
    });

    public static DeferredBlock<Block> createBlock(String name, Supplier<? extends Block> supplier, Item.Properties itemProperties)
    {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> name.endsWith("_generating_box")? new GeneratingBoxItem(block.get(), itemProperties) : new BlockItem(block.get(), itemProperties));
        return block;
    }
    public static DeferredBlock<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }
    public static DeferredBlock<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        return block;
    }
    public static Block getBoxById(ResourceLocation id){
        Optional<DeferredHolder<Block, ? extends Block>> block = BLOCKS.getEntries().stream().filter(r->r.get() instanceof GeneratingBoxBlock b && b.getId() == id).findFirst();
        return block.isPresent() ? block.get().get() : Blocks.AIR;
    }
    public static void createBox(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        createBlock(res.id().getPath()+ "_generating_box", ()->new GeneratingBoxBlock(id), new Item.Properties());
    }
    public static Block[] getAllBox() {
        List<DeferredHolder<Block, ? extends Block>> registries = BLOCKS.getEntries().stream().filter(r->r.getId().toString().endsWith("_generating_box")).toList();
        List<Block> blocks = new ArrayList<>();
        registries.forEach(r->blocks.add(r.get()));
        return blocks.toArray(new Block[]{});
    }
}
