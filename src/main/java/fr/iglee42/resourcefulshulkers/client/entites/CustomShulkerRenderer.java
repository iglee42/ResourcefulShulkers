package fr.iglee42.resourcefulshulkers.client.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.client.entites.layers.CustomShulkerHeadLayer;
import fr.iglee42.resourcefulshulkers.entity.shulkers.CustomShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.TypeShulker;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CustomShulkerRenderer extends MobRenderer<CustomShulker, ShulkerModel<CustomShulker>> {
    private static final ResourceLocation DEFAULT_TEXTURE_LOCATION = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION
            .texture()
            .withPath(texture -> "textures/" + texture + ".png");

    public CustomShulkerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ShulkerModel<>(ctx.bakeLayer(ModelLayers.SHULKER)), 0.0F);
        this.addLayer(new CustomShulkerHeadLayer(this));
    }

    public Vec3 getRenderOffset(CustomShulker shulker, float partialTick) {
        return shulker.getRenderPosition(partialTick).orElse(super.getRenderOffset(shulker, partialTick)).scale(shulker.getScale());
    }

    public boolean shouldRender(CustomShulker shulker, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(shulker, camera, camX, camY, camZ) || shulker.getRenderPosition(0.0F)
                .filter(
                        p_174374_ -> {
                            EntityType<?> entitytype = shulker.getType();
                            float f = entitytype.getHeight() / 2.0F;
                            float f1 = entitytype.getWidth() / 2.0F;
                            Vec3 vec3 = Vec3.atBottomCenterOf(shulker.blockPosition());
                            return camera.isVisible(
                                    new AABB(p_174374_.x, p_174374_.y + (double) f, p_174374_.z, vec3.x, vec3.y + (double) f, vec3.z)
                                            .inflate((double) f1, (double) f, (double) f1)
                            );
                        }
                )
                .isPresent();
    }

    public ResourceLocation getTextureLocation(CustomShulker shulker) {
        return getShulkerTexture(shulker);
    }

    public static ResourceLocation getShulkerTexture(CustomShulker shulker) {
        if (shulker.getCustomName() != null && shulker.getCustomName().getString().equalsIgnoreCase("MLDEG")) return RSIds.id("textures/entity/shulker/types/mldeg.png");
        if (shulker instanceof ResourceShulker rs) return rs.definition().texture().withPath(path->"textures/"+path+".png");
        if (shulker instanceof TypeShulker ts) return ts.definition().texture().withPath(path->"textures/"+path+".png");
        return DEFAULT_TEXTURE_LOCATION;
    }

    protected void setupRotations(CustomShulker shulker, PoseStack poseStack, float p_115909_, float yBodyRot, float scale) {
        super.setupRotations(shulker, poseStack, p_115909_, yBodyRot + 180.0F, scale);
        poseStack.translate(0.0D, 0.5D, 0.0D);
        poseStack.mulPose(shulker.getAttachFace().getOpposite().getRotation());
        poseStack.translate(0.0D, -0.5D, 0.0D);
    }
}
