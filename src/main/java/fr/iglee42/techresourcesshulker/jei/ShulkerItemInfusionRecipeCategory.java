package fr.iglee42.techresourcesshulker.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.recipes.ShulkerItemInfusionRecipe;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
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

    public final static ResourceLocation ARROW = new ResourceLocation(TechResourcesShulker.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerItemInfusionRecipe> RECIPE_TYPE = RecipeType.create(TechResourcesShulker.MODID, "shulker_item_infusion",
            ShulkerItemInfusionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;
    private final IDrawableAnimated arrowAnim;


    public ShulkerItemInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHULKER_INFUSER.get()));
        this.arrow = helper.createDrawable(ARROW,0,0,22,15);
        this.arrowAnim = helper.createAnimatedDrawable(arrow,22, IDrawableAnimated.StartDirection.TOP,false);
    }

    public static void renderEntity(PoseStack poseStack, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {


        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        PoseStack mobPoseStack = new PoseStack();
        mobPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        mobPoseStack.mulPose(Vector3f.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
        livingEntity.yo = (float) Math.atan(yaw / 40.0F) * 20.0F;
        float yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
        float xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        livingEntity.yBodyRot = (float) (180.0F + yaw * 20.0F);
        livingEntity.setYRot(yRot);
        livingEntity.setYRot(yRot);
        livingEntity.setXRot(xRot);
        livingEntity.yHeadRot = yRot;
        livingEntity.yHeadRotO = yRot;
        mobPoseStack.translate(0.0F, livingEntity.getY(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, mobPoseStack, bufferSource, 15728880);
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
        return new TextComponent("Shulker Item Infusion");
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
    public void draw(ShulkerItemInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        RenderSystem.setShaderTexture(0, new ResourceLocation(TechResourcesShulker.MODID,"textures/gui/arrow.png"));
        Minecraft.getInstance().screen.blit(stack,60,15,0,0,60,15,60,15);
        int y = 35;
        float scale = 27.5f, yaw = -25.0f, pitch = -29.0f;
        if (ForgeRegistries.ENTITIES.getValue(recipe.getBaseEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) ForgeRegistries.ENTITIES.getValue(recipe.getBaseEntity()).create(Minecraft.getInstance().level);
            renderEntity(stack, 27, y, scale, yaw, pitch, e);
        }


        if (ForgeRegistries.ENTITIES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) ForgeRegistries.ENTITIES.getValue(recipe.getBaseEntity()).create(Minecraft.getInstance().level);
            e.load(recipe.getResultNBT());
            renderEntity(stack, 155, y, scale, yaw, pitch, e);
        } else if (ForgeRegistries.ENTITIES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof ItemEntity){
            //TODO Render Items
        }

        Minecraft.getInstance().font.draw(stack, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Ingredients", 60, y + 10, 0);


    }

    @Override
    public List<Component> getTooltipStrings(ShulkerItemInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, 50, 45, Minecraft.getInstance().font.width("Ingredients"), Minecraft.getInstance().font.lineHeight)) {
            return Arrays.asList(new TextComponent("The order of items is not important"));
        }
        return Collections.emptyList();
    }




    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ShulkerItemInfusionRecipe.Type.ID);
    }

    @Override
    public Class<? extends ShulkerItemInfusionRecipe> getRecipeClass() {
        return ShulkerItemInfusionRecipe.class;
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerItemInfusionRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        List<Ingredient> ingredients = new ArrayList<>(Arrays.stream(recipe.getPedestalsIngredients()).filter(i->!i.isEmpty()).toList());
        for (int index = 0; index < ingredients.size(); index++){
            Ingredient i = ingredients.get(index);
            builder.addSlot(RecipeIngredientRole.INPUT,10 + index * 20,65).addIngredients(i);
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(recipe.getBaseEntity())));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(recipe.getResultEntity())));
    }


}