package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ShellItem extends Item {
    private final ResourceLocation id;

    public ShellItem(ResourceLocation id) {
        super(new Item.Properties());
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        tooltips.add(Component.translatable("tooltip.resourcefulshulkers.type", TypesManager.getTierDisplayName(ShulkerType.getById(getId()).type())).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(p_41421_, p_41422_, tooltips, p_41424_);
    }
}
