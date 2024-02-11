package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ShellItem extends Item {
    private final ResourceLocation id;

    public ShellItem(ResourceLocation id) {
        super(new Item.Properties().tab(ResourcefulShulkers.SHULKERS_GROUP));
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }
}
