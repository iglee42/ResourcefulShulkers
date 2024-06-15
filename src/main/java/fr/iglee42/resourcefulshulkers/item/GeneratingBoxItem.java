package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModDataComponents;
import net.minecraft.core.component.DataComponentMap;
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

    public static int getDurability(ItemStack stack){
        if (stack.has(ModDataComponents.DURABILITY.get())) return stack.get(ModDataComponents.DURABILITY.get());
        stack.set(ModDataComponents.DURABILITY.get(),256);
        return 256;
    }

}
