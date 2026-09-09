package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.entity.shulkers.CustomShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulkerBullet;
import fr.iglee42.resourcefulshulkers.entity.shulkers.TypeShulker;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@EventBusSubscriber(modid = MODID)
public class RSEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    //Shulkers Methods
    public static Supplier<EntityType<? extends ResourceShulker>> createResourceShulker(IShulkerDefinition shulker){
        return ENTITIES.register(shulker.id().getPath()+"_shulker", ()->
                EntityType.Builder.<ResourceShulker>of((t, lvl) -> new ResourceShulker(t,lvl,shulker), MobCategory.CREATURE)
                        .fireImmune()
                        .canSpawnFarFromPlayer()
                        .sized(1.0F, 1.0F)
                        .clientTrackingRange(10)
                        .build(RSIds.id(shulker.id().getPath()+"_shulker").toString()));
    }

    public static Supplier<EntityType<? extends ResourceShulkerBullet>> createResourceBullet(IShulkerDefinition shulker){
        return ENTITIES.register(shulker.id().getPath() + "_shulker_bullet", ()->
                EntityType.Builder.<ResourceShulkerBullet>of((t,lvl)-> new ResourceShulkerBullet(t,lvl,shulker),MobCategory.MISC)
                        .sized(0.3125F, 0.3125F)
                        .clientTrackingRange(8)
                        .build(RSIds.id(shulker.id().getPath()+"_shulker_bullet").toString())
                );
    }

    //Types Methods
    public static Supplier<EntityType<? extends TypeShulker>> createTypeShulker(ITypeDefinition type){
        return ENTITIES.register(type.id().getPath()+"_shulker", ()->
                EntityType.Builder.<TypeShulker>of((t, lvl) -> new TypeShulker(t,lvl,type), MobCategory.CREATURE)
                        .fireImmune()
                        .canSpawnFarFromPlayer()
                        .sized(1.0F, 1.0F)
                        .clientTrackingRange(10)
                        .build(RSIds.id(type.id().getPath()+"_shulker").toString()));
    }

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        TypesManager.forEachType(t->{
            if (t.entityType() != null) {
                event.put(t.entityType().get(), CustomShulker.createAttributes().build());
            }
        });
        ShulkersManager.forEachShulker(s->{
            if (s.entityType() != null) {
                event.put(s.entityType().get(), CustomShulker.createAttributes().build());
            }
        });
    }

}
