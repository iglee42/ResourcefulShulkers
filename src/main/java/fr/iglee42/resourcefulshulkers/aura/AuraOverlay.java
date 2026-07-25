package fr.iglee42.resourcefulshulkers.aura;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

@EventBusSubscriber(modid = ResourcefulShulkers.MODID, value = Dist.CLIENT)
public class AuraOverlay implements LayeredDraw.Layer {

    private static int chunkAura;

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.HOTBAR,ResourceLocation.fromNamespaceAndPath(MODID,"aura"),new AuraOverlay());
    }

    @Override
    public void render(GuiGraphics gui, DeltaTracker deltaTracker) {
        int x = gui.guiWidth() - ResourcefulShulkersConfig.Client.AURA_BAR_X.get();
        int y = ResourcefulShulkersConfig.Client.AURA_BAR_Y.get();
        if (x >= 0 && y >= 0 && chunkAura > 0) {
            gui.blit( ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/aura_bar.png"),x, y, 0, 0, 24, 84, 256, 256);

            int length = (int) Math.round(76*( (double)chunkAura / ShulkerAura.MAX_AURA));
            gui.blit(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/aura_bar_full.png"), x + 6, y + 6 + (76- length), 0, 0, 12, length, 12, 76);

            if (Screen.hasShiftDown() && Minecraft.getInstance().options.advancedItemTooltips){
                char[] numbers = String.valueOf(chunkAura).toCharArray();
                for (int i = 1; i <= numbers.length; i++){

                    gui.drawString(Minecraft.getInstance().font, numbers[numbers.length - i] + "",x + 9,y  + 76 - 9 - 9*(i>3? i:i-1), 0xffffff);
                }
            }
        }
    }

    public static void set(int chunkAura) {
        AuraOverlay.chunkAura = chunkAura;
    }
    public static int getChunkAura() {
        return chunkAura;
    }
}