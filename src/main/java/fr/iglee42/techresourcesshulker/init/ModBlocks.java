package fr.iglee42.techresourcesshulker.init;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.blocks.EnergyInserterBlock;
import fr.iglee42.techresourcesshulker.blocks.ShulkerInfuserBlock;
import fr.iglee42.techresourcesshulker.blocks.ShulkerPedestalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TechResourcesShulker.MODID);


    public static final RegistryObject<Block> SHULKER_INFUSER = createBlockWithoutItem("shulker_infuser", ShulkerInfuserBlock::new);
    public static final RegistryObject<Block> ENERGY_INSERTER = createBlock("energy_inserter", EnergyInserterBlock::new);
    public static final RegistryObject<Block> SHULKER_PEDESTAL = createBlock("shulker_pedestal", ShulkerPedestalBlock::new);
    //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));

    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(TechResourcesShulker.GROUP)));
        return block;
    }
    public static RegistryObject<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        return block;
    }
}
