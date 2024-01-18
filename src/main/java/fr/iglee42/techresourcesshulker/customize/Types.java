package fr.iglee42.techresourcesshulker.customize;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.utils.Resource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Types {


    public static List<Resource> TYPES = new ArrayList<>();

    private static int id;

    private static int id(){
        return id++;
    }

    public static void init(){
        TYPES.add(new Resource("test",id(), Items.DIAMOND, DyeColor.LIGHT_BLUE,0X00E6FF));
        TYPES.forEach(t->{
            ModContent.createBox(t.id());
            ModContent.createShell(t.id());
            ModContent.createShulker(t.id());
        } );
    }


}
