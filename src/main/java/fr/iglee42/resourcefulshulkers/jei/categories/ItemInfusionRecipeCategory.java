package fr.iglee42.resourcefulshulkers.jei.categories;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.jei.JEIPlugin;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityIngredient;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityRenderer;
import fr.iglee42.resourcefulshulkers.jei.renderers.BigItemstackRenderer;
import fr.iglee42.resourcefulshulkers.jei.utils.CycleTicker;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.iglee42.resourcefulshulkers.jei.categories.EnvironmentInfusionRecipeCategory.renderEntity;

public class ItemInfusionRecipeCategory implements IRecipeCategory<RecipeHolder<ItemInfusionRecipe>> {

    public final static ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(RSIds.MODID, "textures/gui/arrow.png");


    public static final RecipeType<RecipeHolder<ItemInfusionRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(RSIds.id("item_infusion"));
    private final IDrawable background;
    private final IDrawable icon;
    private final CycleTicker cycler;

    public ItemInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.cycler = CycleTicker.createWithRandomOffset();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RSBlocks.SHULKER_INFUSER.get()));
    }


    @Override
    public @NotNull RecipeType<RecipeHolder<ItemInfusionRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Shulker Item Infusion");
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
    public void draw(RecipeHolder<ItemInfusionRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        ItemInfusionRecipe recipe = holder.value();
        stack.blit(ARROW, 60, 15, 0, 0, 60, 15, 60, 15);
        Font font = Minecraft.getInstance().font;
        int y = 30;
        float scale = 27.5f, yaw = -25.0f, pitch = -29.0f;
        if (Minecraft.getInstance().player.tickCount % 10 == 0)
            cycler.tick();
        Optional<Holder<EntityType<?>>> baseEntityType = cycler.getCycled(recipe.baseEntity().stream().toList());
        baseEntityType.map(Holder::value)
                .ifPresent(type->{
                    Entity baseEntity  = type.create(Minecraft.getInstance().level);
                    if (!(baseEntity instanceof LivingEntity lv)) return;
                    //renderEntity(stack,30, (int) (y +(lv.getBoundingBox().getYsize() * scale / 2 )), scale, yaw, pitch, lv);
                });

        if (recipe.resultEntity().create(Minecraft.getInstance().level) instanceof LivingEntity lv) {
            lv.load(recipe.resultNbt());
            //renderEntity(stack, 150, (int) (y +(lv.getBoundingBox().getYsize() * scale / 2)), scale, yaw, pitch, lv);
        }

        String ingredientText = "Ingredients";
        stack.drawString(font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + ingredientText, (getWidth() - font.width(ingredientText)) / 2, y + 15, 0,false);

        String auraText = "Aura : " + recipe.auraConsumed();
        stack.drawString(font, ChatFormatting.BLUE  + auraText, (getWidth() - font.width(auraText)) / 2, y - 29, 0,false);

    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipeHolder<ItemInfusionRecipe> holder, @Nonnull IFocusGroup focusGroup) {
        ItemInfusionRecipe recipe = holder.value();
        List<Ingredient> ingredients = new ArrayList<>(recipe.pedestalsIngredients());
        for (int index = 0; index < ingredients.size(); index++){
            Ingredient i = ingredients.get(index);
            builder.addSlot(RecipeIngredientRole.INPUT,10 + index * 20,65).addIngredients(i);
        }


        recipe.baseEntity().stream().map(Holder::value).forEach(baseEntityType -> {
            if (baseEntityType.equals(EntityType.SHULKER))
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemLike(RSItems.SHULKER.get());
            else
                BuiltInRegistries.ITEM.getOptional(BuiltInRegistries.ENTITY_TYPE.getKey(baseEntityType))
                        .ifPresent(item -> builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemLike(item));
        });

        builder.addInputSlot(0,10)
                .setCustomRenderer(JEIPlugin.ENTITY_TYPE, new JEIEntityRenderer(48))
                .addIngredients(JEIPlugin.ENTITY_TYPE, JEIEntityIngredient.fromHolderSet(recipe.baseEntity()));

        Entity resultEntity = recipe.resultEntity().create(Minecraft.getInstance().level);
        if (resultEntity != null) {
            resultEntity.load(recipe.resultNbt());
            if (resultEntity instanceof ItemEntity itemEntity) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 10)
                        .addItemStack(itemEntity.getItem())
                        .setCustomRenderer(VanillaTypes.ITEM_STACK, new BigItemstackRenderer());
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT,125, 10)
                        .setCustomRenderer(JEIPlugin.ENTITY_TYPE, new JEIEntityRenderer(48))
                        .addIngredient(JEIPlugin.ENTITY_TYPE, new JEIEntityIngredient(resultEntity.getType()));
            }
            if (resultEntity.getType().equals(EntityType.SHULKER))
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemLike(RSItems.SHULKER.get());
            else
                BuiltInRegistries.ITEM.getOptional(BuiltInRegistries.ENTITY_TYPE.getKey(resultEntity.getType()))
                        .ifPresent(item -> builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemLike(item));
        }
    }


}