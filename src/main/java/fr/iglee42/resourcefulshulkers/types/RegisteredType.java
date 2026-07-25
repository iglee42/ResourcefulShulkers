package fr.iglee42.resourcefulshulkers.types;

import fr.iglee42.resourcefulshulkers.api.types.IRegisteredType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RegisteredType implements IRegisteredType {

    private final TypeDefinition definition;

    private Supplier<EntityType<?>> entityType;
    private Supplier<Item> shulkerItem;
    private Supplier<Item> essenceItem;

    public RegisteredType(TypeDefinition definition) {
        this.definition = definition;
    }

    @Override
    public TypeDefinition definition() {
        return definition;
    }


    @Override
    public @Nullable Supplier<EntityType<?>> entityType() {
        return entityType;
    }

    @Override
    public @Nullable Supplier<Item> shulkerItem() {
        return shulkerItem;
    }

    @Override
    public @Nullable Supplier<Item> essenceItem() {
        return essenceItem;
    }


    public void setupEntity(Supplier<EntityType<?>> entityType,Supplier<Item> shulkerItem) {
        if (this.entityType != null) {
            throw new IllegalStateException("Entity type supplier is already set for " + definition.id());
        }
        this.shulkerItem = shulkerItem;
        this.entityType = entityType;
    }

    // TODO Remove when fully implemented
    public void setShulkerItem(Supplier<Item> shulkerItem) {
        if (this.shulkerItem != null) {
            throw new IllegalStateException("Shulker item supplier is already set for " + definition.id());
        }
        this.shulkerItem = shulkerItem;
    }

    public void setEssenceItem(Supplier<Item> essenceItem) {
        if (this.essenceItem != null) {
            throw new IllegalStateException("Essence item supplier is already set for " + definition.id());
        }
        this.essenceItem = essenceItem;
    }

}