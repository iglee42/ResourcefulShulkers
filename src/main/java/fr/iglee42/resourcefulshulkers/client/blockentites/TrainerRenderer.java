package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.trainer.TrainerBlockEntity;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.item.shulker.ResourceShulkerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

import static fr.iglee42.resourcefulshulkers.client.blockentites.EndCityRenderer.POSITIONS;

public class TrainerRenderer implements BlockEntityRenderer<TrainerBlockEntity> {

    private final BlockRenderDispatcher dispatcher;
    private final EntityRenderDispatcher entityDispatcher;

    public TrainerRenderer(BlockEntityRendererProvider.Context ctx) {
        dispatcher = ctx.getBlockRenderDispatcher();
        entityDispatcher = ctx.getEntityRenderer();
    }

    @Override
    public void render(TrainerBlockEntity be, float pt, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        for (int slot = 0; slot < be.getShulkers().getSlots(); slot++){
            if (be.getShulkers().getStackInSlot(slot).isEmpty() || !(be.getShulkers().getStackInSlot(slot).getItem() instanceof ResourceShulkerItem item)) continue;
            Entity entity = item.entityType().create(be.getLevel());
            if (!(entity instanceof ResourceShulker shulker)) continue;
            shulker.setPeekAmountRaw(3);
            shulker.setCustomName(be.getShulkers().getStackInSlot(slot).getHoverName());
            float[] pos = POSITIONS[slot];
            poseStack.pushPose();
            poseStack.translate(8/16F, 1.25F, 8/16F);
            poseStack.translate(pos[0], pos[1], pos[2]);
            poseStack.scale(0.25F, 0.25F, 0.25F);
            if (slot == 0) poseStack.mulPose(Axis.YP.rotationDegrees(180));
            else if (slot == 1) poseStack.mulPose(Axis.YP.rotationDegrees(270));
            else if (slot == 3) poseStack.mulPose(Axis.YP.rotationDegrees(90));
            entityDispatcher.render(shulker, 0,0,0,0,pt,poseStack, multiBufferSource, packedLight);
            poseStack.popPose();
        }
    }
}
