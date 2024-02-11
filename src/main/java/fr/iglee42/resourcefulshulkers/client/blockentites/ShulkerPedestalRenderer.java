package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class ShulkerPedestalRenderer implements BlockEntityRenderer<ShulkerPedestalBlockEntity> {
    public ShulkerPedestalRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(ShulkerPedestalBlockEntity be, float v, PoseStack matrix, MultiBufferSource buffer, int i, int i1) {
        ItemStack stack = be.getStack();
        Minecraft minecraft = Minecraft.getInstance();

        if (!stack.isEmpty()) {
            matrix.pushPose();
            matrix.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
            matrix.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrix.mulPose(Vector3f.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            minecraft.getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, i, i1, matrix, buffer, 0);
            matrix.popPose();
        }
    }
}