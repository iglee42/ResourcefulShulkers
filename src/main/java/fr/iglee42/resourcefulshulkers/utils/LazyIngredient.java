package fr.iglee42.resourcefulshulkers.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Objects;
import java.util.function.Function;

public class LazyIngredient {

    public static final LazyIngredient EMPTY = new LazyIngredient(null,null,null){
        @Override
        public Ingredient getIngredient() {
            return Ingredient.EMPTY;
        }
    };

    private static final Codec<LazyIngredient> ITEM_CODEC = RecordCodecBuilder.create(instance->
        instance.group(
                Codec.STRING.fieldOf("item").forGetter(LazyIngredient::id),
                DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).forGetter(i->i.components)
        ).apply(instance,LazyIngredient::item)
    );

    private static final Codec<LazyIngredient> TAG_CODEC = RecordCodecBuilder.create(instance->
        instance.group(
                Codec.STRING.fieldOf("tag").forGetter(LazyIngredient::id)
        ).apply(instance,LazyIngredient::tag)
    );

    private static final Codec<LazyIngredient> TYPE_CODEC = Codec.xor(ITEM_CODEC,TAG_CODEC).xmap(
            e->e.map(Function.identity(), Function.identity()),
            i-> i.isItem() ? Either.left(i) : Either.right(i)
    );

    public static final Codec<LazyIngredient> STRING_CODEC = Codec.STRING.xmap(
            s->s.startsWith("#") ? LazyIngredient.tag(s.substring(1)) :  LazyIngredient.item(s),
        i->i.isTag() ? "#"+i.id() : i.id()
        );

    public static final Codec<LazyIngredient> CODEC = Codec.withAlternative(TYPE_CODEC, STRING_CODEC);


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
        if (this.ingredient != null) return this.ingredient;
        if (isTag()) {
            var tag = ItemTags.create(ResourceLocation.parse(id));
            this.ingredient = Ingredient.of(tag);
        } else if (isItem()){
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
            if (item != Items.AIR){
                if (components == null || components.isEmpty())
                    this.ingredient = Ingredient.of(item);
                else {
                    ItemStack stack = new ItemStack(item);
                    stack.applyComponents(components);
                    this.ingredient = DataComponentIngredient.of(false,stack);
                }
            }
        }
        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }

    public boolean isConsideredEmptyWithoutTags(){
        if (this == EMPTY) return true;
        if (isTag()) return false;
        return !BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LazyIngredient that)) return false;
        return type == that.type && Objects.equals(id, that.id) && Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, components);
    }

    private enum Type {
        ITEM,
        TAG
    }
}
