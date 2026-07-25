package fr.iglee42.resourcefulshulkers.api.shulkers;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Binds a {@link IShulkerDefinition} to the runtime game objects generated for it.
 * <p>
 * While a definition holds the config-driven description of a Shulker variant, a
 * registration links it to the actual registered {@link Block}, {@link EntityType}
 * and shulker item {@link Item} created from that definition. The objects are exposed as
 * {@link Supplier suppliers} so they can be referenced before registration has
 * completed and resolved lazily afterwards.
 *
 * @see IShulkerDefinition
 */
public interface IRegisteredShulker {

    /**
     * Returns the definition this registration is built from.
     *
     * @return the backing Shulker definition
     */
    IShulkerDefinition definition();

    /**
     * Returns a supplier for the generated Shulker box block.
     *
     * @return a supplier yielding the box block, or {@code null} if none is bound
     */
    @Nullable Supplier<Block> generatingBox();

    /**
     * Returns a supplier for the generated Shulker entity type.
     *
     * @return a supplier yielding the entity type, or {@code null} if none is bound
     */
    @Nullable Supplier<EntityType<?>> entityType();

    /**
     * Returns a supplier for the generated Shulker bullet entity type.
     *
     * @return a supplier yielding the bullet entity type, or {@code null} if none is bound
     */
    @Nullable Supplier<EntityType<?>> bulletType();

    /**
     * Returns a supplier for the generated Shulker item.
     *
     * @return a supplier yielding the shulker item, or {@code null} if none is bound
     */
    @Nullable Supplier<Item> shulkerItem();

    /**
     * Returns a supplier for the generated Shulker shell item.
     *
     * @return a supplier yielding the shell item, or {@code null} if none is bound
     */
    @Nullable Supplier<Item> shell();
}
