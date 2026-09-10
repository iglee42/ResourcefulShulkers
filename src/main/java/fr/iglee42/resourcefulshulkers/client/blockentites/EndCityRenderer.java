package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class EndCityRenderer implements BlockEntityRenderer<EndCityBlockEntity> {

    static final float[][] POSITIONS = new float[][] {
            {0F,0F,0.75F}, {0.75F,0F,0F}, {0F,0F,-0.75F}, {-0.75F,0F,0F},
            {0F,0.75F,0.75F}, {0.75F,0.75F,0F}, {0F,0.75F,-0.75F}, {-0.75F,0.75F,0F}
    };

    private final BlockRenderDispatcher dispatcher;

    public EndCityRenderer(BlockEntityRendererProvider.Context ctx) {
        dispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(EndCityBlockEntity be, float pt, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        for (int slot = 0; slot < be.getShulkers().getSlots(); slot++){
            if (be.getShulkers().getStackInSlot(slot).isEmpty() || !(be.getShulkers().getStackInSlot(slot).getItem() instanceof GeneratingBoxItem box)) continue;
            BlockState state = box.getBlock().defaultBlockState();
            float[] pos = POSITIONS[slot];
            poseStack.pushPose();
            poseStack.translate(6/16F, 1.25F, 6/16F);
            poseStack.translate(pos[0], pos[1], pos[2]);
            poseStack.scale(0.25F, 0.25F, 0.25F);
            dispatcher.renderSingleBlock(state, poseStack, multiBufferSource, packedLight, packedOverlay, ModelData.EMPTY, RenderType.translucent());
            poseStack.popPose();
        }
    }
}
