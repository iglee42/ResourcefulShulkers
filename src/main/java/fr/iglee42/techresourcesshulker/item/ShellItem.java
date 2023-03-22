package fr.iglee42.techresourcesshulker.item;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import net.minecraft.world.item.Item;

public class ShellItem extends Item {
    private final int id;

    public ShellItem(int id) {
        super(new Item.Properties().tab(TechResourcesShulker.GROUP));
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
