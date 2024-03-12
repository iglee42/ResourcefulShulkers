package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GeneratingBoxItem extends BlockItem {
    public GeneratingBoxItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
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

}
