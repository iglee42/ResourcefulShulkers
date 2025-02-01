package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.client.screen.widgets.ChooseItemWidget;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.awt.*;

public class GeneratingBoxScreen extends AbstractContainerScreen<GeneratingBoxMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"textures/gui/generating_box.png");


    public GeneratingBoxScreen(GeneratingBoxMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ChooseItemWidget(getGuiLeft()+imageWidth - 25,getGuiTop()+ 35,20,Component.empty(),menu.getTile()));
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
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        graphics.blit(TEXTURE,x + 35,y +37,0,166,menu.getTile().getGeneratingTick(),8);
        renderTooltip(graphics, mouseX, mouseY);
        String generating = menu.getTile().getResourceGenerated().getItems().getFirst() != Items.AIR ?"Generating : " + Component.translatable(menu.getTile().getResourceGenerated().getItems().get(menu.getTile().getGeneratedIndex()).getDescriptionId()).getString() : "Resource Not Found";
        if (menu.getTile().isTimeInABottled()){
            generating = "You can't time in bottle this block";
        }
        int xGeneratingPos = x + 7;
        graphics.drawString(font, generating, xGeneratingPos ,y + 40 , menu.getTile().getResourceGenerated().getItems().get(menu.getTile().getGeneratedIndex())!= Items.AIR && !menu.getTile().isTimeInABottled() ? 4210752: ChatFormatting.RED.getColor(), false);
        String dura = menu.getTile().getRemainingDurability() > 0 ? "Durability : " : "Reload Needed";
        String duraRemain = menu.getTile().getRemainingDurability()+ "" ;
        String duraEnd = "/"+ GeneratingBoxBlockEntity.MAX_DURABILITY;
        if (menu.getTile().isTimeInABottled())
            dura = "CHEH !";
        int xDuraPos = x + (imageWidth / 2) - (font.width(dura + (menu.getTile().getRemainingDurability() > 0 && !menu.getTile().isTimeInABottled() ?  duraRemain + duraEnd : "")) / 2);
        graphics.drawString(font, dura, xDuraPos ,y + 5 ,menu.getTile().getRemainingDurability() > 0 && !menu.getTile().isTimeInABottled()? 4210752: ChatFormatting.RED.getColor(),false);
        if (menu.getTile().getRemainingDurability() > 0 && !menu.getTile().isTimeInABottled()) {
            int xDuraRemain = xDuraPos + font.width(dura);
            int xDuraEnd = xDuraRemain + font.width(duraRemain);
            float f = Math.max(0.0F, (float) menu.getTile().getRemainingDurability() / GeneratingBoxBlockEntity.MAX_DURABILITY);
            graphics.drawString(font, duraRemain, xDuraRemain, y + 5, Mth.hsvToRgb(f / 3.0F, 0.9F, 0.9F), false);
            graphics.drawString(font, duraEnd, xDuraEnd, y + 5,4210752 , false);
        }
        String duraAdded = "+"+menu.getTile().calculateAddedDurability();
        graphics.drawString(font,duraAdded , x+150 - font.width(duraAdded) ,y + 58 ,4210752,false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
