package fr.iglee42.resourcefulshulkers.client.screen;

import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorIndexChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.List;

public class ChooseItemScreen extends Screen {

    private static final ResourceLocation SLOT_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = RSIds.id("textures/gui/item_choose_background.png");

    private static final int SLOT_SIZE = 16;
    private static final int ROWS = 4;
    private static final int COLUMNS = 4;
    private static final int ITEMS_PER_PAGE = ROWS * COLUMNS;
    private static final int PADDING = 2;
    private static final int ARROW_WIDTH = 12;
    private static final int ARROW_HEIGHT = 12;

    private final BlockPos pos;
    private final int slot;
    private final List<ItemStack> items;
    private final int anchorX;
    private final int anchorY;

    private int page;
    private int gridX;
    private int gridY;

    private ChooseItemScreen(BlockPos pos, int slot, List<ItemStack> items, int anchorX, int anchorY) {
        super(Component.translatable("gui.resourcefulshulkers.choose_item"));
        this.pos = pos;
        this.slot = slot;
        this.items = items;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }


    public static boolean open(BlockPos pos, int slot, List<ItemStack> items, int anchorX, int anchorY) {
        if (items == null || items.size() < 2) return false;
        Minecraft.getInstance().pushGuiLayer(new ChooseItemScreen(pos, slot, List.copyOf(items), anchorX, anchorY));
        return true;
    }

    public static boolean open(GeneratingBoxBlockEntity be, int anchorX, int anchorY) {
        if (be.isAccelerated()) return false;
        return open(be.getBlockPos(), 0, be.availableItems(), anchorX, anchorY);
    }

    public static boolean open(EndCityBlockEntity be, int shulkerSlot, int anchorX, int anchorY) {
        if (be.isAccelerated()) return false;
        return open(be.getBlockPos(), shulkerSlot, be.availableItemsForSlot(shulkerSlot), anchorX, anchorY);
    }


    private int pageCount() {
        return Math.max(1, Mth.positiveCeilDiv(items.size(), ITEMS_PER_PAGE));
    }

    private int pageStart() {
        return page * ITEMS_PER_PAGE;
    }

    private int pageSize() {
        return Math.min(ITEMS_PER_PAGE, items.size() - pageStart());
    }

    private int columns() {
        return Math.max(1, Mth.positiveCeilDiv(pageSize(), ROWS));
    }

    private int rows() {
        return Math.clamp(pageSize(), 1, ROWS);
    }

    private boolean hasArrows() {
        return pageCount() > 1;
    }

    private int arrowY() {
        return gridY - PADDING - ARROW_HEIGHT;
    }

    @Override
    protected void init() {
        layout();
    }

    private void layout() {
        int panelWidth = columns() * SLOT_SIZE + PADDING * 2;
        int panelHeight = rows() * SLOT_SIZE + PADDING * 2;
        int minY = PADDING + (hasArrows() ? ARROW_HEIGHT : 0);
        gridX = Mth.clamp(anchorX, PADDING, Math.max(PADDING, width - panelWidth + PADDING));
        gridY = Mth.clamp(anchorY, minY, Math.max(minY, height - panelHeight + PADDING));
    }

    public Rect2i getPopupBounds() {
        int x = gridX - PADDING;
        int y = gridY - PADDING;
        int popupWidth = columns() * SLOT_SIZE + PADDING * 2;
        int popupHeight = rows() * SLOT_SIZE + PADDING * 2;
        if (hasArrows()) {
            y -= ARROW_HEIGHT;
            popupHeight += ARROW_HEIGHT;
        }
        return new Rect2i(x, y, popupWidth, popupHeight);
    }

