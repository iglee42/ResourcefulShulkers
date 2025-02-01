package fr.iglee42.resourcefulshulkers.aura;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class AuraOverlay {

    public static final LayeredDraw.Layer HUD_AURA = (gui, tracker) -> {
        String toDisplay = String.valueOf(ClientAuraData.getChunkMana());
        int x = gui.guiWidth() - 24;
        int y = 0;
        if (x >= 0 && y >= 0 && ClientAuraData.getChunkMana() > 0) {
            gui.blit( ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/aura_bar.png"),x, y, 0, 0, 24, 84, 256, 256);

            int lenght = 84;

            lenght = (int) Math.round(76*( (double)ClientAuraData.getChunkMana() / ShulkerAura.MAX_AURA));
            gui.blit(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/aura_bar_full.png"), x + 6, y + 6 + (76- lenght), 0, 0, 12, lenght, 12, 76);

            if (Screen.hasShiftDown() && Minecraft.getInstance().options.advancedItemTooltips){
                char[] numbers = String.valueOf(ClientAuraData.getChunkMana()).toCharArray();
                for (int i = 1; i <= numbers.length; i++){

                    gui.drawString(Minecraft.getInstance().font, numbers[numbers.length - i] + "",x + 9,y  + 76 - 9 - 9*(i>3? i:i-1), 0xffffff);
                }
            }
        }
    };
}