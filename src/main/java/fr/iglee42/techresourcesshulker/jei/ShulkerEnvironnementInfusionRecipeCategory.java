package fr.iglee42.techresourcesshulker.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.recipes.ShulkerRecipeEnvironnement;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
    public final static ResourceLocation UID = new ResourceLocation(TechResourcesShulker.MODID, "shulker_environnement_infuse");
    public final static ResourceLocation ARROW = new ResourceLocation(TechResourcesShulker.MODID, "textures/gui/arrow.png");


    public static final RecipeType<ShulkerRecipeEnvironnement> RECIPE_TYPE = RecipeType.create(TechResourcesShulker.MODID, "shulker_environnement_infuse",
            ShulkerRecipeEnvironnement.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;
    private final IDrawableAnimated arrowAnim;


    public ShulkerEnvironnementInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.SHULKER_INFUSER_ITEM.get()));
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
    public @NotNull RecipeType<ShulkerRecipeEnvironnement> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return new TextComponent("Shulker Environnement Infusion");
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
    public void draw(ShulkerRecipeEnvironnement recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        RenderSystem.setShaderTexture(0, new ResourceLocation(TechResourcesShulker.MODID,"textures/gui/arrow.png"));
        Minecraft.getInstance().screen.blit(stack,52,35,0,0,60,15,60,15);
        int y = 55;
        float scale = 22.5f, yaw = -25.0f, pitch = -29.0f;
        if (ForgeRegistries.ENTITIES.getValue(recipe.getBaseEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(stack, 20, y, scale, yaw, pitch, e);
        }

        if (ForgeRegistries.ENTITIES.getValue(recipe.getResultEntity()).create(Minecraft.getInstance().level) instanceof LivingEntity e) {
            renderEntity(stack, 155, y, scale, yaw, pitch, e);
        }

        Minecraft.getInstance().font.draw(stack, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Allowed Biomes", 52, y + 10, 0);
        Minecraft.getInstance().font.draw(stack, ChatFormatting.GRAY + "(Hover)", 70, y + 20, 0);
        Minecraft.getInstance().font.draw(stack, ChatFormatting.BLUE +  "Y : "+recipe.getMinY() + "   " + recipe.getMaxY(), 54, y - 50, 0);
        Minecraft.getInstance().font.draw(stack, ChatFormatting.BLUE +  "~", 91, y - 48, 0);
    }

    @Override
    public List<Component> getTooltipStrings(ShulkerRecipeEnvironnement recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, 52, 65, Minecraft.getInstance().font.width("Allowed Biomes"), Minecraft.getInstance().font.lineHeight * 2)) {
            List<Component> biomes = new ArrayList<>();
            if (recipe.getAllowedBiomes().size() + recipe.getAllowedBiomesTags().size() == 0)
                biomes.add(new TextComponent("There is no biome (This is a problem)").withStyle(ChatFormatting.RED));
            else {
                for (ResourceLocation biomeLocation : recipe.getAllowedBiomes()) {
                    biomes.add(new TextComponent("- ").append(new TranslatableComponent("biome." + biomeLocation.getNamespace() + "." + biomeLocation.getPath())));
                }
                for (ResourceLocation tagLocation : recipe.getAllowedBiomesTags()) {
                    biomes.add(new TextComponent("- ").append("#" + tagLocation.toString()));
                }
            }
            return biomes;
        }
        return Collections.emptyList();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ShulkerRecipeEnvironnement> getRecipeClass() {
        return ShulkerRecipeEnvironnement.class;
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ShulkerRecipeEnvironnement recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(ForgeRegistries.ITEMS.getValue(recipe.getResultEntity())));
    }


}