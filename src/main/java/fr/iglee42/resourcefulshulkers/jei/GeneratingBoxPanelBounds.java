package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.client.screen.widgets.ChooseItemWidget;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GeneratingBoxPanelBounds implements IGuiContainerHandler<GeneratingBoxScreen> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(GeneratingBoxScreen screen) {
        List<Rect2i> bounds = new ArrayList<>();
        if (screen.getMenu().getTile().getResourceGenerated().getItems().size() > 1)screen.children().stream().filter(e-> e instanceof ChooseItemWidget w && w.isExpanded()).map(e->((ChooseItemWidget) e)).forEach(c->{
            int x = (c.getX() + 24);
            int y = (c.getY() + 1);
            int cols = (int) Math.ceil(c.getPages().get(c.getCurrentPage()).size() / 4) - 1;
            int row = 4;
            bounds.add(new Rect2i(x,y,cols * 16 + 16, row * 16 + 8 ));
        });
        return bounds;
    }
}
