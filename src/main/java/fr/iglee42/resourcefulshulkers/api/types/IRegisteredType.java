package fr.iglee42.resourcefulshulkers.api.types;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.entity.shulkers.TypeShulker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Binds a {@link ITypeDefinition} to the runtime game objects generated for it.
 * <p>
 * While a definition holds the config-driven description of a ShulkerType, a
 * registration links it to the actual registered {@link EntityType}
 * and shulker item {@link Item} created from that definition. The objects are exposed as
 * {@link Supplier suppliers} so they can be referenced before registration has
 * completed and resolved lazily afterwards.
 *
 * @see ITypeDefinition
 */
public interface IRegisteredType {

    /**
     * Returns the definition this registration is built from.
     *
     * @return the backing type definition
     */
    ITypeDefinition definition();

    /**
     * Returns a supplier for the generated Shulker entity type.
     *
     * @return a supplier yielding the entity type, or {@code null} if none is bound or if the type is not associated with an shulker entity
     */
    @Nullable Supplier<EntityType<? extends TypeShulker>> entityType();

    /**
     * Returns a supplier for the generated shulker item.
     *
     * @return a supplier yielding the shulker item, or {@code null} if none is bound or if the type is not associated with an shulker entity
     */
    @Nullable Supplier<Item> shulkerItem();


    /**
     * Returns a supplier for the generated essence item.
     *
     * @return a supplier yielding the essence item, or {@code null} if none is bound
     */
    @Nullable Supplier<Item> essenceItem();

}
