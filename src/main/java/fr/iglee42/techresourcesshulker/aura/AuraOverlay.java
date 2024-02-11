package fr.iglee42.techresourcesshulker.aura;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

import static net.minecraft.client.gui.GuiComponent.blit;

public class AuraOverlay {

    public static final IIngameOverlay HUD_AURA = (gui, poseStack, partialTicks, width, height) -> {
        String toDisplay = String.valueOf(ClientAuraData.getChunkMana());
        int x = width - 24;
        int y = 0;
        if (x >= 0 && y >= 0 && ClientAuraData.getChunkMana() > 0) {
            RenderSystem.setShaderTexture(0, new ResourceLocation(TechResourcesShulker.MODID, "textures/gui/aura_bar.png"));
            blit(poseStack, x, y, 0, 0, 24, 84, 256, 256);

            int lenght = 84;

            lenght = (int) Math.round(76*( (double)ClientAuraData.getChunkMana() / ShulkerAura.MAX_AURA));
            RenderSystem.setShaderTexture(0, new ResourceLocation(TechResourcesShulker.MODID, "textures/gui/aura_bar_full.png"));
            blit(poseStack, x + 6, y + 6 + (76- lenght), 0, 0, 12, lenght, 12, 76);

            if (Screen.hasShiftDown() && Minecraft.getInstance().options.advancedItemTooltips){
                char[] numbers = String.valueOf(ClientAuraData.getChunkMana()).toCharArray();
                for (int i = 1; i <= numbers.length; i++){

                    gui.getFont().draw(poseStack,numbers[numbers.length - i] + "",x + 9,y  + 76 - 9 - 9*(i>3? i:i-1), 0xffffff);
                }
            }
        }
    };
}