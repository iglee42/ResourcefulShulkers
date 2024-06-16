package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneratingBoxItem extends BlockItem {
    private final ResourceLocation id;

    public GeneratingBoxItem(Block p_40565_, Properties p_40566_,ResourceLocation id) {
        super(p_40565_, p_40566_);
        this.id = id;
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getDurability(stack) < GeneratingBoxBlockEntity.MAX_DURABILITY;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float maxDura = GeneratingBoxBlockEntity.MAX_DURABILITY;
        float f = Math.max(0.0F, getDurability(stack) / maxDura);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float)getDurability(stack) * 13.0F / (float)GeneratingBoxBlockEntity.MAX_DURABILITY);
    }

    public int getDurability(ItemStack stack){
        CompoundTag tags = stack.getOrCreateTag();
        if (tags.contains("durability")) return tags.getInt("durability");
        tags.putInt("durability",256);
        stack.setTag(tags);
        return 256;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        tooltips.add(Component.translatable("tooltip.resourcefulshulkers.type", TypesManager.getTierDisplayName(ShulkerType.getById(id).type())).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(p_41421_, p_41422_, tooltips, p_41424_);
    }

}
