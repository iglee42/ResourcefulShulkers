package fr.iglee42.techresourcesshulker.customize;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.entity.CustomShulkerBullet;
import fr.iglee42.techresourcesshulker.entity.ResourceShulker;
import fr.iglee42.techresourcesshulker.utils.Resource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Types {


    public static List<Resource> TYPES = new ArrayList<>();
    public static Map<Integer, RegistryObject<EntityType<ResourceShulker>>> ENTITY_TYPES = new HashMap<>();
    public static Map<Integer, RegistryObject<EntityType<CustomShulkerBullet>>> BULLET_TYPES = new HashMap<>();
    private static int id;

    private static int id(){
        return id++;
    }

    public static void init(){
        TYPES.add(new Resource("test",id(), Items.DIAMOND, DyeColor.LIGHT_BLUE,0X00E6FF));
        TYPES.add(new Resource("wood",id(),Items.OAK_LOG,DyeColor.BROWN,0X612B02));
        TYPES.forEach(t->{
            ModContent.createBox(t.id());
            ModContent.createShell(t.id());
            ENTITY_TYPES.put(t.id(),ModContent.createShulker(t.id()));
            BULLET_TYPES.put(t.id(),ModContent.createBullet(t.id()));
        } );
    }


}
