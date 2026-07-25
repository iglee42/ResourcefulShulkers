package fr.iglee42.resourcefulshulkers.mixins;

import  com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.resourcefulshulkers.registries.RSSkullTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AbstractSkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",ordinal = 2,shift = At.Shift.AFTER),remap = false)
    private <T extends LivingEntity> void rs$scaleShulkerHead(PoseStack poseStack, MultiBufferSource p_116732_, int p_116733_, T entity, float p_116735_, float p_116736_, float p_116737_, float p_116738_, float p_116739_, float p_116740_, CallbackInfo ci){
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BlockItem bi && bi.getBlock() instanceof AbstractSkullBlock skull && skull.getType() == RSSkullTypes.SHULKER){
            poseStack.scale(2.0f,2.0f,2.0f);
        }
    }
}
