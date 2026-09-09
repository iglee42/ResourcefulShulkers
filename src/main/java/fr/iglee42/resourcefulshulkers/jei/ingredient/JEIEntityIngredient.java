package fr.iglee42.resourcefulshulkers.jei.ingredient;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public record JEIEntityIngredient(EntityType<?> entityType) {

    public static List<JEIEntityIngredient> fromTag(TagKey<EntityType<?>> tagKey){
        List<JEIEntityIngredient> entities = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tagKey).forEach(entityType -> entities.add(new JEIEntityIngredient(entityType.value())));
        return entities;
    }

    public static List<JEIEntityIngredient> fromHolderSet(HolderSet<EntityType<?>> set){
        return set.stream().map(h->new JEIEntityIngredient(h.value())).toList();
    }

}
