package fr.iglee42.resourcefulshulkers.utils;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class LazyIngredient {

    public static final LazyIngredient EMPTY = new LazyIngredient(null,null,null){
        @Override
        public Ingredient getIngredient() {
            return Ingredient.EMPTY;
        }
    };

    private final Type type;
    private final String id;
    private final DataComponentMap components;
    private Ingredient ingredient;

    private LazyIngredient(Type type, String id, DataComponentMap components) {
        this.type = type;
        this.id = id;
        this.components = components;
    }

    public static LazyIngredient item(String id){
        return item(id, DataComponentMap.EMPTY);
    }

    public static LazyIngredient item(String id, DataComponentMap nbt){
        return new LazyIngredient(Type.ITEM, id, nbt);
    }

    public static LazyIngredient tag(String id){
        return new LazyIngredient(Type.TAG, id, DataComponentMap.EMPTY);
    }


    public String id() {
        return id;
    }

    public boolean isItem() {
        return type == Type.ITEM;
    }

    public boolean isTag() {
        return type == Type.TAG;
    }

    public Ingredient getIngredient(){
        if (isTag()) {
            var tag = ItemTags.create(ResourceLocation.parse(id));
            return Ingredient.of(tag);
        } else if (isItem()){
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
            if (item != Items.AIR){
                if (components == null || components.isEmpty())
                    this.ingredient = Ingredient.of(item);
                else {
                    ItemStack stack = new ItemStack(item);
                    stack.applyComponents(components);
                    return DataComponentIngredient.of(false,stack);
                }
            }
        }
        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }

    private enum Type {
        ITEM,
        TAG
    }
}
