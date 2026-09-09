package fr.iglee42.resourcefulshulkers.client.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulkerBullet;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class ResourceShulkerBulletRenderer extends EntityRenderer<ResourceShulkerBullet> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/shulker/spark.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
    private final ShulkerBulletModel<ResourceShulkerBullet> model;

    public ResourceShulkerBulletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ShulkerBulletModel<>(ctx.bakeLayer(ModelLayers.SHULKER_BULLET));

    }

    protected int getBlockLightLevel(ResourceShulkerBullet p_115869_, BlockPos p_115870_) {
        return 15;
    }

    public void render(ResourceShulkerBullet bullet, float yaw, float pt, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float f = Mth.rotLerp(pt, bullet.yRotO, bullet.getYRot());
        float f1 = Mth.lerp(pt, bullet.xRotO, bullet.getXRot());
        float f2 = (float)bullet.tickCount + pt;
        poseStack.translate(0.0F, 0.15F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        poseStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(bullet, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer solidBuffer = bufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(poseStack, solidBuffer, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.opaque(bullet.shulkerDefinition().color()));
        poseStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer translucentBuffer = bufferSource.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(poseStack, translucentBuffer, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(38, bullet.shulkerDefinition().color()));
        poseStack.popPose();
        super.render(bullet, yaw, pt, poseStack, bufferSource, packedLight);
    }

    public ResourceLocation getTextureLocation(ResourceShulkerBullet bullet) {
        return TEXTURE_LOCATION;
    }

}