package fr.iglee42.resourcefulshulkers.client;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.client.screen.TrainerScreen;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.shulkers.RegisteredShulker;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.RSColors;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RSClient {


    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ShulkersManager.forEachShulker(r -> {
            if (r.definition().id().equals(RSIds.id("dye"))) return;
            event.register((stack, index) -> index == 0 ? FastColor.ARGB32.opaque(r.definition().color()) : 0xffffff, r.shell().get());
        });

        RegisteredShulker dyeShulker = ShulkersManager.getShulker(RSIds.id("dye"));
        if (dyeShulker != null) {
            event.register((stack, index) -> index == 0 ? RSColors.getRGBColor().toARGB() : 0xffffff, dyeShulker.shell().get(), dyeShulker.shulkerItem().get());
        }
    }


    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event) {
        event.register(RSBlockEntities.GENERATING_BOX_MENU.get(), GeneratingBoxScreen::new);
        event.register(RSBlockEntities.END_CITY_MENU.get(), EndCityScreen::new);
        event.register(RSBlockEntities.TRAINER_MENU.get(), TrainerScreen::new);
    }

}
