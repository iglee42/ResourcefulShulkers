package fr.iglee42.resourcefulshulkers.jei;

import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static fr.iglee42.resourcefulshulkers.jei.ShulkerEnvironmentInfusionRecipeCategory.renderEntity;

public class ShulkerItemInfusionRecipeCategory implements IRecipeCategory<ShulkerItemInfusionRecipe> {

    public final static ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerItemInfusionRecipe> RECIPE_TYPE = RecipeType.create(ResourcefulShulkers.MODID, "shulker_item_infusion",
            ShulkerItemInfusionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ShulkerItemInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHULKER_INFUSER.get()));
    }


    @Override
    public @NotNull RecipeType<ShulkerItemInfusionRecipe> getRecipeType() {
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
    public void draw(ShulkerItemInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        stack.blit(ARROW,60,15,0,0,60,15,60,15);
        int y = 35;
        float scale = 27.5f, yaw = -25.0f, pitch = -29.0f;
        Entity en;
        if (recipe.getBaseEntity().startsWith("#")){
            en = BuiltInRegistries.ENTITY_TYPE.getTag(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(recipe.getBaseEntity().substring(1)))).get().stream().toList().get(0).value().create(Minecraft.getInstance().level);
            stack.drawString(Minecraft.getInstance().font, ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + "(Hover)", 8, y + 5, 0);
        } else {
            en = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(recipe.getBaseEntity())).create(Minecraft.getInstance().level);
        }
        if (en instanceof LivingEntity e) {
            renderEntity(stack, 30, y, scale, yaw, pitch, e);
        }


        if (BuiltInRegistries.ENTITY_TYPE.get(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof Entity && !(BuiltInRegistries.ENTITY_TYPE.get(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof ItemEntity)) {
            Entity e = BuiltInRegistries.ENTITY_TYPE.get(recipe.getResultEntity()).create(Minecraft.getInstance().level);
            e.load(recipe.getResultNBT());
            renderEntity(stack, 150, y, scale, yaw, pitch, e);
        }

        stack.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Ingredients", 60, y + 10, 0,false);

        int auraWidth = Minecraft.getInstance().font.width( "Aura : " + recipe.getAuraConsummed());
        stack.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Aura : " + recipe.getAuraConsummed(), 88 -auraWidth/2, y - 29, 0,false);

    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, ShulkerItemInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, 60, 45, Minecraft.getInstance().font.width("Ingredients"), Minecraft.getInstance().font.lineHeight)) {
            tooltip.add(FormattedText.of("The order of items is not important"));
        }
        if (MouseUtil.isMouseOver(mouseX, mouseY, 8, 40, Minecraft.getInstance().font.width("(Hover)"), Minecraft.getInstance().font.lineHeight)&& recipe.getBaseEntity().startsWith("#")) {
            tooltip.add(FormattedText.of("Accept any : " + recipe.getBaseEntity()));
        }
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerItemInfusionRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        List<Ingredient> ingredients = new ArrayList<>(recipe.getPedestalsIngredients().stream().filter(i->!i.isEmpty()).toList());
        for (int index = 0; index < ingredients.size(); index++){
            Ingredient i = ingredients.get(index);
            builder.addSlot(RecipeIngredientRole.INPUT,10 + index * 20,65).addIngredients(i);
        }
        if (recipe.getResultEntity().equals(ResourceLocation.withDefaultNamespace("item"))){
            Entity e = BuiltInRegistries.ENTITY_TYPE.get(recipe.getResultEntity()).create(Minecraft.getInstance().level);
            e.load(recipe.getResultNBT());
            ItemEntity itemEntity = (ItemEntity) e;
            builder.addSlot(RecipeIngredientRole.OUTPUT,135, 10).addIngredients(Ingredient.of(itemEntity.getItem())).setCustomRenderer(VanillaTypes.ITEM_STACK,new BigItemstackRenderer());
        }
        if (recipe.getBaseEntity().startsWith("#")){
            for (EntityType<?> t : BuiltInRegistries.ENTITY_TYPE.getTag(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(recipe.getBaseEntity().substring(1)))).get().stream().map(Holder::value).toList())
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(BuiltInRegistries.ENTITY_TYPE.getKey(t))));
        } else {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse(recipe.getBaseEntity()))));
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(recipe.getResultEntity())));
    }


}