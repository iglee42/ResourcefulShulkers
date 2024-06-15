package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ResourcefulShulkers.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ResourcefulShulkers.MODID);

    public static final DeferredHolder<RecipeType<?>,RecipeType<ShulkerRecipeEnvironnement>> ENVIRONNEMENT = RECIPES.register(ShulkerRecipeEnvironnement.Type.ID, ()->ShulkerRecipeEnvironnement.Type.INSTANCE);
    public static final DeferredHolder<RecipeType<?>,RecipeType<ShulkerItemInfusionRecipe>> INFUSION = RECIPES.register(ShulkerItemInfusionRecipe.Type.ID, ()->ShulkerItemInfusionRecipe.Type.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerRecipeEnvironnement>> ENVIRONNEMENT_SERIALIZER = SERIALIZER.register(ShulkerRecipeEnvironnement.Type.ID, ShulkerRecipeEnvironnement.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerItemInfusionRecipe>> INFUSION_SERIALIZER = SERIALIZER.register(ShulkerItemInfusionRecipe.Type.ID, ShulkerItemInfusionRecipe.Serializer::new);

}
