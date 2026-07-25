package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.entity.TypeShulker;
import fr.iglee42.resourcefulshulkers.entity.CustomShulkerBullet;
import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.item.shulker.ShulkerItem;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.Type;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    //public static final RegistryObject<EntityType<TypeShulker>> OVERWORLD_SHULKER = ENTITIES.register("overworld_shulker", ()->EntityType.Builder.<TypeShulker>of((type, level) -> new TypeShulker(type,level, DyeColor.LIME), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"overworld_shulker").toString()));
    //public static final RegistryObject<EntityType<TypeShulker>> SKY_SHULKER = ENTITIES.register("sky_shulker", ()->EntityType.Builder.<TypeShulker>of((type, level) -> new TypeShulker(type,level, DyeColor.LIGHT_BLUE), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"sky_shulker").toString()));
    //public static final RegistryObject<EntityType<TypeShulker>> NETHER_SHULKER = ENTITIES.register("nether_shulker", ()->EntityType.Builder.<TypeShulker>of((type, level) -> new TypeShulker(type,level, DyeColor.RED), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"nether_shulker").toString()));
    //public static final RegistryObject<EntityType<TypeShulker>> END_SHULKER = ENTITIES.register("end_shulker", ()->EntityType.Builder.<TypeShulker>of((type, level) -> new TypeShulker(type,level, DyeColor.YELLOW), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"end_shulker").toString()));

    /*public static DeferredHolder<EntityType<?>,EntityType<ResourceShulker>> createShulker(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        DeferredHolder<EntityType<?>,EntityType<ResourceShulker>> entity = ENTITIES.register(res.id().getPath()+"_shulker", ()->EntityType.Builder.<ResourceShulker>of((type1, level) -> new ResourceShulker(type1,level,id), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(MODID,res.id().getPath()+"_shulker").toString()));
        RSItems.ITEMS.register(res.id().getPath().toLowerCase()+"_shulker", () -> new ShulkerItem(new Item.Properties(),null,res.type()));
        return entity;
    }

    public static DeferredHolder<EntityType<?>,EntityType<TypeShulker>> createTypeShulker(ResourceLocation id){
        Type res = Type.getById(id);
        DeferredHolder<EntityType<?>,EntityType<TypeShulker>> entity = ENTITIES.register(res.id().getPath()+"_shulker", ()->EntityType.Builder.<TypeShulker>of((type1, level) -> new TypeShulker(type1,level,id), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath(MODID,res.id().getPath()+"_shulker").toString()));
        RSItems.ITEMS.register(res.id().getPath().toLowerCase()+"_shulker", () -> new ShulkerItem(new Item.Properties(),null,Type.getById(ResourceLocation.fromNamespaceAndPath(MODID,"elemental")).id()));
        return entity;
    }

    public static DeferredHolder<EntityType<?>,EntityType<CustomShulkerBullet>> createBullet(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        return ENTITIES.register(res.id().getPath()+"_shulker_bullet",()->EntityType.Builder.<CustomShulkerBullet>of((type1,level)-> new CustomShulkerBullet(type1,level,res.id()), MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(8).build(ResourceLocation.fromNamespaceAndPath(MODID,res.id().getPath()+"_shulker_bullet").toString()));
    }*/


}
