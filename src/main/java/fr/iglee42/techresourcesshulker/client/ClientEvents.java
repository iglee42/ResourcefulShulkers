package fr.iglee42.techresourcesshulker.client;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.client.blockentites.GeneratingBoxRenderer;
import fr.iglee42.techresourcesshulker.client.screen.GeneratingBoxScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModContent.GENERATING_BOX_MENU.get(), GeneratingBoxScreen::new);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModContent.GENERATING_BOX_BLOCK_ENTITY.get(), GeneratingBoxRenderer::new);
    }

}
