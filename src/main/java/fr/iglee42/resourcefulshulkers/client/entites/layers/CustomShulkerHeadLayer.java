package fr.iglee42.resourcefulshulkers.client.entites.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerRenderer;
import fr.iglee42.resourcefulshulkers.entity.shulkers.CustomShulker;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CustomShulkerHeadLayer extends RenderLayer<CustomShulker, ShulkerModel<CustomShulker>> {
    public CustomShulkerHeadLayer(RenderLayerParent<CustomShulker, ShulkerModel<CustomShulker>> parent) {
        super(parent);
    }

    public void render(
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        CustomShulker shulker,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        ResourceLocation resourcelocation = CustomShulkerRenderer.getShulkerTexture(shulker);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(resourcelocation));
        this.getParentModel().getHead().render(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(shulker, 0.0F));
    }
}
