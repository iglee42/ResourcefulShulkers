package fr.iglee42.resourcefulshulkers.registries;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RSNBT {

    public static final String DURABILITY = "durability";
    public static final String ITEM_INDEX = "item_index";


    public static int getOrDefault(ItemStack stack, String key, int defaultValue){
        if (!stack.hasTag()) return defaultValue;
        if (!stack.getTag().contains(key)) return defaultValue;
        return stack.getTag().getInt(key);
    }

    public static int getOrDefaultSet(ItemStack stack, String key, int defaultValue){
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(key)){
            tag.putInt(key, defaultValue);
            return defaultValue;
        }
        return tag.getInt(key);
    }

    public static void set(ItemStack stack, String key, int value){
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(key, value);
    }
}
