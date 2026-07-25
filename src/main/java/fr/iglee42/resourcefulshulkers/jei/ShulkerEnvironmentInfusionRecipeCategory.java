package fr.iglee42.resourcefulshulkers.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironment;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShulkerEnvironmentInfusionRecipeCategory implements IRecipeCategory<ShulkerRecipeEnvironment> {
    public final static ResourceLocation UID =  ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "shulker_environment_infuse");
    public final static ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerRecipeEnvironment> RECIPE_TYPE = RecipeType.create(ResourcefulShulkers.MODID, "shulker_environment_infuse",
            ShulkerRecipeEnvironment.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ShulkerEnvironmentInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHULKER_INFUSER.get()));
    }

    public static void renderEntity(GuiGraphics graphics, int x, int y, double scale, double yaw, double pitch, Entity livingEntity) {

        PoseStack modelViewStack = graphics.pose();
        modelViewStack.pushPose();
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        modelViewStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        if (livingEntity instanceof  LivingEntity e){
            modelViewStack.mulPose(Axis.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
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

        modelViewStack.translate(0.0F, livingEntity.getY(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D,0.0F,1F, modelViewStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        modelViewStack.popPose();
    }

    @Override
    public @NotNull RecipeType<ShulkerRecipeEnvironment> getRecipeType() {
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
    public void draw(ShulkerRecipeEnvironment recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(ARROW,57,35,0,0,60,15,60,15);
        int y = 55;
        float scale = 22.5f, yaw = -25.0f, pitch = -29.0f;
        if (BuiltInRegistries.ENTITY_TYPE.get(recipe.getBaseEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(guiGraphics, 20, y, scale, yaw, pitch, e);
        }

        if (BuiltInRegistries.ENTITY_TYPE.get(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(guiGraphics, 155, y, scale, yaw, pitch, e);
        }

        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Allowed Biomes", 52, y + 10, 0,false);
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.GRAY + "(Hover)", 70, y + 20, 0,false);
        if (recipe.getMinY() > -64 || recipe.getMaxY() < 320) {
            guiGraphics.drawString(Minecraft.getInstance().font,ChatFormatting.BLUE + "Y : " + recipe.getMinY() + " ~ " + recipe.getMaxY(), 54, y - 50, 0, false);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Y : Any", 70, y - 50, 0,false);
        }
        int auraWidth = Minecraft.getInstance().font.width( "Aura : " + recipe.getAuraConsumed());
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.BLUE + "Aura : " + recipe.getAuraConsumed(), 88 -auraWidth/2, y - 39, 0,false);

    }


    @Override
    public List<Component> getTooltipStrings(ShulkerRecipeEnvironment recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerRecipeEnvironment recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(recipe.getResultEntity())));
        if (!recipe.getBaseEntity().equals(ResourceLocation.withDefaultNamespace("shulker")))builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(recipe.getBaseEntity())));
        else builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(RSItems.SHULKER.get()));
    }


}