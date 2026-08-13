package fr.iglee42.resourcefulshulkers.shulkers;

import fr.iglee42.resourcefulshulkers.api.shulkers.IRegisteredShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulkerBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RegisteredShulker implements IRegisteredShulker {

    private final ShulkerDefinition definition;

    private Supplier<Block> generatingBox;
    private Supplier<EntityType<? extends ResourceShulker>> entityType;
    private Supplier<EntityType<? extends ResourceShulkerBullet>> bulletType;
    private Supplier<Item> shulkerItem;
    private Supplier<Item> shell;

    public RegisteredShulker(ShulkerDefinition definition) {
        this.definition = definition;
    }

    @Override
    public ShulkerDefinition definition() {
        return definition;
    }

    @Override
    public @Nullable Supplier<Block> generatingBox() {
        return generatingBox;
    }

    @Override
    public @Nullable Supplier<EntityType<? extends ResourceShulker>> entityType() {
        return entityType;
    }

    @Override
    public @Nullable Supplier<EntityType<? extends ResourceShulkerBullet>> bulletType() {
        return bulletType;
    }

    @Override
    public @Nullable Supplier<Item> shulkerItem() {
        return shulkerItem;
    }

    @Override
    public @Nullable Supplier<Item> shell() {
        return shell;
    }

    public void setGeneratingBox(Supplier<Block> generatingBox) {
        if (this.generatingBox != null) {
            throw new IllegalStateException("Generating box supplier is already set for " + definition.id());
        }
        this.generatingBox = generatingBox;
    }

    public void setupEntity(Supplier<EntityType<? extends ResourceShulker>> entityType,Supplier<Item> shulkerItem) {
        if (this.entityType != null) {
            throw new IllegalStateException("Entity type supplier is already set for " + definition.id());
        }
        this.shulkerItem = shulkerItem;
        this.entityType = entityType;
    }

    public void setBulletType(Supplier<EntityType<? extends ResourceShulkerBullet>> bulletType) {
        if (this.bulletType != null) {
            throw new IllegalStateException("Bullet type supplier is already set for " + definition.id());
        }
        this.bulletType = bulletType;
    }

    public void setShell(Supplier<Item> shell) {
        if (this.shell != null) {
            throw new IllegalStateException("Shell supplier is already set for " + definition.id());
        }
        this.shell = shell;
    }
}