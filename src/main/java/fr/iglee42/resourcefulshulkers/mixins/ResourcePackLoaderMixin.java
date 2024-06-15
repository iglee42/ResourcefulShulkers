package fr.iglee42.resourcefulshulkers.mixins;

import fr.iglee42.resourcefulshulkers.resourcepack.InMemoryPack;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackLoader.class)
public class ResourcePackLoaderMixin {

    @Inject(method = "populatePackRepository", at = @At("RETURN"), remap = false)
    private static void inject(PackRepository resourcePacks, PackType packType, CallbackInfo ci){
        InMemoryPack.injectDatapackFinder(resourcePacks);
    }
}
