package fr.iglee42.resourcefulshulkers.jei.categories;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BoxOutputRecipeCategory implements IRecipeCategory<IShulkerDefinition> {

    public static final RecipeType<IShulkerDefinition> RECIPE_TYPE = RecipeType.create(RSIds.MODID, "box_output",
            IShulkerDefinition.class);

    public final static ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(RSIds.MODID, "textures/gui/arrow.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawable slotBackground;

    public BoxOutputRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(97,18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RSBlocks.getAllGeneratingBox()[0]));
        this.slotBackground = helper.getSlotDrawable();
    }


    @Override
    public RecipeType<IShulkerDefinition> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Generating Box Output");
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
    public void draw(IShulkerDefinition recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(ARROW, 20,2,5,0,55,15,60,15);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IShulkerDefinition shulker, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 1)
                .setBackground(slotBackground, -1 ,-1)
                .addItemStack(shulker.registration().generatingBox().get().asItem().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 1)
                .setBackground(slotBackground, -1 ,-1)
                .addIngredients(shulker.item().getIngredient());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemLike(shulker.registration().shulkerItem().get());
    }


}