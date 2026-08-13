package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class RSTags {

    private RSTags() {}

    public static class Items {

        public static final TagKey<Item> BEE_DROPS = key("drops/bee");
        public static final TagKey<Item> CHICKEN_DROPS = key("drops/chicken");
        public static final TagKey<Item> COW_DROPS = key("drops/cow");
        public static final TagKey<Item> FISH_DROPS = key("drops/fish");
        public static final TagKey<Item> FROG_DROPS = key("drops/frog");
        public static final TagKey<Item> RABBIT_DROPS = key("drops/rabbit");
        public static final TagKey<Item> SHEEP_DROPS = key("drops/sheep");
        public static final TagKey<Item> SPIDER_DROPS = key("drops/spider");
        public static final TagKey<Item> ZOMBIE_DROPS = key("drops/zombie");
        public static final TagKey<Item> CREEPER_DROPS = key("drops/creeper");
        public static final TagKey<Item> SKELETON_DROPS = key("drops/skeleton");
        public static final TagKey<Item> WARDEN_DROPS = key("drops/warden");
        public static final TagKey<Item> OBSIDIAN_DROPS = key("drops/obsidian");
        public static final TagKey<Item> WITHER_DROPS = key("drops/wither");
        public static final TagKey<Item> ENDER_DRAGON_DROPS = key("drops/ender_dragon");
        public static final TagKey<Item> GUARDIAN_DROPS = key("drops/guardian");
        public static final TagKey<Item> XP_DROPS = key("drops/xp");

        public static final TagKey<Item> ESSENCES = key("essences");
        public static final TagKey<Item> ELEMENTAL_SHULKERS = key("shulkers/elemental");
        public static final TagKey<Item> RESOURCE_SHULKERS = key("shulkers/resource");
        public static final TagKey<Item> SHULKERS = key("shulkers");
        public static final TagKey<Item> SHULKER_SHELLS = key("shulker_shells");
        public static final TagKey<Item> RESOURCE_SHULKER_SHELLS = key("shulker_shells/resource");



        public static TagKey<Item> key(String path){
            return TagKey.create(Registries.ITEM, RSIds.id(path));
        }

        public static TagKey<Item> common(String path){
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c",path));
        }
    }

    public static class Blocks {

        public static final TagKey<Block> GENERATING_BOXES = key("generating_boxes");


        public static TagKey<Block> key(String path){
            return TagKey.create(Registries.BLOCK, RSIds.id(path));
        }

        public static TagKey<Block> common(String path){
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c",path));
        }
    }

    public static class Entities {

        public static final TagKey<EntityType<?>> ELEMENTAL_SHULKERS = key("shulkers/elemental");
        public static final TagKey<EntityType<?>> RESOURCE_SHULKERS = key("shulkers/resource");
        public static final TagKey<EntityType<?>> SHULKERS = key("shulkers");



        public static TagKey<EntityType<?>> key(String path){
            return TagKey.create(Registries.ENTITY_TYPE, RSIds.id(path));
        }

        public static TagKey<EntityType<?>> common(String path){
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c",path));
        }
    }
}
