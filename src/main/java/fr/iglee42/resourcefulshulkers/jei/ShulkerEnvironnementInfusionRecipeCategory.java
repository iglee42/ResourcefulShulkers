package fr.iglee42.resourcefulshulkers.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShulkerEnvironnementInfusionRecipeCategory implements IRecipeCategory<ShulkerRecipeEnvironnement> {
    public final static ResourceLocation UID = new ResourceLocation(ResourcefulShulkers.MODID, "shulker_environnement_infuse");
    public final static ResourceLocation ARROW = new ResourceLocation(ResourcefulShulkers.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerRecipeEnvironnement> RECIPE_TYPE = RecipeType.create(ResourcefulShulkers.MODID, "shulker_environnement_infuse",
            ShulkerRecipeEnvironnement.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ShulkerEnvironnementInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHULKER_INFUSER.get()));
    }

    public static void renderEntity(GuiGraphics poseStack, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.pose().last().pose());
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        PoseStack mobPoseStack = new PoseStack();
        mobPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        mobPoseStack.mulPose(Axis.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
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
    public @NotNull RecipeType<ShulkerRecipeEnvironnement> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Shulker Environnement Infusion");
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
    public void draw(ShulkerRecipeEnvironnement recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(ARROW,57,35,0,0,60,15,60,15);
        int y = 55;
        float scale = 22.5f, yaw = -25.0f, pitch = -29.0f;
        if (ForgeRegistries.ENTITY_TYPES.getValue(recipe.getBaseEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(guiGraphics, 20, y, scale, yaw, pitch, e);
        }

        if (ForgeRegistries.ENTITY_TYPES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(guiGraphics, 155, y, scale, yaw, pitch, e);
        }

        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Allowed Biomes", 52, y + 10, 0,false);
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.GRAY + "(Hover)", 70, y + 20, 0,false);
        if (recipe.getMinY() > -64 || recipe.getMaxY() < 320) {
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Y : " + recipe.getMinY() + "   " + recipe.getMaxY(), 54, y - 50, 0,false);
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "~", 91, y - 48, 0,false);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Y : Any", 70, y - 50, 0,false);
        }
        int auraWidth = Minecraft.getInstance().font.width( "Aura : " + recipe.getAuraConsummed());
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Aura : " + recipe.getAuraConsummed(), 88 -auraWidth/2, y - 39, 0,false);

    }
    

    @Override
    public List<Component> getTooltipStrings(ShulkerRecipeEnvironnement recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, 52, 65, Minecraft.getInstance().font.width("Allowed Biomes"), Minecraft.getInstance().font.lineHeight * 2)) {
            List<Component> biomes = new ArrayList<>();
            if (recipe.getAllowedBiomes().size() + recipe.getAllowedBiomesTags().size() == 0)
                biomes.add(Component.literal("There is no biome (This is a problem)").withStyle(ChatFormatting.RED));
            else {
                for (ResourceLocation biomeLocation : recipe.getAllowedBiomes()) {
                    biomes.add(Component.literal("- ").append(Component.translatable("biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath())));
                }
                for (ResourceLocation tagLocation : recipe.getAllowedBiomesTags()) {
                    biomes.add(Component.literal("- ").append("#" + tagLocation.toString()));
                }
            }
            return biomes;
        }
        return Collections.emptyList();
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerRecipeEnvironnement recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(recipe.getResultEntity())));
    }


}