package fr.iglee42.techresourcesshulker.utils;

import fr.iglee42.techresourcesshulker.customize.Types;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public record Resource(String name, int id, Item item, DyeColor color, int shellItemColor) {

    public static Resource getById(int id){
        return Types.TYPES.stream().filter(r->r.id == id).findFirst().orElse(null);
    }


}
