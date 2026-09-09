package fr.iglee42.resourcefulshulkers.jei.categories;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.igleelib.api.utils.MouseUtil;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.jei.JEIPlugin;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityIngredient;
import fr.iglee42.resourcefulshulkers.jei.ingredient.JEIEntityRenderer;
import fr.iglee42.resourcefulshulkers.jei.renderers.BigItemstackRenderer;
import fr.iglee42.resourcefulshulkers.jei.utils.CycleTicker;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.recipes.EnvironmentInfusionRecipe;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
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
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnvironmentInfusionRecipeCategory implements IRecipeCategory<RecipeHolder<EnvironmentInfusionRecipe>> {
    public final static ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(RSIds.MODID, "textures/gui/arrow.png");


    public static final RecipeType<RecipeHolder<EnvironmentInfusionRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(RSIds.id("environment_infusion"));
    private final IDrawable background;
    private final IDrawable icon;
    private final CycleTicker cycler;


    public EnvironmentInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 92);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RSBlocks.SHULKER_INFUSER.get()));
        this.cycler = CycleTicker.createWithRandomOffset();
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
    public @NotNull RecipeType<RecipeHolder<EnvironmentInfusionRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Shulker Environment Infusion");
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
    public void draw(RecipeHolder<EnvironmentInfusionRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        EnvironmentInfusionRecipe recipe = holder.value();
        guiGraphics.blit(ARROW,57,35,0,0,60,15,60,15);
        int y = 55;
        if (Minecraft.getInstance().player.tickCount % 10 == 0)
            cycler.tick();
        Font font = Minecraft.getInstance().font;
        float scale = 22.5f, yaw = -25.0f, pitch = -29.0f;

        Optional<Holder<EntityType<?>>> baseEntityType = cycler.getCycled(recipe.baseEntity().stream().toList());
        baseEntityType.map(Holder::value)
                .ifPresent(type->{
                    Entity baseEntity  = type.create(Minecraft.getInstance().level);
                    if (!(baseEntity instanceof LivingEntity lv)) return;
                    //renderEntity(guiGraphics,30, (int) (y +(lv.getBoundingBox().getYsize() * scale / 2 )), scale, yaw, pitch, lv);
                });


        if (recipe.resultEntity().create(Minecraft.getInstance().level) instanceof LivingEntity lv) {
            lv.load(recipe.resultNbt());
            //renderEntity(guiGraphics, 150, (int) (y +(lv.getBoundingBox().getYsize() * scale / 2 )), scale, yaw, pitch, lv);
        }

        String biomeText = "Allowed Biomes";
        int biomeWidth = font.width(biomeText);
        guiGraphics.drawString(font, ChatFormatting.BLUE + "" + ChatFormatting.UNDERLINE + "Allowed Biomes", (getWidth() - biomeWidth) / 2 , y + 10, 0,false);
        guiGraphics.drawString(font, ChatFormatting.GRAY + "(Hover)",  (getWidth() - font.width("(Hover)")) / 2, y + 20, 0,false);
        String yText = "";
        if (recipe.minY() > -128 && (recipe.maxY() < 320 && recipe.maxY() != -128)){
            yText = "Y : " + recipe.minY() + " ~ " + recipe.maxY();
        } else if (recipe.minY() > -128 && recipe.maxY() == -128){
            yText = "Y : > " + recipe.minY();
        } else if (recipe.minY() == -128 && (recipe.maxY() < 320 && recipe.maxY() != -128)){
            yText = "Y : < " + recipe.maxY();
        } else {
            yText = "Y : Any";
        }
        guiGraphics.drawString(font, ChatFormatting.BLUE + yText, (getWidth() - font.width(yText)) / 2 , y - 50, 0,false);
        String auraText = "Aura : " + recipe.auraConsumed();
        guiGraphics.drawString(font, ChatFormatting.BLUE + auraText, (getWidth() - font.width(auraText)) / 2, y - 39, 0,false);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipeHolder<EnvironmentInfusionRecipe> holder, @Nonnull IFocusGroup focusGroup) {
        EnvironmentInfusionRecipe recipe = holder.value();
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
        if (resultEntity != null){
            resultEntity.load(recipe.resultNbt());
            if (resultEntity instanceof ItemEntity itemEntity){
                builder.addSlot(RecipeIngredientRole.OUTPUT,135, 10).addItemStack(itemEntity.getItem()).setCustomRenderer(VanillaTypes.ITEM_STACK,new BigItemstackRenderer());
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT,125, 10)
                        .setCustomRenderer(JEIPlugin.ENTITY_TYPE, new JEIEntityRenderer(48))
                        .addIngredient(JEIPlugin.ENTITY_TYPE, new JEIEntityIngredient(resultEntity.getType()));
            }
            if (resultEntity.getType().equals(EntityType.SHULKER))
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemLike(RSItems.SHULKER.get());
            else
                BuiltInRegistries.ITEM.getOptional(BuiltInRegistries.ENTITY_TYPE.getKey(resultEntity.getType()))
                        .ifPresent(item-> builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemLike(item));
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<EnvironmentInfusionRecipe> holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        EnvironmentInfusionRecipe recipe = holder.value();
        Font font = Minecraft.getInstance().font;
        int biomeWidth = font.width("Allowed Biomes");
        if (MouseUtil.isMouseOver(mouseX, mouseY, (getWidth() - biomeWidth) / 2, 65, biomeWidth , font.lineHeight * 2)) {
            if (recipe.allowedBiomes().size() == 0)
                tooltip.add(Component.literal("There is no biome (This is a problem)").withStyle(ChatFormatting.RED));
            else {
                List<Component> biomesComps = new ArrayList<>();
                for (Holder<Biome> biome : recipe.allowedBiomes()) {
                    if (biomesComps.size() == 10){
                        biomesComps.add(Component.literal("+ " + Math.min(recipe.allowedBiomes().size() - 10, 99)).withStyle(ChatFormatting.GRAY));
                        break;
                    }
                    biomesComps.add(Component.literal("- ").append(Component.translatable(Util.makeDescriptionId("biome", biome.getKey().location()))));
                }
                tooltip.addAll(biomesComps);
            }
        }
    }
}