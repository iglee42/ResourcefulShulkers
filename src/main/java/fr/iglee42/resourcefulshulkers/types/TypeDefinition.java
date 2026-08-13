package fr.iglee42.resourcefulshulkers.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.resourcefulshulkers.api.types.IRegisteredType;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public record TypeDefinition(ResourceLocation id, String name, boolean createShulker, @Nullable ResourceLocation texture) implements ITypeDefinition {

    public static final Codec<TypeDefinition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(TypeDefinition::id),
                    Codec.STRING.fieldOf("name").forGetter(TypeDefinition::name),
                    Codec.BOOL.optionalFieldOf("create_shulker",true).forGetter(TypeDefinition::createShulker),
                    ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(def-> Optional.ofNullable(def.texture))
            ).apply(instance, (id,name,create,texture)->new TypeDefinition(id,name,create,texture.orElse(null)))
    );

    @Override
    public @NotNull ResourceLocation texture() {
        return texture != null ? texture : ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "entity/shulker/types/" + id.getPath());
    }

    @Override
    public @NotNull IRegisteredType registration() {
        return TypesManager.getTypeFromDefinition(this);
    }

}
