package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.blocks.GeneratingBoxBlock;
import fr.iglee42.techresourcesshulker.blocks.ShulkerInfuserBlock;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.techresourcesshulker.entity.BaseEssenceShulker;
import fr.iglee42.techresourcesshulker.item.ShellItem;
import fr.iglee42.techresourcesshulker.item.ShulkerInfuserItem;
import fr.iglee42.techresourcesshulker.item.ShulkerItem;
import fr.iglee42.techresourcesshulker.item.UpgradeItem;
import fr.iglee42.techresourcesshulker.menu.GeneratingBoxMenu;
import fr.iglee42.techresourcesshulker.utils.Resource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModContent {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,TechResourcesShulker.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,TechResourcesShulker.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,TechResourcesShulker.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS,TechResourcesShulker.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,TechResourcesShulker.MODID);


    public static final RegistryObject<Block> SHULKER_INFUSER = createBlockWithoutItem("shulker_infuser", ShulkerInfuserBlock::new);
    public static final RegistryObject<Block> ENERGY_INPUTER = createBlock("energy_inputer", () ->new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()){
        public boolean propagatesSkylightDown(BlockState p_49100_, BlockGetter p_49101_, BlockPos p_49102_) {
            return true;
        }

        @Override
        public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0.375, 1, 0.375, 0.625, 1.5625, 0.625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);

            return shape;

        }

        public float getShadeBrightness(BlockState p_49094_, BlockGetter p_49095_, BlockPos p_49096_) {
            return 1.0F;
        }
    });
        //public static final RegistryObject<Block> GENERATING_BOX = createBlock("generating_box", () -> new GeneratingBoxBlock(id));
    public static final RegistryObject<BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new,getAllBox()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerInfuserBlockEntity>> SHULKER_INFUSER_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_infuser", ()->BlockEntityType.Builder.of(ShulkerInfuserBlockEntity::new,ModContent.SHULKER_INFUSER.get()).build(null));

    public static final RegistryObject<EntityType<BaseEssenceShulker>> OVERWORLD_SHULKER = ENTITIES.register("overworld_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIME), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"overworld_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> SKY_SHULKER = ENTITIES.register("sky_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIGHT_BLUE), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"sky_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> NETHER_SHULKER = ENTITIES.register("nether_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.RED), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"nether_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> END_SHULKER = ENTITIES.register("end_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.YELLOW), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"end_shulker").toString()));


    public static final RegistryObject<Item> SHULKER_INFUSER_ITEM = ITEMS.register("shulker_infuser", ShulkerInfuserItem::new);
    public static final RegistryObject<Item> SHULKER_KILLER = ITEMS.register("shulker_killer", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)){
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            List<BaseEssenceShulker> target = level.getEntitiesOfClass(BaseEssenceShulker.class, player.getBoundingBox().inflate(8), (entity) -> true);
            for (BaseEssenceShulker s : target){
                s.remove(Entity.RemovalReason.KILLED);
            }
            if (!player.isCreative())player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        @Override
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            p_41423_.add(new TextComponent("Kill all custom shulker in radius of 8 blocks").withStyle(ChatFormatting.YELLOW));
            p_41423_.add(new TextComponent("Creative Only").withStyle(ChatFormatting.RED));
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        }
    });
    public static final RegistryObject<Item> SHULKER_ITEM = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),EntityType.SHULKER));
    public static final RegistryObject<Item> OVERWORLD_SHULKER_ITEM = ITEMS.register("overworld_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> SKY_SHULKER_ITEM = ITEMS.register("sky_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> NETHER_SHULKER_ITEM = ITEMS.register("nether_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> END_SHULKER_ITEM = ITEMS.register("end_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> OVERWORLD_ESSENCE = ITEMS.register("overworld_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> SKY_ESSENCE = ITEMS.register("sky_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> NETHER_ESSENCE = ITEMS.register("nether_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));

    public static final RegistryObject<Item> UPGRADE_BASE = ITEMS.register("upgrade_base", UpgradeItem::new);

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
        ENTITIES.register(bus);
    }

    public static Item getShellById(int resourceId) {
        Optional<RegistryObject<Item>> item = ITEMS.getEntries().stream().filter(r->r.get() instanceof ShellItem s && s.getId() == resourceId).findFirst();
        return item.isPresent() ? item.get().get() : Items.SHULKER_SHELL;
    }
}
