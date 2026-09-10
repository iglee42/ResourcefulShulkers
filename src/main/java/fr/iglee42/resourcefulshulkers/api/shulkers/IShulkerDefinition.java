package fr.iglee42.resourcefulshulkers.api.shulkers;

import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.utils.LazyIngredient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Describes a single kind of Shulker known to Resourceful Shulkers.
 * <p>
 * A definition is the config-driven description of a Shulker variant (its identity,
 * display name, crafting item, textures, tint and category). Definitions are
 * typically loaded from configuration files and are immutable. The runtime objects that a
 * definition is bound to (block, entity type, shell item) are exposed separately
 * through its {@link #registration()}.
 *
 * @see IRegisteredShulker
 */
public interface IShulkerDefinition {

    /**
     * Returns the unique identifier of this Shulker.
     *
     * @return the definition id, never {@code null}
     */
    @NotNull ResourceLocation id();

    /**
     * Returns the display name shown to players.
     *
     * @return the name, never {@code null}
     */
    @NotNull String name();

    /**
     * Returns the item (or item tag) used to identify or craft this Shulker.
     *
     * @return the lazily-resolved ingredient, never {@code null}
     * @see #hasItem()
     */
    @NotNull LazyIngredient item();

    /**
     * Returns the texture used to render this Shulker.
     * <p>
     * Implementations may derive a default location from the {@link #id()} and
     * {@link #type()} when no explicit texture is provided.
     *
     * @return the entity texture location, never {@code null}
     */
    @NotNull ResourceLocation texture();

    /**
     * Returns the texture used for this Shulker's box/shell.
     * <p>
     * When no dedicated box texture is set, implementations fall back to
     * {@link #texture()}, so this never returns {@code null}.
     *
     * @return the box texture location, never {@code null}
     */
    @NotNull ResourceLocation boxTexture();

    /**
     * Returns the tint applied to this Shulker.
     *
     * @return the color packed as an {@code 0xRRGGBB} integer
     */
    int color();

    /**
     * Returns the category this Shulker belongs to (for example the dimension or
     * grouping the variant is associated with).
     *
     * @return the type definition, never {@code null}
     */
    @NotNull ITypeDefinition type();

    /**
     * Returns the runtime registration bound to this definition.
     *
     * @return the matching registration, never {@code null}
     */
    @NotNull IRegisteredShulker registration();

    /**
     * Convenience check for whether this definition has a usable identifying item.
     *
     * @return {@code true} if {@link #item()} resolves to a non-empty ingredient
     */
    default boolean hasItem(){
        return item().getIngredient().getItems().length > 0 && Arrays.stream(item().getIngredient().getItems()).noneMatch(it->it.is(Items.BARRIER));
    }

}