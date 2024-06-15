package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ResourcefulShulkers.MODID);

    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerRecipeEnvironnement>> ENVIRONNEMENT = RECIPES.register(ShulkerRecipeEnvironnement.Type.ID, ShulkerRecipeEnvironnement.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerItemInfusionRecipe>> INFUSION = RECIPES.register(ShulkerItemInfusionRecipe.Type.ID, ShulkerItemInfusionRecipe.Serializer::new);
}
