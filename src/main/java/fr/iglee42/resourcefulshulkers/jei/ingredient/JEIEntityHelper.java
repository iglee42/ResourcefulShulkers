package fr.iglee42.resourcefulshulkers.jei.ingredient;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.resourcefulshulkers.jei.JEIPlugin;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.registries.RSTags;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class JEIEntityHelper implements IIngredientHelper<JEIEntityIngredient> {

    @Override
    public Optional<TagKey<?>> getTagKeyEquivalent(Collection<JEIEntityIngredient> ingredients) {
        Registry<EntityType<?>> registry = BuiltInRegistries.ENTITY_TYPE;
        return getTagEquivalent(ingredients, JEIEntityIngredient::entityType, registry::getTags);
    }
    @Override
    public IIngredientType<JEIEntityIngredient> getIngredientType() {
        return JEIPlugin.ENTITY_TYPE;
    }

    @Override
    public String getDisplayName(JEIEntityIngredient ingredient) {
        return ingredient.entityType().getDescription().getString();
    }

    @Override
    public String getUniqueId(JEIEntityIngredient ingredient, UidContext context) {
        return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(ingredient.entityType())).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(JEIEntityIngredient ingredient) {
        return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(ingredient.entityType()));
    }

    @Override
    public JEIEntityIngredient copyIngredient(JEIEntityIngredient ingredient) {
        return ingredient;
    }


    @Override
    public Stream<ResourceLocation> getTagStream(JEIEntityIngredient ingredient) {
        return ingredient.entityType().getTags().map(TagKey::location);
    }

    @Override
    public String getErrorInfo(@Nullable JEIEntityIngredient ingredient) {
        if (ingredient == null) {
            return "null";
        }
        ResourceLocation name = BuiltInRegistries.ENTITY_TYPE.getKey(ingredient.entityType());
        if (name == null) {
            return "unnamed";
        }
        return name.toString();
    }

    @Override
    public ItemStack getCheatItemStack(JEIEntityIngredient ingredient) {
        if (ingredient.entityType().equals(EntityType.SHULKER)) return new ItemStack(RSItems.SHULKER.get());
        if (ingredient.entityType().is(RSTags.Entities.SHULKERS)){
            Item it = BuiltInRegistries.ITEM.get(BuiltInRegistries.ENTITY_TYPE.getKey(ingredient.entityType()));
            if (it != null && it != Items.AIR) return new ItemStack(it);
        }
        SpawnEggItem item = ForgeSpawnEggItem.fromEntityType(ingredient.entityType());
        if (item == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(item);
    }
    // Copied from Jei

    public static <VALUE, STACK> Optional<TagKey<?>> getTagEquivalent(
            Collection<STACK> stacks,
            Function<STACK, VALUE> stackToValue,
            Supplier<Stream<Pair<TagKey<VALUE>, HolderSet.Named<VALUE>>>> tagSupplier
    ) {
        List<VALUE> values = stacks.stream()
                .map(stackToValue)
                .toList();

        return tagSupplier.get()
                .filter(e -> {
                    HolderSet.Named<VALUE> tag = e.getSecond();
                    return areEquivalent(tag, values);
                })
                .<TagKey<?>>map(Pair::getFirst)
                .findFirst();
    }

    private static <VALUE> boolean areEquivalent(HolderSet.Named<VALUE> tag, List<VALUE> values) {
        int count = tag.size();
        if (count != values.size()) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            VALUE tagValue = tag.get(i).value();
            VALUE value = values.get(i);
            if (!value.equals(tagValue)) {
                return false;
            }
        }
        return true;
    }
}
