package fr.iglee42.resourcefulshulkers.jei;

import com.direwolf20.justdirethings.client.jei.JEIIntegration;
import fr.iglee42.resourcefulshulkers.client.screen.EndCityScreen;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.jei.categories.*;
import fr.iglee42.resourcefulshulkers.jei.handlers.ChooseItemPanelBoundsHandler;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityHelper;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityIngredient;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityRenderer;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.recipes.EnvironmentInfusionRecipe;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final IIngredientType<JEIEntityIngredient> ENTITY_TYPE = () -> JEIEntityIngredient.class;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("igleemods","shulker");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(GeneratingBoxScreen.class, new ChooseItemPanelBoundsHandler<>());
        registration.addGuiContainerHandler(EndCityScreen.class, new ChooseItemPanelBoundsHandler<>());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                EnvironmentInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ItemInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                BoxOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShellOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RSBlocks.SHULKER_INFUSER.get()), EnvironmentInfusionRecipeCategory.RECIPE_TYPE, ItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RSBlocks.SHULKER_PEDESTAL.get()), ItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RSBlocks.PURPUR_TARGET.get()), ShellOutputRecipeCategory.RECIPE_TYPE);
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        ShulkersManager.forEachShulker(shulker->{
            if (shulker.definition().hasItem()){
                registration.addRecipes(BoxOutputRecipeCategory.RECIPE_TYPE, List.of(shulker.definition()));
                registration.addRecipes(ShellOutputRecipeCategory.RECIPE_TYPE, List.of(shulker.definition()));
            }
        });
        registration.addRecipes(EnvironmentInfusionRecipeCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(EnvironmentInfusionRecipe.Type.INSTANCE)
                        .stream()
                        .filter(r->r.value()
                                .getIngredients()
                                .stream()
                                .noneMatch(Ingredient::hasNoItems))
                        .toList());
        registration.addRecipes(ItemInfusionRecipeCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ItemInfusionRecipe.Type.INSTANCE)
                        .stream()
                        .filter(r->r.value()
                                .pedestalsIngredients()
                                .stream()
                                .noneMatch(Ingredient::hasNoItems))
                        .toList());
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(ENTITY_TYPE, List.of(), new JEIEntityHelper(), new JEIEntityRenderer(16), BuiltInRegistries.ENTITY_TYPE.byNameCodec()
                .xmap(JEIEntityIngredient::new, JEIEntityIngredient::entityType));
    }
}