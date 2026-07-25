package fr.iglee42.resourcefulshulkers.shulkers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.resourcefulshulkers.api.shulkers.IRegisteredShulker;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import fr.iglee42.resourcefulshulkers.utils.LazyIngredient;
import fr.iglee42.resourcefulshulkers.utils.RSExtraCodecs;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;


public final class ShulkerDefinition implements IShulkerDefinition {

    public static final Codec<ShulkerDefinition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ShulkerDefinition::id),
                    Codec.STRING.fieldOf("name").forGetter(ShulkerDefinition::name),
                    LazyIngredient.CODEC.fieldOf("item").forGetter(ShulkerDefinition::item),
                    ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(def -> Optional.ofNullable(def.texture)),
                    ResourceLocation.CODEC.optionalFieldOf("box_texture").forGetter(def -> Optional.ofNullable(def.boxTexture)),
                    RSExtraCodecs.COLOR.fieldOf("color").forGetter(ShulkerDefinition::color),
                    ResourceLocation.CODEC.fieldOf("type").forGetter(ShulkerDefinition::typeId)
            ).apply(instance, (id, name, item, texture, boxTexture, color, type) -> new ShulkerDefinition(id, name, item, texture.orElse(null), boxTexture.orElse(null), color, type))
    );
    private final ResourceLocation id;
    private final String name;
    private final LazyIngredient item;
    private final @Nullable ResourceLocation texture;
    private final @Nullable ResourceLocation boxTexture;
    private final int color;
    private final ResourceLocation type;

    public ShulkerDefinition(ResourceLocation id, String name, LazyIngredient item,
                             @Nullable ResourceLocation texture,
                             @Nullable ResourceLocation boxTexture, int color,
                             ResourceLocation type) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.texture = texture;
        this.boxTexture = boxTexture;
        this.color = color;
        this.type = type;
    }

    @Override
    public @NotNull ResourceLocation texture() {
        return texture != null ? texture : ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/entity/shulkers/" + type.getPath() + "/" + id.getPath());
    }

    @Override
    public @NotNull ResourceLocation boxTexture() {
        return boxTexture != null ? boxTexture : texture();
    }

    @Override
    public @NotNull IRegisteredShulker registration() {
        return ShulkersManager.getShulkerFromDefinition(this);
    }

    public ResourceLocation typeId() {
        return type;
    }

    @Override
    public @NotNull ITypeDefinition type() {
        return TypesManager.getDefinition(type);
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LazyIngredient item() {
        return item;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShulkerDefinition) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.item, that.item) &&
                Objects.equals(this.texture, that.texture) &&
                Objects.equals(this.boxTexture, that.boxTexture) &&
                this.color == that.color &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, item, texture, boxTexture, color, type);
    }

    @Override
    public String toString() {
        return "ShulkerDefinition[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "item=" + item + ", " +
                "texture=" + texture + ", " +
                "boxTexture=" + boxTexture + ", " +
                "color=" + color + ", " +
                "type=" + type + ']';
    }

}
