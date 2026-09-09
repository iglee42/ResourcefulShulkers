package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerInfuserInput;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.EnvironmentInfusionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;


public class RSRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, RSIds.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RSIds.MODID);

    public static DeferredHolder<RecipeType<?>,RecipeType<EnvironmentInfusionRecipe>> SHULKER_ENVIRONNEMENT_INFUSE = RECIPES.register(EnvironmentInfusionRecipe.Type.ID, ()-> EnvironmentInfusionRecipe.Type.INSTANCE);
    public static DeferredHolder<RecipeType<?>,RecipeType<ItemInfusionRecipe>> SHULKER_ITEM_INFUSE = RECIPES.register(ItemInfusionRecipe.Type.ID, ()-> ItemInfusionRecipe.Type.INSTANCE);


    public static DeferredHolder<RecipeSerializer<?>,RecipeSerializer<EnvironmentInfusionRecipe>> SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER = SERIALIZER.register(EnvironmentInfusionRecipe.Type.ID, EnvironmentInfusionRecipe.Serializer::new);
    public static DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ItemInfusionRecipe>> SHULKER_ITEM_INFUSE_SERIALIZER = SERIALIZER.register(ItemInfusionRecipe.Type.ID, ItemInfusionRecipe.Serializer::new);


    public static <R extends Recipe<ShulkerInfuserInput> & ITickableRecipe<ShulkerInfuserBlockEntity>> Optional<RecipeHolder<R>> findShulkerInfuserRecipe(Level level, ShulkerInfuserBlockEntity be){
        Optional<RecipeHolder<ItemInfusionRecipe>> itemInfusionRecipe = level.getRecipeManager().getRecipeFor(ItemInfusionRecipe.Type.INSTANCE,
                new ShulkerInfuserInput(be), level);
        if (itemInfusionRecipe.isPresent()) return Optional.of((RecipeHolder<R>) itemInfusionRecipe.get());
        Optional<RecipeHolder<EnvironmentInfusionRecipe>> environmentRecipe = level.getRecipeManager()
                .getRecipeFor(EnvironmentInfusionRecipe.Type.INSTANCE, new ShulkerInfuserInput(be), level);
        return environmentRecipe.map(recipe -> (RecipeHolder<R>) recipe);
    }
}
