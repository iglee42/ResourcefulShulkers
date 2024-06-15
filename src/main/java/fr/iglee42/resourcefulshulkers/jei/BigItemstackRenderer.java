package fr.iglee42.resourcefulshulkers.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BigItemstackRenderer implements IIngredientRenderer<ItemStack> {

    public void render(GuiGraphics guiGraphics, @Nullable ItemStack ingredient) {
        if (ingredient != null) {
            guiGraphics.pose().scale(1.5F,1.5F,1.0F);
            RenderSystem.enableDepthTest();
            Minecraft minecraft = Minecraft.getInstance();
            Font font = this.getFontRenderer(minecraft, ingredient);
            guiGraphics.renderFakeItem(ingredient, 0, 0);
            guiGraphics.renderItemDecorations(font, ingredient, 0, 0);
            RenderSystem.disableBlend();
        }

    }

    public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        return ingredient.getTooltipLines(player, tooltipFlag);
    }

    public Font getFontRenderer(Minecraft minecraft, ItemStack ingredient) {
        return Minecraft.getInstance().font;
    }


    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }

}