    private boolean isOverPopup(double mouseX, double mouseY) {
        Rect2i bounds = getPopupBounds();
        return MouseUtil.isMouseOver(mouseX, mouseY,
                bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    private int hoveredIndex(double mouseX, double mouseY) {
        int col = Mth.floor((mouseX - gridX) / SLOT_SIZE);
        int row = Mth.floor((mouseY - gridY) / SLOT_SIZE);
        if (col < 0 || row < 0 || col >= columns() || row >= ROWS) return -1;
        int index = col * ROWS + row;
        if (index >= pageSize()) return -1;
        return pageStart() + index;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    }



    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        blitNineSliceRepeat(graphics, BACKGROUND_TEXTURE,gridX - PADDING, arrowY(),
                columns() * SLOT_SIZE + PADDING * 2, rows() * SLOT_SIZE + PADDING * 2 + ARROW_HEIGHT, 4,4 ,12,12,0,0);

        if (hasArrows()) {
            if (page > 0) {
                boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, gridX + PADDING, arrowY(), ARROW_WIDTH, ARROW_HEIGHT);
                graphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/transferable_list/move_up"
                                + (hovered ? "_highlighted" : "") + ".png"),
                        gridX + PADDING, arrowY(), 12, 16, 0, 13, 16, 32, 32);
            }
            if (page < pageCount() - 1) {
                boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, gridX + PADDING + ARROW_WIDTH, arrowY(), ARROW_WIDTH, ARROW_HEIGHT);
                graphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/transferable_list/move_down"
                                + (hovered ? "_highlighted" : "") + ".png"),
                        gridX + PADDING + ARROW_WIDTH, arrowY(), 12, 16, 16, 13, 16, 32, 32);
            }
        }

        ItemStack hovered = ItemStack.EMPTY;
        for (int i = 0; i < pageSize(); i++) {
            int x = gridX + (i / ROWS) * SLOT_SIZE;
            int y = gridY + (i % ROWS) * SLOT_SIZE;
            ItemStack stack = items.get(pageStart() + i);
            graphics.blit(SLOT_TEXTURE, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
            graphics.pose().pushPose();
            graphics.pose().translate(x,y,0);
            graphics.pose().scale(0.9F, 0.9F, 0.9F);
            graphics.renderItem(stack, 1, 1);
            graphics.pose().popPose();
            if (MouseUtil.isMouseOver(mouseX, mouseY, x, y, SLOT_SIZE, SLOT_SIZE)) hovered = stack;
        }

        if (!hovered.isEmpty()) graphics.renderTooltip(font, hovered, mouseX, mouseY);
    }

    public static void blitNineSliceRepeat(
            GuiGraphics g,
            ResourceLocation texture,
            int x, int y,
            int width, int height,
            int cornerW, int cornerH,
            int texW, int texH,
            int u, int v
    ) {

        int centerW = texW - cornerW * 2;
        int centerH = texH - cornerH * 2;

        int right = x + width;
        int bottom = y + height;

        int uRight = u + texW - cornerW;
        int vBottom = v + texH - cornerH;

        // coins
        g.blit(texture, x, y, cornerW, cornerH, u, v, cornerW, cornerH, texW, texH);
        g.blit(texture, right - cornerW, y, cornerW, cornerH,uRight, v, cornerW, cornerH, texW, texH);
        g.blit(texture, x, bottom - cornerH, cornerW, cornerH, u, vBottom, cornerW, cornerH, texW, texH);
        g.blit(texture, right - cornerW, bottom - cornerH, cornerW, cornerH, uRight, vBottom, cornerW, cornerH, texW, texH);

        // top / bottom edges (repeat horizontally)
        for (int dx = x + cornerW; dx < right - cornerW; dx += centerW) {
            int w = Math.min(centerW, right - cornerW - dx);

            g.blit(texture, dx, y, w, cornerH,
                    u + cornerW, v, w, cornerH, texW, texH);

            g.blit(texture, dx, bottom - cornerH, w, cornerH,
                    u + cornerW, vBottom, w, cornerH, texW, texH);
        }

        // left / right edges (repeat vertically)
        for (int dy = y + cornerH; dy < bottom - cornerH; dy += centerH) {
            int h = Math.min(centerH, bottom - cornerH - dy);

            g.blit(texture, x, dy, cornerW, h,
                    u, v + cornerH, cornerW, h, texW, texH);

            g.blit(texture, right - cornerW, dy, cornerW, h,
                    uRight, v + cornerH, cornerW, h, texW, texH);
        }

        // center (repeat both directions)
        for (int dx = x + cornerW; dx < right - cornerW; dx += centerW) {
            int w = Math.min(centerW, right - cornerW - dx);

            for (int dy = y + cornerH; dy < bottom - cornerH; dy += centerH) {
                int h = Math.min(centerH, bottom - cornerH - dy);

                g.blit(texture, dx, dy, w, h,
                        u + cornerW, v + cornerH, w, h, texW, texH);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        if (!isOverPopup(mouseX, mouseY)) {
            onClose();
            return true;
        }
        if (button != 0) return true;

        if (hasArrows()) {
            if (MouseUtil.isMouseOver(mouseX, mouseY, gridX + PADDING, arrowY(), ARROW_WIDTH, ARROW_HEIGHT)) {
                if (page > 0) turnPage(-1);
                return true;
            }
            if (MouseUtil.isMouseOver(mouseX, mouseY, gridX + PADDING + ARROW_WIDTH, arrowY(), ARROW_WIDTH, ARROW_HEIGHT)) {
                if (page < pageCount() - 1) turnPage(1);
                return true;
            }
        }

        int index = hoveredIndex(mouseX, mouseY);
        if (index > -1) {
            PacketDistributor.sendToServer(new GeneratorIndexChangePayload(pos, slot, index));
            playClickSound();
            onClose();
        }
        return true;
    }

    private void turnPage(int direction) {
        page = Mth.clamp(page + direction, 0, pageCount() - 1);
        layout();
        playClickSound();
    }

    private void playClickSound() {
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().popGuiLayer();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
