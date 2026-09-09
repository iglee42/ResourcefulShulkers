package fr.iglee42.resourcefulshulkers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.utils.RSColors;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @ModifyConstant(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", constant = @Constant(intValue = -1))
    private <T extends LivingEntity> int rs$rgbDyeShulker(int constant, @Local(name = "p_115308_")T entity){
        if (entity instanceof ResourceShulker rs){
            if (rs.definition().id().equals(RSIds.id("dye"))){
                return RSColors.getRGBColor().toARGB();
            }
        }
        return constant;
    }

}
