package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.awt.*;

public class GeneratingBoxScreen extends AbstractContainerScreen<GeneratingBoxMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ResourcefulShulkers.MODID,"textures/gui/generating_box.png");


    public GeneratingBoxScreen(GeneratingBoxMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }




    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {}

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        graphics.blit(TEXTURE,x + 35,y +37,0,166,menu.getTile().getGeneratingTick(),8);
        renderTooltip(graphics, mouseX, mouseY);
        String generating = menu.getTile().getResourceGenerated().getItem() != Items.AIR ?"Generating : " + Component.translatable(menu.getTile().getResourceGenerated().getItem().getDescriptionId()).getString() : "Resource Not Found";
        if (menu.getTile().isTimeInABottled()){
            generating = "You can't time in bottle this block";
        }
        int xGeneratingPos = x + (imageWidth / 2);
        graphics.drawCenteredString(font, generating, xGeneratingPos ,y + 40 , menu.getTile().getResourceGenerated().getItem()!= Items.AIR && !menu.getTile().isTimeInABottled() ? Color.GRAY.getRGB(): ChatFormatting.RED.getColor());
        String dura = menu.getTile().getRemainingDurability() > 0 ? "Durability : "  + menu.getTile().getRemainingDurability() + "/"+ GeneratingBoxBlockEntity.MAX_DURABILITY : "Reload Needed";
        int xDuraPos = x + (imageWidth / 2);
        if (menu.getTile().isTimeInABottled())
            dura = "CHEH !";
        graphics.drawCenteredString(font, dura, xDuraPos ,y + 5 ,menu.getTile().getRemainingDurability() > 0 && !menu.getTile().isTimeInABottled()? Color.GRAY.getRGB() : ChatFormatting.RED.getColor());
    }


}
