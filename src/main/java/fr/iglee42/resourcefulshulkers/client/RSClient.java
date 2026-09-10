package fr.iglee42.resourcefulshulkers.client;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.client.screen.TrainerScreen;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.shulkers.RegisteredShulker;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.RSColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RSClient {


    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ShulkersManager.forEachShulker(r -> {
            if (r.definition().id().equals(RSIds.id("dye"))) return;
            event.register((stack, index) -> index == 0 ? 0xff000000 | r.definition().color() : 0xffffff, r.shell().get());
        });

        RegisteredShulker dyeShulker = ShulkersManager.getShulker(RSIds.id("dye"));
        if (dyeShulker != null) {
            event.register((stack, index) -> index == 0 ? RSColors.getRGBColor().toARGB() : 0xffffff, dyeShulker.shell().get(), dyeShulker.shulkerItem().get());
        }
    }


    @SubscribeEvent
    public static void registerMenus(FMLClientSetupEvent event) {
        event.enqueueWork(()->{
            MenuScreens.register(RSBlockEntities.GENERATING_BOX_MENU.get(), GeneratingBoxScreen::new);
            MenuScreens.register(RSBlockEntities.END_CITY_MENU.get(), EndCityScreen::new);
            MenuScreens.register(RSBlockEntities.TRAINER_MENU.get(), TrainerScreen::new);
        });
    }

}
