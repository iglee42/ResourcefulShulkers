package fr.iglee42.resourcefulshulkers.events;

import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.resourcepack.RSPackFinder;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.Type;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class CommonEvents {

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD)
    public static class Mod{

        @SubscribeEvent
        public static void registerAttribute(EntityAttributeCreationEvent event) {
            //event.put(ModEntities.OVERWORLD_SHULKER.get(), CustomShulker.createAttributes().build());
            //event.put(ModEntities.SKY_SHULKER.get(), CustomShulker.createAttributes().build());
            //event.put(ModEntities.NETHER_SHULKER.get(), CustomShulker.createAttributes().build());
            //event.put(ModEntities.END_SHULKER.get(), CustomShulker.createAttributes().build());
            TypesManager.TYPES.stream().filter(Type::shouldCreateEntity).forEach(t->{
                event.put(TypesManager.ENTITY_TYPES.get(t.id()).get(),  CustomShulker.createAttributes().build());
            });
            ShulkersManager.TYPES.forEach(r->{
                event.put(ShulkersManager.ENTITY_TYPES.get(r.id()).get(),CustomShulker.createAttributes().build());
            });
        }

        @SubscribeEvent
        public static void registerPackRepo(AddPackFindersEvent event){
            if (event.getPackType() == PackType.CLIENT_RESOURCES) event.addRepositorySource(new RSPackFinder(fr.iglee42.resourcefulshulkers.resourcepack.PackType.RESOURCE));
            else event.addRepositorySource(new RSPackFinder(fr.iglee42.resourcefulshulkers.resourcepack.PackType.DATA));
        }


    }

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME)
    public static class Forge{

        @SubscribeEvent
        public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget().getType() == EntityType.SHULKER && event.getEntity().isCrouching()){
                event.getEntity().addItem(new ItemStack(RSItems.SHULKER.get()));
                event.getTarget().remove(Entity.RemovalReason.KILLED);
            }
        }


    }



}
