package fr.iglee42.resourcefulshulkers.client.items;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class GeneratingBoxItemExtension implements IClientItemExtensions {

    private static final Renderer RENDERER = new Renderer();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return RENDERER;
    }

    public static class Renderer extends BlockEntityWithoutLevelRenderer{
        public Renderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
            if (stack.getItem() instanceof GeneratingBoxItem it){
                BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
                dispatcher.renderItem(new GeneratingBoxBlockEntity(BlockPos.ZERO, it.getBlock().defaultBlockState(), it.definition()), poseStack, bufferSource, packedLight, packedOverlay);
            }
        }
    }
}
