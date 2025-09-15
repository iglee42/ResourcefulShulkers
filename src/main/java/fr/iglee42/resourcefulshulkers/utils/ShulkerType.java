package fr.iglee42.resourcefulshulkers.utils;

import com.google.common.collect.Lists;
import fr.iglee42.igleelib.api.utils.DefaultParameter;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.client.renderer.Sheets.SHULKER_SHEET;

public record ShulkerType(ResourceLocation id, Ingredient item, @DefaultParameter(stringValue = "black") String color, String shellItemColor, @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String texture, @DefaultParameter(stringValue = "minecraft:entity/shulker/shulker") String boxTexture, ResourceLocation type) {

    public static ShulkerType getById(ResourceLocation id){
        return ShulkersManager.TYPES.stream().filter(r->r.id.equals(id)).findFirst().orElse(null);
    }

    public Material getMaterial(){
        return new Material(SHULKER_SHEET,ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,getTexture().getPath().replace(".png","")));
    }
    public Material getBoxMaterial(){
        return new Material(SHULKER_SHEET,ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,getBoxTexture().getPath().replace(".png","")));
    }

    public DyeColor getColor() {
        return DyeColor.byName(color.toUpperCase(),DyeColor.BLACK);
    }
    public int getShellColor(){
        return Integer.parseInt(shellItemColor,16);
    }

    public List<Item> getItems(){
        return Arrays.stream(item.getItems()).map(ItemStack::getItem).sorted(Comparator.comparingInt(BuiltInRegistries.ITEM::getId)).toList();
    }

    public ResourceLocation getTexture(){
        return texture != null ? ResourceLocation.parse(texture) : ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"entity/mod_base/"+id.getPath().toLowerCase()+".png");
    }

    public ResourceLocation getBoxTexture(){
        return boxTexture != null ? ResourceLocation.parse(boxTexture) : ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"entity/boxes/"+id.getPath().toLowerCase()+".png");
    }

    public boolean hasItem(){
        return Arrays.stream(item.getValues()).anyMatch(v->{
            if (v instanceof Ingredient.TagValue tv){
                List<ItemStack> list = Lists.newArrayList();
                Iterator var2 = BuiltInRegistries.ITEM.getTagOrEmpty(tv.tag()).iterator();

                while(var2.hasNext()) {
                    Holder<Item> holder = (Holder)var2.next();
                    list.add(new ItemStack(holder));
                }
                return !list.isEmpty();
            }
            return v.getItems().stream().anyMatch(it->!it.isEmpty());
        });
    }

}
