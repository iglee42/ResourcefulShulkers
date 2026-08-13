package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeneratingBoxRenderer implements BlockEntityRenderer<GeneratingBoxBlockEntity> {
   private final ShulkerModel<?> model;

   public GeneratingBoxRenderer(BlockEntityRendererProvider.Context ctx) {
      this.model = new ShulkerModel<>(ctx.bakeLayer(ModelLayers.SHULKER));
   }

   public void render(GeneratingBoxBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
      Material material = new Material(Sheets.SHULKER_SHEET, entity.definition().boxTexture());
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.5F, 0.5F);
      float f = 0.9995F;
      poseStack.scale(f, f, f);
      poseStack.scale(1.0F, -1.0F, -1.0F);
      poseStack.translate(0.0F, -1.0F, 0.0F);
      ModelPart modelpart = this.model.getLid();
      modelpart.setPos(0.0F, 24.0F - entity.getAnimationProgress(partialTick) * 0.5F * 16.0F, 0.0F);
      modelpart.yRot = 270.0F * entity.getAnimationProgress(partialTick) * ((float)Math.PI / 180F);
      VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entityCutoutNoCull);
      this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
      poseStack.popPose();
   }

}