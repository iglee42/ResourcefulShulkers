package fr.iglee42.resourcefulshulkers.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.blocks.GeneratingBoxBlock;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Inject(method = "renderByItem", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;",shift = At.Shift.AFTER,ordinal = 0),locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_, CallbackInfo ci,Item item) {
        Block block = ((BlockItem)item).getBlock();
        if (block instanceof GeneratingBoxBlock gb){
            blockEntityRenderDispatcher.renderItem(new GeneratingBoxBlockEntity(BlockPos.ZERO, ModBlocks.getBoxById(gb.getId()).defaultBlockState(),gb.getId()), p_108832_, p_108833_, p_108834_, p_108835_);
        }
    }
}
