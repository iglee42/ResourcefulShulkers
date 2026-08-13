package fr.iglee42.resourcefulshulkers.aura;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.config.RSClientConfig;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.StringRepresentable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

@EventBusSubscriber(modid = RSIds.MODID, value = Dist.CLIENT)
public class AuraOverlay implements LayeredDraw.Layer {

    private static final int BAR_WIDTH = 24;
    private static final int BAR_HEIGHT = 84;

    private static int chunkAura;

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.HOTBAR,RSIds.id("aura"),new AuraOverlay());
    }

    @Override
    public void render(GuiGraphics gui, DeltaTracker deltaTracker) {
        Anchor anchor = getAnchor();
        int baseX = anchor.getSerializedName().contains("left") ? 0 : gui.guiWidth() - BAR_WIDTH;
        int baseY = anchor.getSerializedName().contains("top") ? 0 : (anchor.getSerializedName().contains("middle") ? (gui.guiHeight() - BAR_HEIGHT) / 2 : gui.guiHeight() - BAR_HEIGHT - 2);
        int x = anchor.getX(baseX, RSClientConfig.AURA_BAR_OFFSET_X.get());
        int y = anchor.getY(baseY, RSClientConfig.AURA_BAR_OFFSET_Y.get());
        if (x >= 0 && y >= 0 && chunkAura > 0) {
            gui.blit( RSIds.id("textures/gui/aura_bar.png"),x, y, 0, 0, BAR_WIDTH, BAR_HEIGHT, 256, 256);

            int fillHeight = BAR_HEIGHT - 8;
            int length = (int) Math.round(fillHeight*( (double)chunkAura / RSServerConfig.getMaxAura()));
            gui.blit(RSIds.id("textures/gui/aura_bar_full.png"), x + 6, y + 6 + (fillHeight - length), 0, 0, BAR_WIDTH / 2, length, BAR_WIDTH / 2, fillHeight);

            if (Screen.hasShiftDown() && Minecraft.getInstance().options.advancedItemTooltips){
                char[] numbers = String.valueOf(chunkAura).toCharArray();
                for (int i = 1; i <= numbers.length; i++){
                    gui.drawString(Minecraft.getInstance().font, numbers[numbers.length - i] + "",x + 9,y  + fillHeight - 9 - 9*(i>3? i:i-1), 0xffffff);
                }
            }
        }
    }

    private static Anchor getAnchor(){
        return RSClientConfig.AURA_BAR_ANCHOR.get();
    }

    public static void set(int chunkAura) {
        AuraOverlay.chunkAura = chunkAura;
    }
    public static int getChunkAura() {
        return chunkAura;
    }

    public static enum Anchor implements StringRepresentable {
        TOP_LEFT(Integer::sum, Integer::sum),
        TOP_RIGHT((x, offset)-> x - offset, Integer::sum),
        MIDDLE_LEFT(Integer::sum, Integer::sum),
        MIDDLE_RIGHT((x, offset)-> x - offset, Integer::sum),
        BOTTOM_LEFT(Integer::sum, (y, offset)-> y - offset),
        BOTTOM_RIGHT((x, offset)-> x - offset, (y, offset)-> y - offset);

        private final BiFunction<Integer, Integer, Integer> xFunc;
        private final BiFunction<Integer, Integer, Integer> yFunc;

        Anchor(BiFunction<Integer, Integer, Integer> xFunc, BiFunction<Integer, Integer, Integer> yFunc) {
            this.xFunc = xFunc;
            this.yFunc = yFunc;
        }

        public int getX(int x, int offset) {
            return xFunc.apply(x, offset);
        }

        public int getY(int y, int offset) {
            return yFunc.apply(y, offset);
        }

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }
}