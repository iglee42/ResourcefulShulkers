package fr.iglee42.resourcefulshulkers.shulkers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.igleelib.common.init.ModItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;
import static fr.iglee42.resourcefulshulkers.registries.RSTags.Items.*;

public enum DefaultShulkerType {

    // Overworld
    ALUMINUM("#c:ingots/aluminum","BACBC9","overworld",ChatFormatting.WHITE),
    AMETHYST(Items.AMETHYST_SHARD,"A678F1","overworld",ChatFormatting.DARK_PURPLE),
    ANTIMATTER("#c:pellets/antimatter","C369DA","overworld",ChatFormatting.LIGHT_PURPLE),
    ARMADILLO(Items.ARMADILLO_SCUTE,"A06460","overworld",ChatFormatting.GOLD),
    BEE(BEE_DROPS,"F5AF23","overworld",ChatFormatting.YELLOW),
    BETTER_GOLD("#c:ingots/better_gold","E2AF0D","overworld",ChatFormatting.GOLD),
    CHICKEN(CHICKEN_DROPS,"D3D3D3","overworld",ChatFormatting.WHITE),
    CLAY(Items.CLAY,"AEB4C9","overworld",ChatFormatting.GRAY),
    COAL(Items.COAL,"151515","overworld",ChatFormatting.DARK_GRAY),
    COPPER(Items.COPPER_INGOT,"A75A40","overworld",ChatFormatting.GOLD),
    COW(COW_DROPS,"443626","overworld",ChatFormatting.GOLD),
    CREEPER(CREEPER_DROPS,"45C336","overworld",ChatFormatting.GREEN),
    DIRT(Items.DIRT,"926948","overworld",ChatFormatting.GOLD),
    FISH(FISH_DROPS,"CEB48F","overworld",ChatFormatting.GOLD),
    FROG(FROG_DROPS,"568022","overworld",ChatFormatting.GOLD),
    GRAVEL(Items.GRAVEL,"968E8E","overworld",ChatFormatting.GRAY),
    IRON(Items.IRON_INGOT,"D1CFCF","overworld",ChatFormatting.GRAY),
    LEAD("#c:ingots/lead","90A29F","overworld",ChatFormatting.GRAY),
    NICKEL("#c:ingots/nickel","939A8A","overworld",ChatFormatting.YELLOW),
    OSMIUM("#c:ingots/osmium","99A3BD","overworld",ChatFormatting.AQUA),
    PIG(Items.PORKCHOP,"EC8985","overworld",ChatFormatting.LIGHT_PURPLE),
    PLASLITHERITE("#c:ingots/plaslitherite","454D4D","overworld",ChatFormatting.DARK_GRAY),
    RABBIT(RABBIT_DROPS,"A5764F","overworld",ChatFormatting.YELLOW),
    REFINED_REDSTONE("#c:ingots/refined_redstone","741123","overworld",ChatFormatting.DARK_RED),
    SHEEP(SHEEP_DROPS,"EBEEEE","overworld",ChatFormatting.WHITE),
    SILVER("#c:ingots/silver","ABD4D4","overworld",ChatFormatting.AQUA),
    SKELETON(SKELETON_DROPS,"A5A5A5","overworld",ChatFormatting.WHITE),
    SLIME("#c:slime_balls","7DB770","overworld",ChatFormatting.GREEN),
    SPIDER(SPIDER_DROPS,"473E35","overworld",ChatFormatting.DARK_GRAY),
    STEEL("#c:ingots/steel","7E7E7D","overworld",ChatFormatting.GRAY),
    STONE("#c:stones","929192","overworld",ChatFormatting.DARK_GRAY),
    TIN("#c:ingots/tin","C8CECD","overworld",ChatFormatting.WHITE),
    TURTLE(Items.TURTLE_SCUTE,"3B983E","overworld",ChatFormatting.GREEN),
    WARDEN(WARDEN_DROPS,"009295","overworld",ChatFormatting.DARK_AQUA),
    WOOD("#minecraft:logs","745A36","overworld",ChatFormatting.GOLD),
    ZINC("#c:ingots/zinc","9B9B87","overworld",ChatFormatting.GRAY),
    ZOMBIE(ZOMBIE_DROPS,"5A7B48","overworld",ChatFormatting.DARK_GREEN),

