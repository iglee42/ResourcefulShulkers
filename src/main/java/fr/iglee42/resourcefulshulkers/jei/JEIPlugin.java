package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.jei.recipes.InputOutputRecipe;
import fr.iglee42.resourcefulshulkers.jei.recipes.IJeiInputOutputRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("igleemods","shulker");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ShulkerEnvironnementInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShulkerItemInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                BoxOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShellOutputRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_INFUSER.get()), new RecipeType<>(ShulkerEnvironnementInfusionRecipeCategory.UID, ShulkerRecipeEnvironnement.class),ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_PEDESTAL.get()), ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PURPUR_TARGET.get()), ShellOutputRecipeCategory.RECIPE_TYPE);
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(ShulkerEnvironnementInfusionRecipeCategory.RECIPE_TYPE,
                new ArrayList<>(rm.getAllRecipesFor(ShulkerRecipeEnvironnement.Type.INSTANCE)));
        registration.addRecipes(ShulkerItemInfusionRecipeCategory.RECIPE_TYPE,
                new ArrayList<>(rm.getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE)));
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Arrays.asList(new ItemStack(ModItems.SHULKER_KILLER.get())));
        ShulkersManager.TYPES.stream().filter(t->t.getItem() == Items.AIR).forEach(t-> registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,Arrays.asList(new ItemStack(ModItems.getShellById(t.id())), new ItemStack(ModBlocks.getBoxById(t.id())), new ItemStack(ModItems.getShulkerItemById(t.id())))));
        if (registration.getJeiHelpers().getRecipeType(new ResourceLocation(ResourcefulShulkers.MODID, "box_output")).isPresent()){
            List<IJeiInputOutputRecipe> shulkersOutputRecipes = new ArrayList<>();
            ShulkersManager.TYPES.forEach(s->{
                if (s.getItem() != Items.AIR)shulkersOutputRecipes.add(new InputOutputRecipe(Ingredient.of(ModBlocks.getBoxById(s.id())),Ingredient.of(s.getItem())));
            });
            registration.addRecipes(BoxOutputRecipeCategory.RECIPE_TYPE,shulkersOutputRecipes);
        }
        List<IJeiInputOutputRecipe> shellOutputRecipes = new ArrayList<>();
        ShulkersManager.TYPES.forEach(s->{
            if (s.getItem() != Items.AIR)shellOutputRecipes.add(new InputOutputRecipe(Ingredient.of(ModItems.getShulkerItemById(s.id())),Ingredient.of(ModItems.getShellById(s.id()))));
        });
        registration.addRecipes(ShellOutputRecipeCategory.RECIPE_TYPE,shellOutputRecipes);

    }
}