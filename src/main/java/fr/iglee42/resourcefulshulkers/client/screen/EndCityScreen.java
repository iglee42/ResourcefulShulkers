package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.GeneratingBoxHandler;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.menu.EndCityMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import static fr.iglee42.resourcefulshulkers.menu.EndCityMenu.SHULKER_SLOTS;

public class EndCityScreen extends AbstractContainerScreen<EndCityMenu> {
    public static final ResourceLocation TEXTURE =
            RSIds.id("textures/gui/end_city.png");

    public static final ResourceLocation DISABLED_SLOT = RSIds.id("textures/gui/disabled_slot.png");

    public static final int TOTAL_SLOTS = 8;

    public EndCityScreen(EndCityMenu menu, Inventory inventory, Component component) {
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
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);

        int tier = menu.getBlockEntity().getTier();
        int inactiveSlots = TOTAL_SLOTS - tier * 4;
        int activeSlots = TOTAL_SLOTS - inactiveSlots;

        for (int slot = activeSlots; slot < TOTAL_SLOTS; slot++) {
            int[] pos = SHULKER_SLOTS[slot];
            graphics.blit(DISABLED_SLOT, x + pos[0], y + pos[1], 16,160, 0,16,16,16,16);
            graphics.blit(DISABLED_SLOT, x + 8 + slot *18,y+86,16,160,0, 16,16,16,16);
        }

        int shulkersY = y + 6;
        int shulkersX = x + 174;
        for (int shulker = 0; shulker < menu.getBlockEntity().getShulkers().getSlots(); shulker++) {
            ItemStack stack = menu.getBlockEntity().getShulkers().getStackInSlot(shulker);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof GeneratingBoxItem it)) continue;
            IShulkerDefinition definition = it.definition();
            int durability = menu.getBlockEntity().getShulkers().getDurabilityOfSlot(shulker);
            float durabilityProgress = Math.max(0,durability / (float) RSServerConfig.getMaxDurability());
            Component durabilityText = durability > 0 ?
                            Component.literal(durability + "/"+RSServerConfig.getMaxDurability()).withStyle(style->style.withColor(Mth.hsvToRgb(durabilityProgress / 3.0F, 0.9F, 0.9F)))
                    : Component.translatable("gui.resourcefulshulkers.reload").withStyle(ChatFormatting.RED);

            Component generatingText = getGeneratingText(menu.getBlockEntity().generatedItemForSlot(shulker));

            graphics.renderItem(stack, shulkersX, shulkersY + 2);
            graphics.blit(TEXTURE, shulkersX, shulkersY + 23,0, 220, 72, 1);
            int progress = menu.getBlockEntity().getProgressPerSlot().getOrDefault(shulker, 0);
            graphics.blit(TEXTURE, shulkersX, shulkersY + 23,0, 221, (int) (((float)progress / RSServerConfig.BOX_MAX_PROGRESS.get()) * 72), 1);

            graphics.pose().pushPose();
            graphics.pose().translate(shulkersX + 20, shulkersY,0);
            graphics.pose().scale(0.75f,0.75f,1);

            graphics.drawString(font, Component.literal(definition.name()),  0, 0, 4210752, false);
            graphics.drawString(font, durabilityText,  0, 10, 4210752);
            graphics.drawString(font, generatingText,  0, 20, ChatFormatting.WHITE.getColor());

            graphics.pose().popPose();

            shulkersY+=26;
        }

        if (menu.getBlockEntity().isAccelerated()){
            Component acceleratedText = Component.translatable("gui.resourcefulshulkers.no_acceleration");
            graphics.drawString(font, acceleratedText, x + 5 ,y + 74 , ChatFormatting.RED.getColor(), false);
        }

        String addedDurabilityText = "+"+menu.getBlockEntity().getAddedDurability();
        graphics.drawString(font,addedDurabilityText , x+154 ,y + 90 , 4210752,false);

        renderTooltip(graphics, mouseX, mouseY);
    }

    public void drawCenteredString(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
        graphics.drawString(font, text, x - font.width(text) / 2, y, color,false);
    }

    private Component getGeneratingText(ItemStack stack){
        if (stack.isEmpty()) return Component.translatable("gui.resourcefulshulkers.resource_not_found");
        return stack.getHoverName();
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void slotClicked(Slot slot, int l, int click, ClickType clickType) {
        if (click == 1){
            if (slot instanceof SlotItemHandler slotHandler && slotHandler.getItemHandler() instanceof GeneratingBoxHandler){
                int shulkerIndex = slotHandler.getContainerSlot();
                if (ChooseItemScreen.open(menu.getBlockEntity(), shulkerIndex,
                        slot.x + leftPos + 18, slot.y + topPos)) return;
            }
        }
        super.slotClicked(slot, l, click, clickType);
    }
}
