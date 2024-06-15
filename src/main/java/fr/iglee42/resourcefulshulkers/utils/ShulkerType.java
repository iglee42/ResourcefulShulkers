package fr.iglee42.resourcefulshulkers.utils;

import fr.iglee42.igleelib.api.utils.DefaultParameter;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static net.minecraft.client.renderer.Sheets.SHULKER_SHEET;

public record ShulkerType(ResourceLocation id, @DefaultParameter(stringValue = "minecraft:stone") String item, @DefaultParameter(stringValue = "black") String color, String shellItemColor, @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String texture, @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String boxTexture,@DefaultParameter(stringValue = "overworld") String type) {

    public static ShulkerType getById(ResourceLocation id){
        return ShulkersManager.TYPES.stream().filter(r->r.id.equals(id)).findFirst().orElse(null);
    }

    public Material getMaterial(){
        return new Material(SHULKER_SHEET,new ResourceLocation(ResourcefulShulkers.MODID,getTexture().getPath().replace(".png","")));
    }
    public Material getBoxMaterial(){
        return new Material(SHULKER_SHEET,new ResourceLocation(ResourcefulShulkers.MODID,getBoxTexture().getPath().replace(".png","")));
    }

    public DyeColor getColor() {
        return DyeColor.byName(color.toUpperCase(),DyeColor.BLACK);
    }
    public int getShellColor(){
        return Integer.parseInt(shellItemColor,16);
    }

    public Item getItem(){
        if (item.startsWith("#")){
            TagKey<Item> tagKey = ItemTags.create(new ResourceLocation(item.substring(1)));
            HolderSet.Named<Item> tag = BuiltInRegistries.ITEM.getOrCreateTag(tagKey);
            return tag.size() == 0 ? Items.AIR : tag.stream().toList().get(0).value();
        } else {
            return BuiltInRegistries.ITEM.get(new ResourceLocation(item));
        }
    }

    public ResourceLocation getTexture(){
        return texture != null ? new ResourceLocation(texture) : new ResourceLocation(ResourcefulShulkers.MODID,"entity/mod_base/"+id.getPath().toLowerCase()+".png");
    }

    public ResourceLocation getBoxTexture(){
        return boxTexture != null ? new ResourceLocation(boxTexture) : new ResourceLocation(ResourcefulShulkers.MODID,"entity/boxes/"+id.getPath().toLowerCase()+".png");
    }

}
