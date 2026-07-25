package fr.iglee42.resourcefulshulkers.api.types;

import fr.iglee42.resourcefulshulkers.api.shulkers.IRegisteredShulker;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a type definition in the Resourceful Shulkers mod.
 * A type definition contains information about a specific type of shulker, including its unique identifier,
 * display name, and whether it has an associated shulker.
 */
public interface ITypeDefinition {

    /**
     * Returns the unique identifier of this type.
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
     * Returns whether this type has a Shulker associated with it.
     *
     * @return {@code true} if this type has a Shulker, {@code false} otherwise
     */
    boolean createShulker();

    /**
     * Returns the runtime registration bound to this definition.
     *
     * @return the matching registration, never {@code null}
     */
    @NotNull IRegisteredType registration();

}
