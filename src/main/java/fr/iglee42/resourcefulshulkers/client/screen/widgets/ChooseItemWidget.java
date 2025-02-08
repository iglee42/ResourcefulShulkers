package fr.iglee42.resourcefulshulkers.client.screen.widgets;

import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorIndexChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChooseItemWidget extends AbstractWidget {

    private GeneratingBoxBlockEntity be;
    private boolean expanded;
    private int currentPage;
    private Map<Integer, List<Integer>> pages = new HashMap<>();
    private Map<Integer, Integer[]> indexPos = new HashMap<>();
    private boolean isPageUpHovered;
    private boolean isPageDownHovered;

    public ChooseItemWidget(int x, int y, int height, Component message, GeneratingBoxBlockEntity be) {
        super(x, y, 49, height, message);
        this.be = be;
        List<Item> items = be.getResourceGenerated().getItems();
        int totalItems = items.size();
        int itemsPerPage = 16;
        int pageCount = (int) Math.ceil((double) totalItems / itemsPerPage);

        for (int page = 0; page < pageCount; page++) {
            int startIdx = page * itemsPerPage;
            int endIdx = Math.min(startIdx + itemsPerPage, totalItems);
            List<Integer> indices = new ArrayList<>();
            for (int i = startIdx; i < endIdx; i++) {
                indices.add(i);
            }
            pages.put(page, indices);
        }

    }

    @Override
    protected void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pt) {
        if (be.isTimeInABottled())
            return;

        guiGraphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png"), getX() + 2,
                getY() + 1, 0, 0, 16, 16, 16, 16);


        guiGraphics.renderItem(new ItemStack(be.getResourceGenerated().getItems().get(be.getGeneratedIndex())),
                getX() + 2, getY() + 1);



        if (expanded) {
            List<Integer> page = pages.get(currentPage);
            int x = (getX() + 24);
            int y = (getY() + 1);
            int maxCols = (int) Math.ceil(page.size() / 4);
            if (maxCols == 0) maxCols = 1;
            int maxRows = 4;

            if (page.size() == 16) {
                if (currentPage < pages.size() - 1)
                    guiGraphics.blit(
                            ResourceLocation.withDefaultNamespace("textures/gui/sprites/transferable_list/move_down"
                                    + (isPageDownHovered ? "_highlighted" : "") + ".png"),
                            x + 12, y - 16, 12, 16, 16, 13, 16, 32, 32);
            }
            if (currentPage > 0)
                guiGraphics.blit(
                            ResourceLocation.withDefaultNamespace("textures/gui/sprites/transferable_list/move_up"+(isPageUpHovered ? "_highlighted" : "")+".png"),
                            x, y - 16, 12, 16, 0, 13, 16, 32, 32);

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,
                            "textures/gui/item_choose_background.png"),
                    x, y - 2, maxCols * 16 + 4, maxRows * 16 + 4, 0, 0, 166, 166, 256, 256);

            int col = 0;
            int row = 0;
            for (int i = 0; i < page.size(); i++) {
                if (row == 4) {
                    col++;
                    row = 0;
                }
                guiGraphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png"),getX() + 26 + col * 16, y + row * 16, 0, 0, 16, 16, 16, 16);
                guiGraphics.renderItem(new ItemStack(be.getResourceGenerated().getItems().get(i + currentPage * 16)),getX() + 26 + col * 16, y + row * 16);

                indexPos.put(i, new Integer[] { col, row });
                row++;

            }

        }

    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (expanded) {
            isPageUpHovered = MouseUtil.isMouseOver(mouseX, mouseY, getX() + 24, getY() - 16, 12, 16);
            isPageDownHovered = MouseUtil.isMouseOver(mouseX, mouseY, getX() + 36, getY() - 16, 12, 16);
        }
        super.mouseMoved(mouseX, mouseY);
        
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_93643_) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, getX() + 2, getY() + 1, 18)) {
            expanded = !expanded;
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        if (expanded) {
            if (MouseUtil.isMouseOver(mouseX, mouseY, getX() + 24, getY() - 16, 12, 16) && currentPage > 0) {
                currentPage--;
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            if (MouseUtil.isMouseOver(mouseX, mouseY, getX() + 36, getY() - 16, 12, 16) && currentPage <  pages.size() - 1) {
                currentPage++;
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }

            double x = mouseX - (getX() + 26);
            double y = mouseY - (getY() + 1);
            int col = (int) (x / 16);
            int row = (int) (y / 16);
            int index = indexPos.entrySet().stream().filter(e -> e.getValue()[0] == col && e.getValue()[1] == row)
                    .map(Map.Entry::getKey).findFirst().orElse(-1);
            if (x < 0 || y < 0)
                index = -1;

            if (index > -1) {
                PacketDistributor.sendToServer(
                        new GeneratorIndexChangePayload(Minecraft.getInstance().player.getUUID(), be.getBlockPos(),
                                pages.get(currentPage).get(index)));
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                expanded = false;
                return true;
            }
        }
        return false;
    }

    public GeneratingBoxBlockEntity getBe() {
        return be;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Map<Integer, List<Integer>> getPages() {
        return pages;
    }
}
