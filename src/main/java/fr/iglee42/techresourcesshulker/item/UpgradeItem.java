package fr.iglee42.techresourcesshulker.item;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.utils.Upgrade;
import net.minecraft.ChatFormatting;
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
        super(new Item.Properties().tab(TechResourcesShulker.GROUP));
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(upgrade.getDescription().withStyle(ChatFormatting.GREEN));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
