package fr.iglee42.techresourcesshulker.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.client.entites.CustomShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeneratingBoxRenderer implements BlockEntityRenderer<GeneratingBoxBlockEntity> {
   private final CustomShulkerModel<?> model;

   public GeneratingBoxRenderer(BlockEntityRendererProvider.Context p_173626_) {
      this.model = new CustomShulkerModel(p_173626_.bakeLayer(ModelLayers.SHULKER));
   }

   public void render(GeneratingBoxBlockEntity entity, float p_112479_, PoseStack p_112480_, MultiBufferSource p_112481_, int p_112482_, int p_112483_) {
      Direction direction = Direction.UP;


      Material material = entity.getResourceGenerated().getMaterial();

      if (material.sprite() instanceof MissingTextureAtlasSprite){
         material = Sheets.SHULKER_TEXTURE_LOCATION.get(entity.getResourceGenerated().getColor().getId());
      }
      p_112480_.pushPose();
      p_112480_.translate(0.5D, 0.5D, 0.5D);
      float f = 0.9995F;
      p_112480_.scale(f, f, f);
      p_112480_.mulPose(direction.getRotation());
      p_112480_.scale(1.0F, -1.0F, -1.0F);
      p_112480_.translate(0.0D, -1.0D, 0.0D);
      ModelPart modelpart = this.model.getLid();
      modelpart.setPos(0.0F, 24.0F - entity.getProgress(p_112479_) * 0.5F * 16.0F, 0.0F);
      modelpart.yRot = 270.0F * entity.getProgress(p_112479_) * ((float)Math.PI / 180F);
      VertexConsumer vertexconsumer = material.buffer(p_112481_, RenderType::entityCutoutNoCull);
      this.model.renderToBuffer(p_112480_, vertexconsumer, p_112482_, p_112483_, 1.0F, 1.0F, 1.0F, 1.0F);
      p_112480_.popPose();
   }
}