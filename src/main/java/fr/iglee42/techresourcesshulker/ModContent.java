package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.blocks.EnergyInserterBlock;
import fr.iglee42.techresourcesshulker.blocks.GeneratingBoxBlock;
import fr.iglee42.techresourcesshulker.blocks.ShulkerInfuserBlock;
import fr.iglee42.techresourcesshulker.blocks.ShulkerPedestalBlock;
import fr.iglee42.techresourcesshulker.blocks.entites.EnergyInserterBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.techresourcesshulker.entity.BaseEssenceShulker;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.entity.ResourceShulker;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.item.ShellItem;
import fr.iglee42.techresourcesshulker.item.ShulkerInfuserItem;
import fr.iglee42.techresourcesshulker.item.ShulkerItem;
import fr.iglee42.techresourcesshulker.item.UpgradeItem;
import fr.iglee42.techresourcesshulker.menu.GeneratingBoxMenu;
import fr.iglee42.techresourcesshulker.utils.Resource;
import fr.iglee42.techresourcesshulker.utils.Upgrade;
import net.minecraft.ChatFormatting;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModContent {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,TechResourcesShulker.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS,TechResourcesShulker.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,TechResourcesShulker.MODID);



    public static final RegistryObject<BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new,getAllBox()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerInfuserBlockEntity>> SHULKER_INFUSER_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_infuser", ()->BlockEntityType.Builder.of(ShulkerInfuserBlockEntity::new,ModBlocks.SHULKER_INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnergyInserterBlockEntity>> ENERGY_INSERTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("energy_inserter", ()->BlockEntityType.Builder.of(EnergyInserterBlockEntity::new,ModBlocks.ENERGY_INSERTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerPedestalBlockEntity>> SHULKER_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_pedestal", ()->BlockEntityType.Builder.of(ShulkerPedestalBlockEntity::new,ModBlocks.SHULKER_PEDESTAL.get()).build(null));


    public static final RegistryObject<EntityType<BaseEssenceShulker>> OVERWORLD_SHULKER = ENTITIES.register("overworld_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIME), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"overworld_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> SKY_SHULKER = ENTITIES.register("sky_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIGHT_BLUE), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"sky_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> NETHER_SHULKER = ENTITIES.register("nether_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.RED), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"nether_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> END_SHULKER = ENTITIES.register("end_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.YELLOW), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"end_shulker").toString()));





    public static Block[] getAllBox() {
        List<RegistryObject<Block>> registries = ModBlocks.BLOCKS.getEntries().stream().filter(r->r.getId().toString().endsWith("_generating_box")).toList();
        List<Block> blocks = new ArrayList<>();
        registries.forEach(r->blocks.add(r.get()));
        return blocks.toArray(new Block[]{});
    }

    public static final RegistryObject<MenuType<GeneratingBoxMenu>> GENERATING_BOX_MENU = registerMenuType(GeneratingBoxMenu::new,"generating_box_menu");

    public static void createShell(int id){
        Resource res = Resource.getById(id);
        ModItems.ITEMS.register(res.name()+"_shell", () -> new ShellItem(id));

    }

    public static void createBox(int id){
        Resource res = Resource.getById(id);
        ModBlocks.createBlock(res.name()+ "_generating_box", ()->new GeneratingBoxBlock(id));
    }

    public static void createShulker(int id){
        Resource res = Resource.getById(id);
        ENTITIES.register(res.name()+"_shulker", ()->EntityType.Builder.<ResourceShulker>of((type, level) -> new ResourceShulker(type,level,id), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,res.name()+"_shulker").toString()));
    }

    public static Block getBoxById(int id){
        Optional<RegistryObject<Block>> block = ModBlocks.BLOCKS.getEntries().stream().filter(r->r.get() instanceof GeneratingBoxBlock b && b.getId() == id).findFirst();
        return block.isPresent() ? block.get().get() : Blocks.AIR;
    }


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }




    public static void register(IEventBus bus){

        BLOCK_ENTITIES.register(bus);
        MENUS.register(bus);
        ENTITIES.register(bus);
    }

    public static Item getShellById(int resourceId) {
        Optional<RegistryObject<Item>> item = ModItems.ITEMS.getEntries().stream().filter(r->r.get() instanceof ShellItem s && s.getId() == resourceId).findFirst();
        return item.isPresent() ? item.get().get() : Items.SHULKER_SHELL;
    }
}
