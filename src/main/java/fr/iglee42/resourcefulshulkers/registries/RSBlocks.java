package fr.iglee42.resourcefulshulkers.registries;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.*;
import fr.iglee42.resourcefulshulkers.blocks.structure.EndCityBlock;
import fr.iglee42.resourcefulshulkers.blocks.structure.StructureBlock;
import fr.iglee42.resourcefulshulkers.blocks.structure.TrainerBlock;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RSBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, RSIds.MODID);


    public static final RegistryObject<Block> SHULKER_INFUSER = createBlock("shulker_infuser",ShulkerInfuserBlock::new);
    public static final RegistryObject<Block> SHULKER_PEDESTAL = createBlock("shulker_pedestal", ShulkerPedestalBlock::new);
    public static final RegistryObject<Block> SHULKER_ABSORBER = createBlock("shulker_absorber", ShulkerAbsorberBlock::new);

    public static final RegistryObject<Block> PURPUR_TARGET = createBlock("purpur_target", PurpurTargetBlock::new);
    //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));

    public static final RegistryObject<Block> SHULKER_HEAD = createBlockWithoutItem("shulker_head",()-> new SkullBlock(RSSkullTypes.SHULKER, BlockBehaviour.Properties.copy(Blocks.CREEPER_HEAD).strength(1.0f)){
        @Override
        public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });
    public static final RegistryObject<Block> WALL_SHULKER_HEAD = createBlockWithoutItem("wall_shulker_head",()-> new WallSkullBlock(RSSkullTypes.SHULKER, BlockBehaviour.Properties.copy(Blocks.CREEPER_WALL_HEAD).strength(1.0f).lootFrom(SHULKER_HEAD)){
        @Override
        public VoxelShape getShape(BlockState p_58114_, BlockGetter p_58115_, BlockPos p_58116_, CollisionContext p_58117_) {
            Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.0, 4.0, 9.0, 11.0, 10.0, 15.0),
                    Direction.SOUTH, Block.box(5.0, 4.0, 1.0, 11.0, 10.0, 7.0),
                    Direction.EAST, Block.box(1.0, 4.0, 5.0, 7.0, 10.0, 11.0),
                    Direction.WEST, Block.box(9.0, 4.0, 5.0, 15.0, 10.0, 11.0)));

            return AABBS.get(p_58114_.getValue(FACING));
        }
    });

    public static final RegistryObject<Block> END_CITY = createBlock("end_city", ()->new EndCityBlock(BlockBehaviour.Properties.of().strength(3f,10f).noOcclusion(),1));
    public static final RegistryObject<Block> END_CITY_TIER_2 = createBlock("end_city_tier_2", ()->new EndCityBlock(BlockBehaviour.Properties.of().strength(3f,10f).noOcclusion(),2));
    public static final RegistryObject<Block> TRAINER = createBlock("shulker_trainer", ()->new TrainerBlock(BlockBehaviour.Properties.of().strength(3f,10f).noOcclusion()));
    public static final RegistryObject<Block> STRUCTURE = createBlockWithoutItem("structure", ()->new StructureBlock(BlockBehaviour.Properties.of().noLootTable().strength(3f,10f).noOcclusion()));

    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        RSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    public static RegistryObject<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        return BLOCKS.register(name, supplier);
    }

    // Shulker Blocks
    public static RegistryObject<Block> createGeneratingBox(IShulkerDefinition shulker){
        RegistryObject<Block> box = BLOCKS.register(shulker.id().getPath()+"_generating_box", ()->new GeneratingBoxBlock(shulker));
        RSItems.ITEMS.register(shulker.id().getPath()+"_generating_box", () -> new GeneratingBoxItem(box.get(), new Item.Properties(),shulker));
        return box;
    }

    public static Block[] getAllGeneratingBox() {
        List<Block> blocks = new ArrayList<>();
        ShulkersManager.forEachShulker(shulker->blocks.add(shulker.generatingBox().get()));
        return blocks.toArray(new Block[0]);
    }
}
