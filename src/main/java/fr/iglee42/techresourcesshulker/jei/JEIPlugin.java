package fr.iglee42.techresourcesshulker.jei;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.recipes.ShulkerRecipeEnvironnement;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("techresourcesmods","shulker");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ShulkerEnvironnementInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModContent.SHULKER_INFUSER_ITEM.get()), new RecipeType<>(ShulkerEnvironnementInfusionRecipeCategory.UID, ShulkerRecipeEnvironnement.class));
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration){
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(ShulkerEnvironnementInfusionRecipeCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ShulkerRecipeEnvironnement.Type.INSTANCE)
                        .stream()
                        .map(r -> (ShulkerRecipeEnvironnement) r)
                        .collect(Collectors.toList()));

    }
}