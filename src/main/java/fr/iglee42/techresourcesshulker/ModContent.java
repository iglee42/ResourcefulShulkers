package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.blocks.GeneratingBoxBlock;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.item.ShellItem;
import fr.iglee42.techresourcesshulker.item.ShulkerItem;
import fr.iglee42.techresourcesshulker.menu.GeneratingBoxMenu;
import fr.iglee42.techresourcesshulker.utils.Resource;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModContent {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,TechResourcesShulker.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,TechResourcesShulker.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,TechResourcesShulker.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS,TechResourcesShulker.MODID);



    public static final RegistryObject<Item> SHULKER_ITEM = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> OVERWORLD_SHULKER = ITEMS.register("overworld_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> SKY_SHULKER = ITEMS.register("sky_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> NETHER_SHULKER = ITEMS.register("nether_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> END_SHULKER = ITEMS.register("end_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> OVERWORLD_ESSENCE = ITEMS.register("overworld_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> SKY_ESSENCE = ITEMS.register("sky_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> NETHER_ESSENCE = ITEMS.register("nether_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));

        //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));
    public static final RegistryObject<BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new,getAllBox()).build(null));

    public static Block[] getAllBox() {
        List<RegistryObject<Block>> registries = BLOCKS.getEntries().stream().filter(r->r.getId().toString().endsWith("_generating_box")).toList();
        List<Block> blocks = new ArrayList<>();
        registries.forEach(r->blocks.add(r.get()));
        return blocks.toArray(new Block[]{});
    }

    public static final RegistryObject<MenuType<GeneratingBoxMenu>> GENERATING_BOX_MENU = registerMenuType(GeneratingBoxMenu::new,"generating_box_menu");

    public static void createShell(int id){
        Resource res = Resource.getById(id);
        ITEMS.register(res.name().toLowerCase()+"_shell", () -> new ShellItem(id));

    }

    public static void createBox(int id){
        Resource res = Resource.getById(id);
        createBlock(res.name().toLowerCase() + "_generating_box", ()->new GeneratingBoxBlock(id));
    }

    public static Block getBoxById(int id){
        Optional<RegistryObject<Block>> block = BLOCKS.getEntries().stream().filter(r->r.get() instanceof GeneratingBoxBlock b && b.getId() == id).findFirst();
        return block.isPresent() ? block.get().get() : Blocks.AIR;
    }


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }


    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(TechResourcesShulker.GROUP)));
        return block;
    }
    public static RegistryObject<Block> createBlockWithoutItem(String name, Supplier<? extends Block> supplier)
    {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        return block;
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENUS.register(bus);
    }

    public static Item getShellById(int resourceId) {
        Optional<RegistryObject<Item>> item = ITEMS.getEntries().stream().filter(r->r.get() instanceof ShellItem s && s.getId() == resourceId).findFirst();
        return item.isPresent() ? item.get().get() : Items.SHULKER_SHELL;
    }
}