    // Sky
    AMETHYST_BRONZE("#c:ingots/amethyst_bronze","B16AAD","sky",ChatFormatting.LIGHT_PURPLE),
    AQUAMARINE("#c:gems/aquamarine","3A7A99","sky",ChatFormatting.DARK_AQUA),
    BRASS("#c:ingots/brass","EEC168","sky",ChatFormatting.GOLD),
    BREEZE(Items.BREEZE_ROD,"958DD3","sky",ChatFormatting.AQUA),
    BRONZE("#c:ingots/bronze","E98C54","sky",ChatFormatting.GOLD),
    CERTUS_QUARTZ("#c:gems/certus_quartz","9CD3FF","sky",ChatFormatting.AQUA),
    CONSTANTAN("#c:ingots/constantan","974F40","sky",ChatFormatting.GOLD),
    DEMONITE("#c:ingots/hellforged","68A19B","sky",ChatFormatting.DARK_AQUA),
    DYE("#c:dyes","FFFFFF","sky",ChatFormatting.WHITE),
    ELECTRUM("#c:ingots/electrum","E2BB75","sky",ChatFormatting.YELLOW),
    EVOKER(Items.TOTEM_OF_UNDYING,"1E1F24","sky",ChatFormatting.GRAY),
    FLUIX("#c:gems/fluix","463D79","sky",ChatFormatting.DARK_PURPLE),
    GUARDIAN(GUARDIAN_DROPS,"529983","sky",ChatFormatting.AQUA),
    KNIGHTMETAL("#c:ingots/knightmetal","8E948B","sky",ChatFormatting.GRAY),
    LAPIS(Items.LAPIS_LAZULI,"193D7C","sky",ChatFormatting.DARK_BLUE),
    PHANTOM(Items.PHANTOM_MEMBRANE,"495997","sky",ChatFormatting.DARK_BLUE),
    PIG_IRON("#c:ingots/pig_iron","C87D82","sky",ChatFormatting.LIGHT_PURPLE),
    QUEENS_SLIME("#c:ingots/queens_slime","38661A","sky",ChatFormatting.DARK_GREEN),
    REDSTONE(Items.REDSTONE,"941400","sky",ChatFormatting.DARK_RED),
    ROSE_GOLD("#c:ingots/rose_gold","DBA392","sky",ChatFormatting.LIGHT_PURPLE),
    SLIMESTEEL("#c:ingots/slime_steel","89DADB","sky",ChatFormatting.AQUA),
    STELLARITE("forbidden_arcanus:stellarite_piece","54544B","sky",ChatFormatting.GRAY),
    URANIUM("#c:ingots/uranium","73B275","sky",ChatFormatting.GREEN),
    XP(XP_DROPS,"419760","sky",ChatFormatting.GREEN),

