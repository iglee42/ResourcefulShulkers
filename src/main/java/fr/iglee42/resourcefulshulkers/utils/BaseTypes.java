package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public enum BaseTypes {

    ALUMINUM("#forge:ingots/aluminum", DyeColor.LIGHT_GRAY,"C2AEA0"),
    AMETHYST(Items.AMETHYST_SHARD, DyeColor.MAGENTA,"B38EF3"),
    BRASS("#forge:ingots/brass", DyeColor.ORANGE,"E4B763"),
    BRONZE("#forge:ingots/bronze", DyeColor.ORANGE,"CE6E26"),
    CLAY(Items.CLAY_BALL, DyeColor.LIGHT_GRAY,"A1A7B1"),
    COAL(Items.COAL, DyeColor.BLACK,"151515"),
    CONSTANTAN("#forge:ingots/constantan", DyeColor.ORANGE,"AA7030"),
    COPPER(Items.COPPER_INGOT, DyeColor.ORANGE,"C26B4C"),
    DIAMOND(Items.DIAMOND, DyeColor.LIGHT_BLUE,"65F5E3"),
    DYE(Items.WHITE_DYE, DyeColor.BLACK,"FFFFFF"),
    ELECTRUM("#forge:ingots/electrum", DyeColor.YELLOW,"C9B255"),
    EMERALD(Items.EMERALD, DyeColor.LIME,"17DD62"),
    GOLD(Items.GOLD_INGOT, DyeColor.YELLOW,"FEE048"),
    GRAVEL(Items.GRAVEL, DyeColor.LIGHT_GRAY,"817F7F"),
    INVAR("#forge:ingots/invar", DyeColor.LIGHT_GRAY,"A3B1A8"),
    IRON(Items.IRON_INGOT, DyeColor.WHITE,"E6E6E6"),
    LAPIS(Items.LAPIS_LAZULI, DyeColor.BLUE,"1E4285"),
    LEAD("#forge:ingots/lead", DyeColor.BLUE,"484E6A"),
    NETHERITE(Items.NETHERITE_SCRAP, DyeColor.BROWN,"4D494D"),
    NICKEL("#forge:ingots/nickel", DyeColor.YELLOW,"BCAD7E"),
    OBSIDIAN(Items.OBSIDIAN, DyeColor.PURPLE,"271E3D"),
    OSMIUM("#forge:ingots/osmium", DyeColor.LIGHT_GRAY,"A5B1BA"),
    QUARTZ(Items.QUARTZ, DyeColor.WHITE,"F2EFED"),
    REDSTONE(Items.REDSTONE, DyeColor.RED,"7E0F00"),
    REFINED_GLOWSTONE("#forge:ingots/refined_glowstone", DyeColor.YELLOW,"EABE2B"),
    REFINED_OBSIDIAN("#forge:ingots/refined_obsidian", DyeColor.PURPLE,"4A3C64"),
    SIGNALUM("#forge:ingots/signalum", DyeColor.RED,"ED3706"),
    SILVER("#forge:ingots/silver", DyeColor.LIGHT_GRAY,"758392"),
    STONE(Items.STONE, DyeColor.LIGHT_GRAY,"7F7F7F"),
    TIN("#forge:ingots/tin", DyeColor.LIGHT_BLUE,"476F81"),
    URANIUM("#forge:ingots/uranium", DyeColor.LIME,"9FC3A1"),
    WOOD(Items.OAK_LOG, DyeColor.BROWN,"745A36"),
    ZINC("#forge:ingots/zinc", DyeColor.LIGHT_GRAY,"AAB59D"),

    ENDERMAN(Items.ENDER_PEARL,DyeColor.BLACK, "161616"),
    BLAZE(Items.BLAZE_ROD,DyeColor.YELLOW,"FFD528"),
    CREEPER(Items.GUNPOWDER,DyeColor.LIME,"4AC63A"),
    ZOMBIE(Items.ROTTEN_FLESH, DyeColor.GREEN,"497135"),
    SKELETON(Items.BONE,DyeColor.WHITE,"BCBCBC"),
    GUARDIAN(Items.PRISMARINE_SHARD,DyeColor.CYAN,"4E7966")
    ;

    private String item, color, shellColor;

    BaseTypes(String item, DyeColor color, String shellColor) {
        this.item = item;
        this.color = color.toString();
        this.shellColor = shellColor;
    }
    BaseTypes(Item item, DyeColor color, String shellColor) {
        this.item = ForgeRegistries.ITEMS.getKey(item).toString();
        this.color = color.toString();
        this.shellColor = shellColor;
    }

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("id",MODID + ":"+name().toLowerCase());
        obj.addProperty("item",item);
        obj.addProperty("color",color);
        obj.addProperty("shellItemColor",shellColor);
        obj.addProperty("texture",MODID + ":entity/mod_base/"+name().toLowerCase()+".png");
        obj.addProperty("boxTexture",MODID + ":entity/boxes/"+name().toLowerCase()+".png");
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

}
