package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.jei.recipes.IJeiInputOutputRecipe;
import fr.iglee42.resourcefulshulkers.jei.recipes.InputOutputRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironment;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("igleemods","shulker");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(GeneratingBoxScreen.class,new GeneratingBoxPanelBounds());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ShulkerEnvironmentInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShulkerItemInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                BoxOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShellOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_INFUSER.get()), new RecipeType<>(ShulkerEnvironmentInfusionRecipeCategory.UID, ShulkerRecipeEnvironment.class),ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_PEDESTAL.get()), ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PURPUR_TARGET.get()), ShellOutputRecipeCategory.RECIPE_TYPE);
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(ShulkerEnvironmentInfusionRecipeCategory.RECIPE_TYPE,
                new ArrayList<>(rm.getAllRecipesFor(ShulkerRecipeEnvironment.Type.INSTANCE).stream().map(RecipeHolder::value).toList()));
        registration.addRecipes(ShulkerItemInfusionRecipeCategory.RECIPE_TYPE,
                new ArrayList<>(rm.getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).toList()));
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Arrays.asList(new ItemStack(ModItems.SHULKER_KILLER.get())));
        //ShulkersManager.TYPES.stream().filter(t->t.getItems() == Items.AIR).forEach(t-> registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,Arrays.asList(new ItemStack(ModItems.getShellById(t.id())), new ItemStack(ModBlocks.getBoxById(t.id())), new ItemStack(ModItems.getShulkerItemById(t.id())))));
        if (registration.getJeiHelpers().getRecipeType(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "box_output")).isPresent()){
            List<IJeiInputOutputRecipe> shulkersOutputRecipes = new ArrayList<>();
            ShulkersManager.TYPES.forEach(s->{
                if (s.hasItem())shulkersOutputRecipes.add(new InputOutputRecipe(Ingredient.of(ModBlocks.getBoxById(s.id())),s.item().getIngredient()));
            });
            registration.addRecipes(BoxOutputRecipeCategory.RECIPE_TYPE,shulkersOutputRecipes);
        }
        List<IJeiInputOutputRecipe> shellOutputRecipes = new ArrayList<>();
        ShulkersManager.TYPES.forEach(s->{
            if (s.hasItem())shellOutputRecipes.add(new InputOutputRecipe(Ingredient.of(ModItems.getShulkerItemById(s.id())),Ingredient.of(ModItems.getShellById(s.id()))));
        });
        registration.addRecipes(ShellOutputRecipeCategory.RECIPE_TYPE,shellOutputRecipes);

    }
}