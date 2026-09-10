package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.*;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.StructureBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.trainer.TrainerBlockEntity;
import fr.iglee42.resourcefulshulkers.menu.EndCityMenu;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import fr.iglee42.resourcefulshulkers.menu.TrainerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RSBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RSIds.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, RSIds.MODID);

    public static final RegistryObject<BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new, RSBlocks.getAllGeneratingBox()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerInfuserBlockEntity>> SHULKER_INFUSER = BLOCK_ENTITIES.register("shulker_infuser", ()->BlockEntityType.Builder.of(ShulkerInfuserBlockEntity::new, RSBlocks.SHULKER_INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerPedestalBlockEntity>> SHULKER_PEDESTAL = BLOCK_ENTITIES.register("shulker_pedestal", ()->BlockEntityType.Builder.of(ShulkerPedestalBlockEntity::new, RSBlocks.SHULKER_PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShulkerAbsorberBlockEntity>> SHULKER_ABSORBER = BLOCK_ENTITIES.register("shulker_absorber", ()->BlockEntityType.Builder.of(ShulkerAbsorberBlockEntity::new, RSBlocks.SHULKER_ABSORBER.get()).build(null));
    public static final RegistryObject<BlockEntityType<PurpurTargetBlockEntity>> PURPUR_TARGET = BLOCK_ENTITIES.register("purpur_target", ()->BlockEntityType.Builder.of(PurpurTargetBlockEntity::new, RSBlocks.PURPUR_TARGET.get()).build(null));

    public static final RegistryObject<BlockEntityType<EndCityBlockEntity>> END_CITY = BLOCK_ENTITIES.register("end_city", ()->BlockEntityType.Builder.of(EndCityBlockEntity::new,RSBlocks.END_CITY.get(), RSBlocks.END_CITY_TIER_2.get()).build(null));
    public static final RegistryObject<BlockEntityType<TrainerBlockEntity>> TRAINER = BLOCK_ENTITIES.register("shulker_trainer", ()->BlockEntityType.Builder.of(TrainerBlockEntity::new,RSBlocks.TRAINER.get()).build(null));
    public static final RegistryObject<BlockEntityType<StructureBlockEntity>> STRUCTURE = BLOCK_ENTITIES.register("structure", ()->BlockEntityType.Builder.of(StructureBlockEntity::new, RSBlocks.STRUCTURE.get()).build(null));

    public static final RegistryObject<MenuType<GeneratingBoxMenu>> GENERATING_BOX_MENU = registerMenuType(GeneratingBoxMenu::new,"generating_box");
    public static final RegistryObject<MenuType<EndCityMenu>> END_CITY_MENU = registerMenuType(EndCityMenu::new,"end_city");
    public static final RegistryObject<MenuType<TrainerMenu>> TRAINER_MENU = registerMenuType(TrainerMenu::new,"shulker_trainer");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
