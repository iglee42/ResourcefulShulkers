package fr.iglee42.resourcefulshulkers.client.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.resourcefulshulkers.entity.CustomShulkerBullet;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomShulkerBulletRenderer extends EntityRenderer<CustomShulkerBullet> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/shulker/spark.png");
    private static final RenderType RENDER_TYPE;
    private final ShulkerBulletModel<CustomShulkerBullet> model;

    public CustomShulkerBulletRenderer(EntityRendererProvider.Context p_174368_) {
        super(p_174368_);
        this.model = new ShulkerBulletModel(p_174368_.bakeLayer(ModelLayers.SHULKER_BULLET));
    }

    protected int getBlockLightLevel(CustomShulkerBullet p_115869_, BlockPos p_115870_) {
        return 15;
    }

    public void render(CustomShulkerBullet p_115862_, float p_115863_, float p_115864_, PoseStack p_115865_, MultiBufferSource p_115866_, int p_115867_) {
        p_115865_.pushPose();
        float $$6 = Mth.rotLerp(p_115862_.yRotO, p_115862_.getYRot(), p_115864_);
        float $$7 = Mth.lerp(p_115864_, p_115862_.xRotO, p_115862_.getXRot());
        float $$8 = (float)p_115862_.tickCount + p_115864_;
        p_115865_.translate(0.0, 0.15000000596046448, 0.0);
        p_115865_.mulPose(Axis.YP.rotationDegrees(Mth.sin($$8 * 0.1F) * 180.0F));
        p_115865_.mulPose(Axis.XP.rotationDegrees(Mth.cos($$8 * 0.1F) * 180.0F));
        p_115865_.mulPose(Axis.ZP.rotationDegrees(Mth.sin($$8 * 0.15F) * 360.0F));
        p_115865_.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(p_115862_, 0.0F, 0.0F, 0.0F, $$6, $$7);
        VertexConsumer $$9 = p_115866_.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(p_115865_, $$9, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_115865_.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer $$10 = p_115866_.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(p_115865_, $$10, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        p_115865_.popPose();
        super.render(p_115862_, p_115863_, p_115864_, p_115865_, p_115866_, p_115867_);
    }

    public ResourceLocation getTextureLocation(CustomShulkerBullet p_115860_) {
        return TEXTURE_LOCATION;
    }

    static {
        RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
    }
}