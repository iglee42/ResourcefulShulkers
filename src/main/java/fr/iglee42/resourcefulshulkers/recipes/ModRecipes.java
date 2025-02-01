package fr.iglee42.resourcefulshulkers.recipes;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ResourcefulShulkers.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ResourcefulShulkers.MODID);

    public static DeferredHolder<RecipeType<?>,RecipeType<ShulkerRecipeEnvironment>> SHULKER_ENVIRONNEMENT_INFUSE = RECIPES.register(ShulkerRecipeEnvironment.Type.ID, ()-> ShulkerRecipeEnvironment.Type.INSTANCE);
    public static DeferredHolder<RecipeType<?>,RecipeType<ShulkerItemInfusionRecipe>> SHULKER_ITEM_INFUSE = RECIPES.register(ShulkerItemInfusionRecipe.Type.ID, ()->ShulkerItemInfusionRecipe.Type.INSTANCE);


    public static DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerRecipeEnvironment>> SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER = SERIALIZER.register(ShulkerRecipeEnvironment.Type.ID, ShulkerRecipeEnvironment.Serializer::new);
    public static DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ShulkerItemInfusionRecipe>> SHULKER_ITEM_INFUSE_SERIALIZER = SERIALIZER.register(ShulkerItemInfusionRecipe.Type.ID, ShulkerItemInfusionRecipe.Serializer::new);


}
