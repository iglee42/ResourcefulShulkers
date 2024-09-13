package fr.iglee42.resourcefulshulkers.utils;

import fr.iglee42.igleelib.api.utils.DefaultParameter;
import net.minecraft.resources.ResourceLocation;

public record Type(ResourceLocation id, String displayName, @DefaultParameter(booleanValue = true)boolean shouldCreateEntity) {
    public static Type getById(ResourceLocation id){
        return TypesManager.TYPES.stream().filter(r->r.id.equals(id)).findFirst().orElse(null);
    }

}
