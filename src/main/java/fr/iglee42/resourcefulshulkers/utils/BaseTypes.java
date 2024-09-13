package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public enum BaseTypes {
    
    ALUMINUM("#forge:ingots/aluminum", DyeColor.LIGHT_GRAY,"C2AEA0","overworld"),
    AMETHYST(Items.AMETHYST_SHARD, DyeColor.MAGENTA,"B38EF3","overworld"),
    BRASS("#forge:ingots/brass", DyeColor.ORANGE,"E4B763","sky"),
    BRONZE("#forge:ingots/bronze", DyeColor.ORANGE,"CE6E26","sky"),
    CLAY(Items.CLAY_BALL, DyeColor.LIGHT_GRAY,"A1A7B1","overworld"),
    COAL(Items.COAL, DyeColor.BLACK,"151515","overworld"),
    CONSTANTAN("#forge:ingots/constantan", DyeColor.ORANGE,"AA7030","sky"),
    COPPER(Items.COPPER_INGOT, DyeColor.ORANGE,"C26B4C","overworld"),
    DIAMOND(Items.DIAMOND, DyeColor.LIGHT_BLUE,"65F5E3","end"),
    DYE(ModItems.OMNI_DYE.getId().toString(), DyeColor.BLACK,"FFFFFF","sky"),
    ELECTRUM("#forge:ingots/electrum", DyeColor.YELLOW,"C9B255","sky"),
    EMERALD(Items.EMERALD, DyeColor.LIME,"17DD62","end"),
    GOLD(Items.GOLD_INGOT, DyeColor.YELLOW,"FEE048","nether"),
    GRAVEL(Items.GRAVEL, DyeColor.LIGHT_GRAY,"817F7F","overworld"),
    INVAR("#forge:ingots/invar", DyeColor.LIGHT_GRAY,"A3B1A8","sky"),
    IRON(Items.IRON_INGOT, DyeColor.WHITE,"E6E6E6","overworld"),
    LAPIS(Items.LAPIS_LAZULI, DyeColor.BLUE,"1E4285","sky"),
    LEAD("#forge:ingots/lead", DyeColor.BLUE,"484E6A","overworld"),
    NETHERITE(Items.NETHERITE_SCRAP, DyeColor.BROWN,"4D494D","nether"),
    NICKEL("#forge:ingots/nickel", DyeColor.YELLOW,"BCAD7E","overworld"),
    OBSIDIAN(Items.OBSIDIAN, DyeColor.PURPLE,"271E3D","nether"),
    OSMIUM("#forge:ingots/osmium", DyeColor.LIGHT_GRAY,"A5B1BA","overworld"),
    QUARTZ(Items.QUARTZ, DyeColor.WHITE,"F2EFED","nether"),
    REDSTONE(Items.REDSTONE, DyeColor.RED,"7E0F00","sky"),
    REFINED_GLOWSTONE("#forge:ingots/refined_glowstone", DyeColor.YELLOW,"EABE2B","nether"),
    REFINED_OBSIDIAN("#forge:ingots/refined_obsidian", DyeColor.PURPLE,"4A3C64","end"),
    SIGNALUM("#forge:ingots/signalum", DyeColor.RED,"ED3706","nether"),
    SILVER("#forge:ingots/silver", DyeColor.LIGHT_GRAY,"758392","overworld"),
    STONE(Items.STONE, DyeColor.LIGHT_GRAY,"7F7F7F","overworld"),
    TIN("#forge:ingots/tin", DyeColor.LIGHT_BLUE,"476F81","overworld"),
    URANIUM("#forge:ingots/uranium", DyeColor.LIME,"9FC3A1","overworld"),
    WOOD(Items.OAK_LOG, DyeColor.BROWN,"745A36","overworld"),
    ZINC("#forge:ingots/zinc", DyeColor.LIGHT_GRAY,"AAB59D","overworld"),

    ENDERMAN(Items.ENDER_PEARL,DyeColor.BLACK, "161616","end"),
    BLAZE(Items.BLAZE_ROD,DyeColor.YELLOW,"FFD528","nether"),
    CREEPER(Items.GUNPOWDER,DyeColor.LIME,"4AC63A","overworld"),
    ZOMBIE(Items.ROTTEN_FLESH, DyeColor.GREEN,"497135","overworld"),
    SKELETON(Items.BONE,DyeColor.WHITE,"BCBCBC","overworld"),
    GUARDIAN(Items.PRISMARINE_SHARD,DyeColor.CYAN,"4E7966","sky")
    ;

    private String item, color, shellColor,type;

    BaseTypes(String item, DyeColor color, String shellColor,String type) {
        this.item = item;
        this.color = color.toString();
        this.shellColor = shellColor;
        this.type = type;
    }
    BaseTypes(Item item, DyeColor color, String shellColor,String type) {
        this.item = ForgeRegistries.ITEMS.getKey(item).toString();
        this.color = color.toString();
        this.shellColor = shellColor;
        this.type = type;
    }

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("id",MODID + ":"+name().toLowerCase());
        obj.addProperty("item",item);
        obj.addProperty("color",color);
        obj.addProperty("shellItemColor",shellColor);
        obj.addProperty("texture",MODID + ":entity/mod_base/"+name().toLowerCase()+".png");
        obj.addProperty("boxTexture",MODID + ":entity/boxes/"+name().toLowerCase()+".png");
        obj.addProperty("type",MODID + ":"+ type);
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

}
