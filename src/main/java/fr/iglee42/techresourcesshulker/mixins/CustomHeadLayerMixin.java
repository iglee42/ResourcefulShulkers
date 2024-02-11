package fr.iglee42.techresourcesshulker.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.iglee42.techresourcesshulker.utils.SkullTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",ordinal = 2,shift = At.Shift.AFTER))
    private <T extends LivingEntity> void inject(PoseStack p_116731_, MultiBufferSource p_116732_, int p_116733_, T p_116734_, float p_116735_, float p_116736_, float p_116737_, float p_116738_, float p_116739_, float p_116740_, CallbackInfo ci){
        if (((AbstractSkullBlock)((BlockItem)p_116734_.getItemBySlot(EquipmentSlot.HEAD).getItem()).getBlock()).getType() == SkullTypes.SHULKER){
            p_116731_.scale(2.0f,2.0f,2.0f);
        }
    }
}
