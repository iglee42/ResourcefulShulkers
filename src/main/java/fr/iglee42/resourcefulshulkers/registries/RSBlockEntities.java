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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RSBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RSIds.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, RSIds.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new, RSBlocks.getAllGeneratingBox()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerInfuserBlockEntity>> SHULKER_INFUSER = BLOCK_ENTITIES.register("shulker_infuser", ()->BlockEntityType.Builder.of(ShulkerInfuserBlockEntity::new, RSBlocks.SHULKER_INFUSER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerPedestalBlockEntity>> SHULKER_PEDESTAL = BLOCK_ENTITIES.register("shulker_pedestal", ()->BlockEntityType.Builder.of(ShulkerPedestalBlockEntity::new, RSBlocks.SHULKER_PEDESTAL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerAbsorberBlockEntity>> SHULKER_ABSORBER = BLOCK_ENTITIES.register("shulker_absorber", ()->BlockEntityType.Builder.of(ShulkerAbsorberBlockEntity::new, RSBlocks.SHULKER_ABSORBER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<PurpurTargetBlockEntity>> PURPUR_TARGET = BLOCK_ENTITIES.register("purpur_target", ()->BlockEntityType.Builder.of(PurpurTargetBlockEntity::new, RSBlocks.PURPUR_TARGET.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<EndCityBlockEntity>> END_CITY = BLOCK_ENTITIES.register("end_city", ()->BlockEntityType.Builder.of(EndCityBlockEntity::new,RSBlocks.END_CITY.get(), RSBlocks.END_CITY_TIER_2.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TrainerBlockEntity>> TRAINER = BLOCK_ENTITIES.register("shulker_trainer", ()->BlockEntityType.Builder.of(TrainerBlockEntity::new,RSBlocks.TRAINER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<StructureBlockEntity>> STRUCTURE = BLOCK_ENTITIES.register("structure", ()->BlockEntityType.Builder.of(StructureBlockEntity::new, RSBlocks.STRUCTURE.get()).build(null));

    public static final DeferredHolder<MenuType<?>,MenuType<GeneratingBoxMenu>> GENERATING_BOX_MENU = registerMenuType(GeneratingBoxMenu::new,"generating_box");
    public static final DeferredHolder<MenuType<?>,MenuType<EndCityMenu>> END_CITY_MENU = registerMenuType(EndCityMenu::new,"end_city");
    public static final DeferredHolder<MenuType<?>,MenuType<TrainerMenu>> TRAINER_MENU = registerMenuType(TrainerMenu::new,"shulker_trainer");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>,MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                              String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
