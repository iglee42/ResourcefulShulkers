package fr.iglee42.resourcefulshulkers.client.screen.widgets;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.client.screen.ChooseItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ChooseItemButton extends AbstractWidget {

    private static final ResourceLocation SLOT_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png");
    private static final int SIZE = 16;
    private static final int PICKER_GAP = 8;

    private final int slot;
    private final Supplier<List<ItemStack>> items;
    private final IntSupplier selectedIndex;
    private final BooleanSupplier enabled;
    private final Opener opener;

    public ChooseItemButton(int x, int y, int slot, Supplier<List<ItemStack>> items, IntSupplier selectedIndex,
                            BooleanSupplier enabled, Opener opener) {
        super(x, y, SIZE, SIZE, Component.empty());
        this.slot = slot;
        this.items = items;
        this.selectedIndex = selectedIndex;
        this.enabled = enabled;
        this.opener = opener;
    }

    public static ChooseItemButton forGeneratingBox(int x, int y, GeneratingBoxBlockEntity be) {
        return new ChooseItemButton(x, y, 0, be::availableItems, be::getItemIndex, () -> !be.isAccelerated(),
                (s, anchorX, anchorY) -> ChooseItemScreen.open(be, anchorX, anchorY));
    }

    @Override
    protected void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!enabled.getAsBoolean()) return;
        List<ItemStack> stacks = items.get();
        if (stacks.isEmpty()) return;
        graphics.blit(SLOT_TEXTURE, getX(), getY(), 0, 0, SIZE, SIZE, SIZE, SIZE);
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        graphics.pose().scale(0.9F, 0.9F, 1F);
        graphics.renderItem(stacks.get(Mth.clamp(selectedIndex.getAsInt(), 0, stacks.size() - 1)), 1, 1);
        graphics.pose().popPose();

        if (isHovered())
            graphics.renderTooltip(Minecraft.getInstance().font, stacks.get(Mth.clamp(selectedIndex.getAsInt(), 0, stacks.size() - 1)), mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!enabled.getAsBoolean()) return;
        opener.open(slot, getX() + width + PICKER_GAP, getY());
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
    }

    @FunctionalInterface
    public interface Opener {
        boolean open(int slot, int anchorX, int anchorY);
    }
}
