package fr.iglee42.resourcefulshulkers.client;

import fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.client.screen.TrainerScreen;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@EventBusSubscriber(modid = MODID,value = Dist.CLIENT)
public class RSClient {


    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ShulkersManager.forEachShulker(r->
                event.register((stack,index)->
                        index == 0 ? FastColor.ARGB32.opaque(r.definition().color()): 0xffffff, r.shell().get()
                ));
    }


    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event){
        event.register(RSBlockEntities.GENERATING_BOX_MENU.get(), GeneratingBoxScreen::new);
        event.register(RSBlockEntities.END_CITY_MENU.get(), EndCityScreen::new);
        event.register(RSBlockEntities.TRAINER_MENU.get(), TrainerScreen::new);
    }

}
