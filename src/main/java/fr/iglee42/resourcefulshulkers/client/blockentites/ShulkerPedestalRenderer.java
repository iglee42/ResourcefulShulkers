package fr.iglee42.resourcefulshulkers.client.blockentites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ShulkerPedestalRenderer implements BlockEntityRenderer<ShulkerPedestalBlockEntity> {
    public ShulkerPedestalRenderer(BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void render(ShulkerPedestalBlockEntity be, float partialTick, PoseStack matrix, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = be.getStack();
        Minecraft minecraft = Minecraft.getInstance();

        if (stack.isEmpty()) return;

        matrix.pushPose();
        matrix.translate(0.5D, 1.2D, 0.5D);
        float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
        matrix.scale(scale, scale, scale);
        double tick = System.currentTimeMillis() / 800.0D;
        matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
        matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
        minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, matrix, buffer,be.getLevel(), 0);
        matrix.popPose();
    }
}