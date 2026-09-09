package fr.iglee42.resourcefulshulkers.jei.handlers;

import fr.iglee42.resourcefulshulkers.client.screen.ChooseItemScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChooseItemPanelBoundsHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(T screen) {
        if (Minecraft.getInstance().screen instanceof ChooseItemScreen picker)
            return List.of(picker.getPopupBounds());
        return List.of();
    }
}
