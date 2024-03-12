package fr.iglee42.resourcefulshulkers.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        assignEnergyRenderer();
    }


    private void assignEnergyRenderer() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
    }


    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);


    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(pPoseStack,x + 35,y +37,0,166,menu.getTile().getGeneratingTick(),8);
        renderTooltip(pPoseStack, mouseX, mouseY);
        String generating = menu.getTile().getResourceGenerated().getItem() != Items.AIR ?"Generating : " + new TranslatableComponent(menu.getTile().getResourceGenerated().getItem().getDescriptionId()).getString() : "Resource Not Found";
        int xGeneratingPos = x + 78 + (generating.length() / 2);
        drawCenteredString(pPoseStack,font, generating, xGeneratingPos ,y + 40 , menu.getTile().getResourceGenerated().getItem()!= Items.AIR ? Color.GRAY.getRGB(): ChatFormatting.RED.getColor());
        String dura = menu.getTile().getRemainingDurability() > 0 ? "Durability : "  + menu.getTile().getRemainingDurability() + "/"+ GeneratingBoxBlockEntity.MAX_DURABILITY : "Reload Needed";
        int xDuraPos = x + 78 + (dura.length() / 2);
        drawCenteredString(pPoseStack,font, dura, xDuraPos ,y + 5 ,menu.getTile().getRemainingDurability() > 0 ? Color.GRAY.getRGB() : ChatFormatting.RED.getColor());

    }

}
