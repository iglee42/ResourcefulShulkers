package fr.iglee42.resourcefulshulkers.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.resourcefulshulkers.api.types.IRegisteredType;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public record TypeDefinition(ResourceLocation id, String name, boolean createShulker) implements ITypeDefinition {

    public static final Codec<TypeDefinition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(TypeDefinition::id),
                    Codec.STRING.fieldOf("name").forGetter(TypeDefinition::name),
                    Codec.BOOL.optionalFieldOf("create_shulker",true).forGetter(TypeDefinition::createShulker)
            ).apply(instance, TypeDefinition::new)
    );

    @Override
    public @NotNull IRegisteredType registration() {
        return TypesManager.getTypeFromDefinition(this);
    }

}
