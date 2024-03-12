package fr.iglee42.resourcefulshulkers.utils;

import fr.iglee42.igleelib.api.utils.DefaultParameter;
import fr.iglee42.igleelib.api.utils.OptionalParameter;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import static net.minecraft.client.renderer.Sheets.SHULKER_SHEET;

public record ShulkerType(ResourceLocation id, @DefaultParameter(stringValue = "minecraft:stone") String item, @DefaultParameter(stringValue = "black") @OptionalParameter String color, String shellItemColor, @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String texture,@OptionalParameter @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String boxTexture) {

    public static ShulkerType getById(ResourceLocation id){
        return ShulkersManager.TYPES.stream().filter(r->r.id == id).findFirst().orElse(null);
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
            ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();
            TagKey<Item> tagKey = tagManager.createTagKey(new ResourceLocation(item.substring(1)));
            ITag<Item> tag = tagManager.getTag(tagKey);
            return tag.isEmpty() ? Items.AIR : tag.stream().toList().get(0);
        } else {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
        }
    }

    public ResourceLocation getTexture(){
        return texture != null ? new ResourceLocation(texture) : new ResourceLocation(ResourcefulShulkers.MODID,"entity/mod_base/"+id.getPath().toLowerCase()+".png");
    }

    public ResourceLocation getBoxTexture(){
        return boxTexture != null ? new ResourceLocation(boxTexture) : new ResourceLocation(ResourcefulShulkers.MODID,"entity/boxes/"+id.getPath().toLowerCase()+".png");
    }

}
