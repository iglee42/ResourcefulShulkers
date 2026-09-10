package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.igleelib.common.init.ModCreativeTab;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

public class RSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers"))
            .icon(()->new ItemStack(RSItems.BASE_ESSENCE.get()))
            .withTabsBefore(ModCreativeTab.TAB.getId())
            .build());
    public static final RegistryObject<CreativeModeTab> SHULKERS = CREATIVE_TABS.register("shulkers",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers.shulkers"))
            .icon(()->new ItemStack(RSItems.SHULKER.get()))
            .withTabsBefore(MAIN.getId())
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey().equals(SHULKERS.getKey())) {
            ShulkersManager.forEachShulker(shulker -> {
                if (!shulker.definition().hasItem()) return;
                event.accept(shulker.shulkerItem().get());
                event.accept(shulker.shell().get());
                event.accept(shulker.generatingBox().get());
            });
        }
        if (event.getTabKey().equals(MAIN.getKey())){
            event.accept(RSItems.SHULKER.get());
            event.accept(RSItems.SHULKER_HEAD.get());
            event.accept(RSItems.BASE_ESSENCE.get());
            event.accept(RSItems.UPGRADE_BASE.get());
            event.accept(RSItems.SPEED_UPGRADE.get());
            event.accept(RSItems.DURABILITY_UPGRADE.get());
            event.accept(RSItems.QUANTITY_UPGRADE.get());
            event.accept(RSItems.SHELL_UPGRADE.get());

            event.accept(RSBlocks.SHULKER_ABSORBER.get());
            event.accept(RSBlocks.SHULKER_INFUSER.get());
            event.accept(RSBlocks.SHULKER_PEDESTAL.get());
            event.accept(RSBlocks.PURPUR_TARGET.get());
            event.accept(RSBlocks.TRAINER.get());
            event.accept(RSBlocks.END_CITY.get());
            event.accept(RSBlocks.END_CITY_TIER_2.get());

            TypesManager.forEachType(type -> {
                if (type.definition().equals(TypesManager.getElementalType())) return;
                event.accept(type.shulkerItem().get());
                event.accept(type.essenceItem().get());
            });
        }
    }
}