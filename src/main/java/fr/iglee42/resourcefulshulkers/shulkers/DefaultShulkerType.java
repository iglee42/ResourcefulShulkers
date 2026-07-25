package fr.iglee42.resourcefulshulkers.shulkers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public enum DefaultShulkerType {
    
    ALUMINUM("#c:ingots/aluminum", "C2AEA0","overworld"),
    AMETHYST(Items.AMETHYST_SHARD, "B38EF3","overworld"),
    BRASS("#c:ingots/brass", "E4B763","sky"),
    BRONZE("#c:ingots/bronze", "CE6E26","sky"),
    CLAY(Items.CLAY_BALL, "A1A7B1","overworld"),
    COAL(Items.COAL, "151515","overworld"),
    CONSTANTAN("#c:ingots/constantan", "AA7030","sky"),
    COPPER(Items.COPPER_INGOT, "C26B4C","overworld"),
    DIAMOND(Items.DIAMOND, "65F5E3","end"),
    DYE("#c:dyes", "FFFFFF","sky"),
    ELECTRUM("#c:ingots/electrum", "C9B255","sky"),
    EMERALD(Items.EMERALD, "17DD62","end"),
    GOLD(Items.GOLD_INGOT, "FEE048","nether"),
    GRAVEL(Items.GRAVEL, "817F7F","overworld"),
    INVAR("#c:ingots/invar", "A3B1A8","sky"),
    IRON(Items.IRON_INGOT, "E6E6E6","overworld"),
    LAPIS(Items.LAPIS_LAZULI, "1E4285","sky"),
    LEAD("#c:ingots/lead", "484E6A","overworld"),
    NETHERITE(Items.NETHERITE_SCRAP, "4D494D","nether"),
    NICKEL("#c:ingots/nickel", "BCAD7E","overworld"),
    OBSIDIAN(Items.OBSIDIAN, "271E3D","nether"),
    OSMIUM("#c:ingots/osmium", "A5B1BA","overworld"),
    QUARTZ(Items.QUARTZ, "F2EFED","nether"),
    REDSTONE(Items.REDSTONE, "7E0F00","sky"),
    REFINED_GLOWSTONE("#c:ingots/refined_glowstone", "EABE2B","nether"),
    REFINED_OBSIDIAN("#c:ingots/refined_obsidian", "4A3C64","end"),
    SIGNALUM("#c:ingots/signalum", "ED3706","nether"),
    SILVER("#c:ingots/silver", "758392","overworld"),
    STONE(Items.STONE, "7F7F7F","overworld"),
    TIN("#c:ingots/tin", "476F81","overworld"),
    URANIUM("#c:ingots/uranium", "9FC3A1","overworld"),
    WOOD("#minecraft:logs", "745A36","overworld"),
    ZINC("#c:ingots/zinc", "AAB59D","overworld"),

    ENDERMAN(Items.ENDER_PEARL, "161616","end"),
    BLAZE(Items.BLAZE_ROD, "FFD528","nether"),
    CREEPER(Items.GUNPOWDER, "4AC63A","overworld"),
    ZOMBIE(Items.ROTTEN_FLESH, "497135","overworld"),
    SKELETON(Items.BONE, "BCBCBC","overworld"),
    GUARDIAN(Items.PRISMARINE_SHARD, "4E7966","sky")
    ;

    private String item, color,type;

    DefaultShulkerType(String item, String color, String type) {
        this.item = item;
        this.color = color;
        this.type = type;
    }
    DefaultShulkerType(Item item, String color, String type) {
        this(BuiltInRegistries.ITEM.getKey(item).toString(), color, type);
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("id",MODID + ":"+name().toLowerCase());
        obj.addProperty("name", ModsUtils.getUpperName(name().toLowerCase(),"_"));
        obj.addProperty("item",item);
        obj.addProperty("color","#"+color);
        obj.addProperty("type",MODID + ":"+ type);
        return GSON.toJson(obj);
    }

}
