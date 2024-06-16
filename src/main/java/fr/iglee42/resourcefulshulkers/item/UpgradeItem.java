package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeItem extends Item {
    private final Upgrade upgrade;

    public UpgradeItem(Upgrade upgrade) {
        super(new Item.Properties());
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        if (Screen.hasShiftDown())tooltips.add(upgrade.getDescription());
        else tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift"));
        super.appendHoverText(p_41421_, p_41422_, tooltips, p_41424_);
    }
}
