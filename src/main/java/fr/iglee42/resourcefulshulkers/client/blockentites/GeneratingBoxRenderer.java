package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.utils.RSColors;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeneratingBoxRenderer implements BlockEntityRenderer<GeneratingBoxBlockEntity> {
   private final ShulkerModel<?> model;

   public GeneratingBoxRenderer(BlockEntityRendererProvider.Context ctx) {
      this.model = new ShulkerModel<>(ctx.bakeLayer(ModelLayers.SHULKER));
   }

   public void render(GeneratingBoxBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
      Material material = new Material(Sheets.SHULKER_SHEET, entity.definition().boxTexture());
      boolean isDyeShulker = entity.definition().id().equals(RSIds.id("dye"));
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
      int argb = isDyeShulker ? RSColors.getRGBColor().toARGB() : 0xffffffff;
      float alpha = (argb >> 24 & 0xFF) / 255.0F;
      float red = (argb >> 16 & 0xFF) / 255.0F;
      float green = (argb >> 8 & 0xFF) / 255.0F;
      float blue = (argb & 0xFF) / 255.0F;
      this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay, red, green, blue, alpha);
      poseStack.popPose();
   }

}