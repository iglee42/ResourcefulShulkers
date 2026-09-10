package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.client.screen.widgets.ChooseItemButton;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GeneratingBoxScreen extends AbstractContainerScreen<GeneratingBoxMenu> {
    private static final ResourceLocation TEXTURE =
            RSIds.id("textures/gui/generating_box.png");

    public GeneratingBoxScreen(GeneratingBoxMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        if (menu.getBlockEntity().availableItems().size() > 1)
            addRenderableWidget(ChooseItemButton.forGeneratingBox(
                    getGuiLeft() + imageWidth - 23, getGuiTop() + 36, menu.getBlockEntity()));
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

        int progress = menu.getBlockEntity().getProgress();
        graphics.blit(TEXTURE,x + 35,y +37,0,166, (int) ((progress / (float) RSServerConfig.BOX_MAX_PROGRESS.get()) * 100),8);

        renderTooltip(graphics, mouseX, mouseY);


        boolean isInvalid = menu.getBlockEntity().currentItem().isEmpty() || menu.getBlockEntity().isAccelerated();
        graphics.drawString(font, getGeneratingText(menu.getBlockEntity().currentItem(), menu.getBlockEntity().isAccelerated()), x + 7 ,y + 40 , !isInvalid ? 4210752: ChatFormatting.RED.getColor(), false);

        int durability = menu.getBlockEntity().getDurability();
        float durabilityProgress = Math.max(0,durability / (float) RSServerConfig.getMaxDurability());
        Component durabilityText = durability > 0 ?
                Component.translatable(
                        "gui.resourcefulshulkers.durability",
                        Component.literal(String.valueOf(durability)).withStyle(style->style.withColor(Mth.hsvToRgb(durabilityProgress / 3.0F, 0.9F, 0.9F))),
                        RSServerConfig.getMaxDurability())
                : Component.translatable("gui.resourcefulshulkers.reload").withStyle(ChatFormatting.RED);
        drawCenteredString(graphics,font, durabilityText,x + imageWidth / 2, y+5,4210752);

        String addedDurabilityText = "+"+menu.getBlockEntity().getAddedDurability();
        graphics.drawString(font,addedDurabilityText , x+150 - font.width(addedDurabilityText) ,y + 58 , 4210752,false);
    }

    public void drawCenteredString(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
        graphics.drawString(font, text, x - font.width(text) / 2, y, color,false);
    }

    private Component getGeneratingText(ItemStack stack, boolean isAccelerated){
        if (stack.isEmpty()) return Component.translatable("gui.resourcefulshulkers.resource_not_found");
        if (isAccelerated) return Component.translatable("gui.resourcefulshulkers.no_acceleration");
        return Component.translatable("gui.resourcefulshulkers.generating", Component.translatable(stack.getDescriptionId()));
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
