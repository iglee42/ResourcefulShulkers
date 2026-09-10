package fr.iglee42.resourcefulshulkers.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.resourcefulshulkers.utils.codecs.XorCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

import java.util.Objects;
import java.util.function.Function;

import static fr.iglee42.resourcefulshulkers.utils.RSExtraCodecs.withAlternative;

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
                CompoundTag.CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(i->i.nbt)
        ).apply(instance,LazyIngredient::item)
    );

    private static final Codec<LazyIngredient> TAG_CODEC = RecordCodecBuilder.create(instance->
        instance.group(
                Codec.STRING.fieldOf("tag").forGetter(LazyIngredient::id)
        ).apply(instance,LazyIngredient::tag)
    );

    private static final Codec<LazyIngredient> TYPE_CODEC = new XorCodec<>(ITEM_CODEC,TAG_CODEC).xmap(
            e->e.map(Function.identity(), Function.identity()),
            i-> i.isItem() ? Either.left(i) : Either.right(i)
    );

    public static final Codec<LazyIngredient> STRING_CODEC = Codec.STRING.xmap(
            s->s.startsWith("#") ? LazyIngredient.tag(s.substring(1)) :  LazyIngredient.item(s),
        i->i.isTag() ? "#"+i.id() : i.id()
        );

    public static final Codec<LazyIngredient> CODEC = withAlternative(TYPE_CODEC, STRING_CODEC);


    private final Type type;
    private final String id;
    private final CompoundTag nbt;
    private Ingredient ingredient;

    private LazyIngredient(Type type, String id, CompoundTag nbt) {
        this.type = type;
        this.id = id;
        this.nbt = nbt;
    }

    public static LazyIngredient item(String id){
        return item(id, new CompoundTag());
    }

    public static LazyIngredient item(String id, CompoundTag nbt){
        return new LazyIngredient(Type.ITEM, id, nbt);
    }

    public static LazyIngredient tag(String id){
        return new LazyIngredient(Type.TAG, id, new CompoundTag());
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
                if (nbt == null || nbt.isEmpty())
                    this.ingredient = Ingredient.of(item);
                else {
                    ItemStack stack = new ItemStack(item);
                    stack.setTag(nbt);
                    this.ingredient = PartialNBTIngredient
                            .of(nbt, stack.getItem());
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
        return type == that.type && Objects.equals(id, that.id) && Objects.equals(nbt, that.nbt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, nbt);
    }

    private enum Type {
        ITEM,
        TAG
    }
}
