package fr.iglee42.resourcefulshulkers.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.*;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.registries.RSSkullTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, ResourcefulShulkers.MODID);


    public static final DeferredHolder<Block,Block> SHULKER_INFUSER = createGeneratingBlock("shulker_infuser",ShulkerInfuserBlock::new);
    public static final DeferredHolder<Block,Block> SHULKER_PEDESTAL = createGeneratingBlock("shulker_pedestal", ShulkerPedestalBlock::new);
    public static final DeferredHolder<Block,Block> SHULKER_ABSORBER = createGeneratingBlock("shulker_absorber", ShulkerAbsorberBlock::new);

    public static final DeferredHolder<Block,Block> PURPUR_TARGET = createGeneratingBlock("purpur_target", PurpurTargetBlock::new);
    //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));

    public static final DeferredHolder<Block,Block> SHULKER_HEAD = createBlockWithoutItem("shulker_head",()-> new SkullBlock(RSSkullTypes.SHULKER, BlockBehaviour.Properties.ofFullCopy(Blocks.CREEPER_HEAD).strength(1.0f)){
        @Override
        public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });
    public static final DeferredHolder<Block,Block> WALL_SHULKER_HEAD = createBlockWithoutItem("wall_shulker_head",()-> new WallSkullBlock(RSSkullTypes.SHULKER, BlockBehaviour.Properties.ofFullCopy(Blocks.CREEPER_WALL_HEAD).strength(1.0f).lootFrom(SHULKER_HEAD)){
        @Override
        public VoxelShape getShape(BlockState p_58114_, BlockGetter p_58115_, BlockPos p_58116_, CollisionContext p_58117_) {
            Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.0, 4.0, 9.0, 11.0, 10.0, 15.0),
                    Direction.SOUTH, Block.box(5.0, 4.0, 1.0, 11.0, 10.0, 7.0),
                    Direction.EAST, Block.box(1.0, 4.0, 5.0, 7.0, 10.0, 11.0),
                    Direction.WEST, Block.box(9.0, 4.0, 5.0, 15.0, 10.0, 11.0)));

            return AABBS.get(p_58114_.getValue(FACING));
        }
    });

    public static void createGeneratingBlock(String name, Supplier<? extends Block> supplier, Item.Properties itemProperties, ResourceLocation id)
    {
        DeferredHolder<Block,Block> block = BLOCKS.register(name, supplier);
        RSItems.ITEMS.register(name, () -> name.endsWith("_generating_box")? new GeneratingBoxItem(block.get(), itemProperties,id) : new BlockItem(block.get(), itemProperties));
    }
    public static DeferredHolder<Block,Block> createGeneratingBlock(String name, Supplier<? extends Block> supplier)
    {
        DeferredHolder<Block,Block> block = BLOCKS.register(name, supplier);
        RSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    public static DeferredHolder<Block,Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        DeferredHolder<Block,Block> block = BLOCKS.register(name, supplier);
        return block;
    }
    public static Block getBoxById(ResourceLocation id){
        Optional<DeferredHolder<Block,? extends Block>> block = BLOCKS.getEntries().stream().filter(r->r.get() instanceof GeneratingBoxBlock b && b.getId() == id).findFirst();
        return block.map(h->BuiltInRegistries.BLOCK.get(h.getId())).orElse(Blocks.AIR);
    }
    public static void createBox(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        createGeneratingBlock(res.id().getPath()+ "_generating_box", ()->new GeneratingBoxBlock(id), new Item.Properties(),res.id());
    }
    public static Block[] getAllBox() {
        List<DeferredHolder<Block,? extends Block>> registries = BLOCKS.getEntries().stream().filter(r->r.getId().toString().endsWith("_generating_box") && r.asOptional().isPresent()).toList();
        return registries.stream().map(h->BuiltInRegistries.BLOCK.get(h.getId())).toList().toArray(new Block[]{});
    }
}
