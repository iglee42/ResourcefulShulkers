package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerAbsorberBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourcefulShulkers.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, ResourcefulShulkers.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GeneratingBoxBlockEntity>> GENERATING_BOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("generating_box", ()->BlockEntityType.Builder.of(GeneratingBoxBlockEntity::new,ModBlocks.getAllBox()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerInfuserBlockEntity>> SHULKER_INFUSER_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_infuser", ()->BlockEntityType.Builder.of(ShulkerInfuserBlockEntity::new,ModBlocks.SHULKER_INFUSER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerPedestalBlockEntity>> SHULKER_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_pedestal", ()->BlockEntityType.Builder.of(ShulkerPedestalBlockEntity::new,ModBlocks.SHULKER_PEDESTAL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShulkerAbsorberBlockEntity>> SHULKER_ABSORBER_BLOCK_ENTITY = BLOCK_ENTITIES.register("shulker_absorber", ()->BlockEntityType.Builder.of(ShulkerAbsorberBlockEntity::new,ModBlocks.SHULKER_ABSORBER.get()).build(null));

    public static final DeferredHolder<MenuType<?>,MenuType<GeneratingBoxMenu>> GENERATING_BOX_MENU = registerMenuType(GeneratingBoxMenu::new,"generating_box_menu");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>,MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                                              String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
