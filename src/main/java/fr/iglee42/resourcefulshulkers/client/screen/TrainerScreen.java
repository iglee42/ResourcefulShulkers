package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.item.shulker.ResourceShulkerItem;
import fr.iglee42.resourcefulshulkers.menu.TrainerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen.DISABLED_SLOT;
import static fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen.TEXTURE;
import static fr.iglee42.resourcefulshulkers.menu.EndCityMenu.SHULKER_SLOTS;

public class TrainerScreen extends AbstractContainerScreen<TrainerMenu> {

    public TrainerScreen(TrainerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 256;
        this.imageHeight = 220;
        this.inventoryLabelY = 128;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);


        for (int slot = 4; slot < 8; slot++) {
            int[] pos = SHULKER_SLOTS[slot];
            graphics.blitSprite(DISABLED_SLOT, x + pos[0], y + pos[1], 16, 16);
        }
        for (int slot = 0; slot < 8; slot ++){
            graphics.blitSprite(DISABLED_SLOT, x + 8 + slot *18,y+86, 16,16 );
        }

        for (int slot = 2; slot < 4; slot ++){
            graphics.blitSprite(DISABLED_SLOT, x + 8 + slot *18,y+108, 16,16 );
        }

        int shulkersY = y + 6;
        int shulkersX = x + 174;
        for (int shulker = 0; shulker < menu.getBlockEntity().getShulkers().getSlots(); shulker++) {
            ItemStack stack = menu.getBlockEntity().getShulkers().getStackInSlot(shulker);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof ResourceShulkerItem it)) continue;
            IShulkerDefinition definition = it.definition();
            /*int durability = menu.getBlockEntity().getShulkers().getDurabilityOfSlot(shulker);
            float durabilityProgress = Math.max(0,durability / (float) RSServerConfig.getMaxDurability());
            Component durabilityText = durability > 0 ?
                            Component.literal(durability + "/"+RSServerConfig.getMaxDurability()).withStyle(style->style.withColor(Mth.hsvToRgb(durabilityProgress / 3.0F, 0.9F, 0.9F)))
                    : Component.translatable("gui.resourcefulshulkers.reload").withStyle(ChatFormatting.RED);*/

            //Component generatingText = getGeneratingText(menu.getBlockEntity().generatedItemForSlot(shulker));

            graphics.renderItem(stack, shulkersX, shulkersY + 2);
            graphics.blit(TEXTURE, shulkersX, shulkersY + 23,0, 220, 72, 1);
            int progress = menu.getBlockEntity().getProgressPerSlot().getOrDefault(shulker, 0);
            graphics.blit(TEXTURE, shulkersX, shulkersY + 23,0, 221, (int) (((float)progress / RSServerConfig.TRAINER_MAX_PROGRESS.get()) * 72), 1);

            graphics.pose().pushPose();
            graphics.pose().translate(shulkersX + 20, shulkersY,0);
            graphics.pose().scale(0.75f,0.75f,1);

            graphics.drawString(font, Component.literal(definition.name()),  0, 0, 4210752, false);
            //graphics.drawString(font, durabilityText,  0, 10, 4210752);
            //graphics.drawString(font, generatingText,  0, 20, ChatFormatting.WHITE.getColor());

            graphics.pose().popPose();

            shulkersY+=26;
        }

        if (menu.getBlockEntity().isAccelerated()){
            Component acceleratedText = Component.translatable("gui.resourcefulshulkers.no_acceleration");
            graphics.drawString(font, acceleratedText, x + 5 ,y + 74 , ChatFormatting.RED.getColor(), false);
        }


        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
