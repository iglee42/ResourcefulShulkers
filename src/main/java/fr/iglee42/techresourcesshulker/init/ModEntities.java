package fr.iglee42.techresourcesshulker.init;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.entity.BaseEssenceShulker;
import fr.iglee42.techresourcesshulker.entity.CustomShulkerBullet;
import fr.iglee42.techresourcesshulker.entity.ResourceShulker;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.item.ShulkerItem;
import fr.iglee42.techresourcesshulker.utils.ShulkerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TechResourcesShulker.MODID);

    public static final RegistryObject<EntityType<BaseEssenceShulker>> OVERWORLD_SHULKER = ENTITIES.register("overworld_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIME), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"overworld_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> SKY_SHULKER = ENTITIES.register("sky_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.LIGHT_BLUE), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"sky_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> NETHER_SHULKER = ENTITIES.register("nether_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.RED), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"nether_shulker").toString()));
    public static final RegistryObject<EntityType<BaseEssenceShulker>> END_SHULKER = ENTITIES.register("end_shulker", ()->EntityType.Builder.<BaseEssenceShulker>of((type, level) -> new BaseEssenceShulker(type,level, DyeColor.YELLOW), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,"end_shulker").toString()));

    public static RegistryObject<EntityType<ResourceShulker>> createShulker(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        RegistryObject<EntityType<ResourceShulker>> entity = ENTITIES.register(res.id().getPath()+"_shulker", ()->EntityType.Builder.<ResourceShulker>of((type1, level) -> new ResourceShulker(type1,level,id), MobCategory.CREATURE).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10).build(new ResourceLocation(TechResourcesShulker.MODID,res.id().getPath()+"_shulker").toString()));
        ModItems.ITEMS.register(res.id().getPath().toLowerCase()+"_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.SHULKERS_GROUP),null));
        return entity;
    }

    public static RegistryObject<EntityType<CustomShulkerBullet>> createBullet(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        return ENTITIES.register(res.id().getPath()+"_shulker_bullet",()->EntityType.Builder.<CustomShulkerBullet>of((type1,level)-> new CustomShulkerBullet(type1,level,res.id()), MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(8).build(new ResourceLocation(TechResourcesShulker.MODID,res.id().getPath()+"_shulker_bullet").toString()));
    }


}
