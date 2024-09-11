package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.jei.recipes.IJeiInputOutputRecipe;
import fr.iglee42.resourcefulshulkers.jei.recipes.InputOutputRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class ShellOutputRecipeCategory implements IRecipeCategory<IJeiInputOutputRecipe> {

    public static final RecipeType<IJeiInputOutputRecipe> RECIPE_TYPE = RecipeType.create(ResourcefulShulkers.MODID, "shell_output",
            InputOutputRecipe.class);

    public final static ResourceLocation ARROW = new ResourceLocation(ResourcefulShulkers.MODID, "textures/gui/arrow.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawable slotBackground;

    public ShellOutputRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(160,20);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PURPUR_TARGET.get()));
        this.slotBackground = helper.getSlotDrawable();
    }


    @Override
    public RecipeType<IJeiInputOutputRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Shell Output");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(IJeiInputOutputRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(ARROW,52,2,5,0,55,15,60,15);
        float scale = 16f, yaw = -25.0f, pitch = -29.0f;
        if (ForgeRegistries.ENTITY_TYPES.getValue(ForgeRegistries.ITEMS.getKey(recipe.getInput().getItems()[0].getItem())).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            ShulkerEnvironnementInfusionRecipeCategory.renderEntity(guiGraphics, 38, 17, scale, yaw, pitch, e);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiInputOutputRecipe recipe, IFocusGroup iFocusGroup) {
        int x = 48;

        IIngredientAcceptor<?> inputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);
        IRecipeSlotBuilder catalystSlotBuilder = builder.addSlot(RecipeIngredientRole.CATALYST, x + 24, 1);
        IRecipeSlotBuilder outputSlotBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, x + 64, 1)
                .setBackground(slotBackground, -1, -1);
        inputSlotBuilder.addIngredients(recipe.getInput());
        catalystSlotBuilder.addIngredients(Ingredient.of(ModBlocks.PURPUR_TARGET.get()));
        outputSlotBuilder.addIngredients(recipe.getOutput());
    }


}