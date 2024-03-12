package fr.iglee42.resourcefulshulkers.mixins;

import fr.iglee42.resourcefulshulkers.utils.Materials;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Sheets.class)
public class SheetsMixin {
    @Inject(at = @At("HEAD"), method = "getAllMaterials")
    private static void getAllMaterials(Consumer<Material> consumer, CallbackInfo info) {
        consumer.accept(Materials.OVERWORLD_SHULKER_TEXTURE);
        consumer.accept(Materials.SKY_SHULKER_TEXTURE);
        consumer.accept(Materials.NETHER_SHULKER_TEXTURE);
        ShulkersManager.TYPES.forEach(r->consumer.accept(r.getMaterial()));
        ShulkersManager.TYPES.forEach(r->consumer.accept(r.getBoxMaterial()));
    }
}
