package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShellItem extends Item {
    private final IShulkerDefinition definition;

    public ShellItem(IShulkerDefinition definition) {
        super(new Item.Properties());
        this.definition = definition;
    }

    public IShulkerDefinition getDefinition() {
        return definition;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        RSTooltipHandler.tooltip(stack,tooltips)
                .type(definition.type())
                .apply();
        super.appendHoverText(stack, p_41422_, tooltips, p_41424_);
    }
}
