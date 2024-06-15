/*package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("techresourcesshulkers","jei");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ShulkerEnvironnementInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                ShulkerItemInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_INFUSER.get()), ShulkerEnvironnementInfusionRecipeCategory.RECIPE_TYPE,ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHULKER_PEDESTAL.get()), ShulkerItemInfusionRecipeCategory.RECIPE_TYPE);
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
    }
}*/