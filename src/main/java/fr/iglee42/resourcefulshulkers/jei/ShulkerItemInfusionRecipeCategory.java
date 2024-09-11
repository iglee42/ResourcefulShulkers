package fr.iglee42.resourcefulshulkers.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShulkerItemInfusionRecipeCategory implements IRecipeCategory<ShulkerItemInfusionRecipe> {

    public final static ResourceLocation ARROW = new ResourceLocation(ResourcefulShulkers.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerItemInfusionRecipe> RECIPE_TYPE = RecipeType.create(ResourcefulShulkers.MODID, "shulker_item_infusion",
            ShulkerItemInfusionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ShulkerItemInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHULKER_INFUSER.get()));
    }

    public static void renderEntity(GuiGraphics poseStack, int x, int y, double scale, double yaw, double pitch, Entity livingEntity) {


        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.pose().last().pose());
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        PoseStack mobPoseStack = new PoseStack();
        mobPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        if (livingEntity instanceof  LivingEntity e){
            mobPoseStack.mulPose(Axis.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
            livingEntity.yo = (float) Math.atan(yaw / 40.0F) * 20.0F;
            float yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
            float xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
            e.yBodyRot = (float) (180.0F + yaw * 20.0F);
            livingEntity.setYRot(yRot);
            livingEntity.setYRot(yRot);
            livingEntity.setXRot(xRot);
            e.yHeadRot = yRot;
            e.yHeadRotO = yRot;
        }

        mobPoseStack.translate(0.0F, livingEntity.getY(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, Minecraft.getInstance().getFrameTime(), 1.0F, mobPoseStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
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
            en = ForgeRegistries.ENTITY_TYPES.tags().getTag(ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation(recipe.getBaseEntity().substring(1)))).stream().toList().get(0).create(Minecraft.getInstance().level);
            stack.drawString(Minecraft.getInstance().font, ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + "(Hover)", 8, y + 5, 0);
        } else {
            en = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(recipe.getBaseEntity())).create(Minecraft.getInstance().level);
        }
        if (en instanceof LivingEntity e) {
            renderEntity(stack, 27, y, scale, yaw, pitch, e);
        }


        if (ForgeRegistries.ENTITY_TYPES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof Entity && !(ForgeRegistries.ENTITY_TYPES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof ItemEntity)) {
            Entity e = ForgeRegistries.ENTITY_TYPES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level);
            e.load(recipe.getResultNBT());
            renderEntity(stack, 155, y, scale, yaw, pitch, e);
        }

        stack.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Ingredients", 60, y + 10, 0,false);

        int auraWidth = Minecraft.getInstance().font.width( "Aura : " + recipe.getAuraConsummed());
        stack.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Aura : " + recipe.getAuraConsummed(), 88 -auraWidth/2, y - 29, 0,false);

    }

    @Override
    public List<Component> getTooltipStrings(ShulkerItemInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, 60, 45, Minecraft.getInstance().font.width("Ingredients"), Minecraft.getInstance().font.lineHeight)) {
            return Arrays.asList(Component.literal("The order of items is not important"));
        }
        if (MouseUtil.isMouseOver(mouseX, mouseY, 8, 40, Minecraft.getInstance().font.width("(Hover)"), Minecraft.getInstance().font.lineHeight)&& recipe.getBaseEntity().startsWith("#")) {
            return Arrays.asList(Component.literal("Accept any : " + recipe.getBaseEntity()));
        }
        return Collections.emptyList();
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerItemInfusionRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        List<Ingredient> ingredients = new ArrayList<>(Arrays.stream(recipe.getPedestalsIngredients()).filter(i->!i.isEmpty()).toList());
        for (int index = 0; index < ingredients.size(); index++){
            Ingredient i = ingredients.get(index);
            builder.addSlot(RecipeIngredientRole.INPUT,10 + index * 20,65).addIngredients(i);
        }
        if (recipe.getResultEntity().equals(new ResourceLocation("item"))){
            Entity e = ForgeRegistries.ENTITY_TYPES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level);
            e.load(recipe.getResultNBT());
            ItemEntity itemEntity = (ItemEntity) e;
            builder.addSlot(RecipeIngredientRole.OUTPUT,135, 10).addIngredients(Ingredient.of(itemEntity.getItem())).setCustomRenderer(VanillaTypes.ITEM_STACK,new BigItemstackRenderer());
        }
        if (recipe.getBaseEntity().startsWith("#")){
            for (EntityType<?> t : ForgeRegistries.ENTITY_TYPES.tags().getTag(ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation(recipe.getBaseEntity().substring(1)))).stream().toList())
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(ForgeRegistries.ENTITY_TYPES.getKey(t))));
        } else {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(recipe.getBaseEntity()))));
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(recipe.getResultEntity())));
    }


}