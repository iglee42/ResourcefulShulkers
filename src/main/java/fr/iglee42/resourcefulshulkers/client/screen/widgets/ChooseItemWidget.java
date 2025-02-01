package fr.iglee42.resourcefulshulkers.client.screen.widgets;

import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorIndexChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class ChooseItemWidget extends AbstractWidget {

    private GeneratingBoxBlockEntity be;
    private boolean expanded;
    private Map<Integer,Integer[]> indexPos = new HashMap<>();

    public ChooseItemWidget(int x, int y, int height, Component message, GeneratingBoxBlockEntity be) {
        super(x, y, 49, height, message);
        this.be = be;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float pt) {
        if (be.isTimeInABottled()) return;
        guiGraphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png"),getX() + 2 ,getY()+ 1,0,0,16,16,16,16);
        guiGraphics.renderItem(new ItemStack(be.getResourceGenerated().getItems().get(be.getGeneratedIndex())),getX() + 2, getY() + 1);
        if (expanded) {
            int col = 0;
            int row = 0;
            for (int i = 0; i < be.getResourceGenerated().getItems().size(); i++) {
                if (row == 5) {
                    col++;
                    row = 0;
                }
                int centerY = getY() +11;
                int movement = row % 2 == 0 ? row * 8 : - (row+1) * 8;
                guiGraphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png"),getX() + 24 + col * 16,centerY + movement - 10,0,0,16,16,16,16);
                guiGraphics.renderItem(new ItemStack(be.getResourceGenerated().getItems().get(i)),getX() + 24 + col * 16,centerY + movement - 10);

                indexPos.put(i,new Integer[]{col,switch (row){
                    case 0: yield 2;
                    case 1: yield 1;
                    case 2: yield 3;
                    case 4: yield 4;
                    default: yield 0;
                }});

                row++;



            }
            
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_93643_) {
        if (MouseUtil.isMouseOver(mouseX,mouseY,getX() + 2,getY() + 1,18)){
            expanded = !expanded;
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        double x =mouseX - (getX() + 24);
        double y = mouseY - (getY() + 1 - (4*8));
        int col = (int) (x / 16);
        int row = (int) (y / 16);
        int index = indexPos.entrySet().stream().filter(e->e.getValue()[0] == col && e.getValue()[1] == row).map(Map.Entry::getKey).findFirst().orElse(-1);
        if (index > -1){
            PacketDistributor.sendToServer(new GeneratorIndexChangePayload(Minecraft.getInstance().player.getUUID(),be.getBlockPos(),index));
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            expanded = false;
            return true;
        }
        return false;
    }

    public GeneratingBoxBlockEntity getBe() {
        return be;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
