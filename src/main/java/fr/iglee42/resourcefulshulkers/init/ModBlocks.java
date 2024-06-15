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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ResourcefulShulkers.MODID);


    public static final RegistryObject<Block> SHULKER_INFUSER = createBlock("shulker_infuser", ShulkerInfuserBlock::new);
    public static final RegistryObject<Block> SHULKER_PEDESTAL = createBlock("shulker_pedestal", ShulkerPedestalBlock::new);
    public static final RegistryObject<Block> SHULKER_ABSORBER = createBlock("shulker_absorber", ShulkerAbsorberBlock::new);

    public static final RegistryObject<Block> PURPUR_TARGET = createBlock("purpur_target", PurpurTargetBlock::new);
    //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));

    public static final RegistryObject<Block> SHULKER_HEAD = createBlockWithoutItem("shulker_head",()-> new SkullBlock(SkullTypes.SHULKER, BlockBehaviour.Properties.copy(Blocks.CREEPER_HEAD).strength(1.0f)){
        @Override
        public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });
    public static final RegistryObject<Block> WALL_SHULKER_HEAD = createBlockWithoutItem("wall_shulker_head",()-> new WallSkullBlock(SkullTypes.SHULKER, BlockBehaviour.Properties.copy(Blocks.CREEPER_WALL_HEAD).strength(1.0f).lootFrom(SHULKER_HEAD)){
        @Override
        public VoxelShape getShape(BlockState p_58114_, BlockGetter p_58115_, BlockPos p_58116_, CollisionContext p_58117_) {
            Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.0, 4.0, 9.0, 11.0, 10.0, 15.0),
                    Direction.SOUTH, Block.box(5.0, 4.0, 1.0, 11.0, 10.0, 7.0),
                    Direction.EAST, Block.box(1.0, 4.0, 5.0, 7.0, 10.0, 11.0),
                    Direction.WEST, Block.box(9.0, 4.0, 5.0, 15.0, 10.0, 11.0)));

            return AABBS.get(p_58114_.getValue(FACING));
        }
    });

    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier, Item.Properties itemProperties)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> name.endsWith("_generating_box")? new GeneratingBoxItem(block.get(), itemProperties) : new BlockItem(block.get(), itemProperties));
        return block;
    }
    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    public static RegistryObject<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        return block;
    }
    public static Block getBoxById(ResourceLocation id){
        Optional<RegistryObject<Block>> block = BLOCKS.getEntries().stream().filter(r->r.get() instanceof GeneratingBoxBlock b && b.getId() == id).findFirst();
        return block.map(RegistryObject::get).orElse(Blocks.AIR);
    }
    public static void createBox(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        createBlock(res.id().getPath()+ "_generating_box", ()->new GeneratingBoxBlock(id), new Item.Properties());
    }
    public static Block[] getAllBox() {
        List<RegistryObject<Block>> registries = BLOCKS.getEntries().stream().filter(r->r.getId().toString().endsWith("_generating_box") && r.isPresent()).toList();
        return registries.stream().map(RegistryObject::get).toList().toArray(new Block[]{});
    }
}
