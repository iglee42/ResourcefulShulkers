package fr.iglee42.resourcefulshulkers.jei.ingredient;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class JEIEntityRenderer implements IIngredientRenderer<JEIEntityIngredient> {

    private final int size;

    public JEIEntityRenderer(int size) {
        this.size = size;
    }

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    public static void renderEntity(GuiGraphics graphics, EntityType<?> type, int size) {
        Entity entity = Minecraft.getInstance().level == null ? null : type.create(Minecraft.getInstance().level);
        if (entity instanceof LivingEntity living) {
            // scale down large mobs, but don't scale up small ones
            int scale = size / 2;
            float height = entity.getBbHeight();
            float width = entity.getBbWidth();
            if (height > 2.25F || width > 2.25F) {
                scale = (int) (20 / Math.max(height, width));
            }
            // catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
            try {
                renderTheEntity(graphics, size / 2, size - 2, scale, living);
            } catch (Exception e) {
            }
        }
    }

    //[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
    private static void renderTheEntity(GuiGraphics graphics, int x, int y, int scale, LivingEntity entity) {
        PoseStack posestack = graphics.pose();
        Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
        quaternion.mul(quaternion1);
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 0.0F;
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        posestack.pushPose();
        posestack.translate(x, y, 50.0D);
        applyAdditionalTransforms(entity.getType(), posestack);
        posestack.scale((float) scale, (float) scale, (float) -scale);
        posestack.mulPose(quaternion);
        posestack.mulPose(Axis.XN.rotationDegrees(35.0F));
        posestack.mulPose(Axis.YN.rotationDegrees(145.0F));
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        dispatcher.overrideCameraOrientation(quaternion1);
        boolean hitboxes = dispatcher.shouldRenderHitBoxes();
        dispatcher.setRenderShadow(false);
        dispatcher.setRenderHitBoxes(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, posestack, graphics.bufferSource(), 15728880));
        graphics.flush();
        dispatcher.setRenderShadow(true);
        dispatcher.setRenderHitBoxes(hitboxes);
        posestack.popPose();
        Lighting.setupFor3DItems();
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
    }

    //certain entities are a pain. This exists to fix vanilla cases.
    private static void applyAdditionalTransforms(EntityType<?> entity, PoseStack stack) {
        if (entity == EntityType.GHAST) {
            stack.translate(0.0D, -12.5D, 0.0D);
            stack.scale(0.5F, 0.5F, 0.5F);
        }
        if (entity == EntityType.ENDER_DRAGON) stack.translate(0.0D, -4.0D, 0.0D);
        if (entity == EntityType.WITHER) stack.translate(0.0D, 5.0D, 0.0D);
        if (entity == EntityType.SQUID || entity == EntityType.GLOW_SQUID) stack.translate(0.0D, -3.0D, 0.0D);
        if (entity == EntityType.ELDER_GUARDIAN) stack.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    public void render(GuiGraphics guiGraphics, JEIEntityIngredient ingredient) {
        renderEntity(guiGraphics, ingredient.entityType(), size);
    }

    @Override
    public List<Component> getTooltip(JEIEntityIngredient ingredient, TooltipFlag tooltipFlag) {
        return getMobTooltip(ingredient.entityType());
    }


    public static List<Component> getMobTooltip(EntityType<?> type) {
        List<Component> components = new ArrayList<>();
        components.add(type.getDescription());
        if (Minecraft.getInstance().options.advancedItemTooltips) {
            components.add(Component.literal(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        return components;
    }
}