    // Nether
    BASALT(Items.BASALT,"4F4B4F","nether",ChatFormatting.DARK_GRAY),
    BLACKSTONE(Items.BLACKSTONE,"312C36","nether",ChatFormatting.DARK_GRAY),
    BLAZE(Items.BLAZE_ROD,"FFD528","nether",ChatFormatting.GOLD),
    BLAZUM(ModItem.BLAZUM_INGOT,"E5892D","nether",ChatFormatting.GOLD),
    CINDERSLIME("#c:ingots/cinderslime","EA5C35","nether",ChatFormatting.RED),
    COBALT("#c:ingots/cobalt","2376DD","nether",ChatFormatting.BLUE),
    DERIUM(ModItem.DERIUM_INGOT,"771B89","nether",ChatFormatting.DARK_PURPLE),
    GHAST(Items.GHAST_TEAR,"E5E4E4","nether",ChatFormatting.WHITE),
    GOLD(Items.GOLD_INGOT,"FEE048","nether",ChatFormatting.YELLOW),
    HEPATIZON("#c:ingots/hepatizon","523B5D","nether",ChatFormatting.DARK_PURPLE),
    LAVIUM(ModItem.LAVIUM_INGOT,"E53D2D","nether",ChatFormatting.RED),
    MAGMA_CUBE(Items.MAGMA_CREAM,"591600","nether",ChatFormatting.GOLD),
    MANYULLYN("#c:ingots/manyullyn","7D4EB7","nether",ChatFormatting.LIGHT_PURPLE),
    MODIUM(ModItem.MODIUM_INGOT,"2D3CE5","nether",ChatFormatting.BLUE),
    NETHERITE(Items.NETHERITE_INGOT,"3B393B","nether",ChatFormatting.DARK_GRAY),
    NETHERRACK(Items.NETHERRACK,"723232","nether",ChatFormatting.RED),
    OBSIDIAN(OBSIDIAN_DROPS,"100C1C","nether",ChatFormatting.DARK_GRAY),
    QUARTZ(Items.QUARTZ,"DDD4C6","nether",ChatFormatting.WHITE),
    REFINED_GLOWSTONE("#c:ingots/refined_glowstone","EDBB18","nether",ChatFormatting.GOLD),
    SIGNALUM("#c:ingots/signalum","FB6114","nether",ChatFormatting.RED),
    WITHER(WITHER_DROPS,"282828","nether",ChatFormatting.DARK_GRAY),

    // End
    DIAMOND(Items.DIAMOND,"4BEDE6","end",ChatFormatting.AQUA),
    EMERALD(Items.EMERALD,"17DD62","end",ChatFormatting.GREEN),
    END_STONE(Items.END_STONE,"EAF2B0","end",ChatFormatting.YELLOW),
    ENDER_DRAGON(ENDER_DRAGON_DROPS,"320538","end",ChatFormatting.DARK_GRAY),
    ENDERMAN(Items.ENDER_PEARL,"0B0B0B","end",ChatFormatting.DARK_GRAY),
    PEARLESCENT("#c:ingots/pearlescent","CF62C7","end",ChatFormatting.LIGHT_PURPLE),
    PRISMALITE("#c:ingots/prismalite","A490E2","end",ChatFormatting.LIGHT_PURPLE),
    REFINED_OBSIDIAN("#c:ingots/refined_obsidian","44345A","end",ChatFormatting.DARK_PURPLE),
    //SHULKER(Items.SHULKER_SHELL_/_CHORUS_FRUIT,"","end",ChatFormatting.DARK_PURPLE),


    ;

    private final String item, color,type;
    private final ChatFormatting nameColor;

    DefaultShulkerType(String item, String color, String type, ChatFormatting nameColor) {
        this.item = item;
        this.color = color;
        this.type = type;
        this.nameColor = nameColor;
    }
    DefaultShulkerType(Holder<Item> item, String color, String type, ChatFormatting nameColor) {
        this(item.getRegisteredName(), color, type, nameColor);
    }
    DefaultShulkerType(TagKey<Item> item, String color, String type, ChatFormatting nameColor) {
        this("#"+ item.location(), color, type, nameColor);
    }
    DefaultShulkerType(Item item, String color, String type, ChatFormatting nameColor) {
        this(BuiltInRegistries.ITEM.getKey(item).toString(), color, type, nameColor);
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String toJson(){
        JsonObject obj = new JsonObject();
        obj.addProperty("id",MODID + ":"+name().toLowerCase());
        obj.addProperty("name", "§"+nameColor.getChar()+ModsUtils.getUpperName(name().toLowerCase(),"_"));
        obj.addProperty("item",item);
        obj.addProperty("color","#"+color);
        obj.addProperty("type",MODID + ":"+ type);
        return GSON.toJson(obj);
    }

}
