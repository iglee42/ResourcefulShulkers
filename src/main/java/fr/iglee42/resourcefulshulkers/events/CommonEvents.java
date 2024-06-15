package fr.iglee42.resourcefulshulkers.events;

import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.command.TRSCommand;
import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.init.ModEntities;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.resourcepack.PackType;
import fr.iglee42.resourcefulshulkers.resourcepack.TRSPackFinder;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class CommonEvents {

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD)
    public static class Mod{

        @SubscribeEvent
        public static void registerAttribute(EntityAttributeCreationEvent event) {
            event.put(ModEntities.OVERWORLD_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModEntities.SKY_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModEntities.NETHER_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModEntities.END_SHULKER.get(), CustomShulker.createAttributes().build());
            ShulkersManager.TYPES.forEach(r->{
                event.put(ShulkersManager.ENTITY_TYPES.get(r.id()).get(),CustomShulker.createAttributes().build());
            });
        }


    }

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.FORGE)
    public static class Forge{

        @SubscribeEvent
        public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget().getType() == EntityType.SHULKER && event.getEntity().isCrouching()){
                event.getEntity().addItem(new ItemStack(ModItems.SHULKER_ITEM.get()));
                event.getTarget().remove(Entity.RemovalReason.KILLED);
            }
        }

        @SubscribeEvent
        public static void onServerStart(final ServerAboutToStartEvent event) {
            event.getServer().getPackRepository().addPackFinder(new TRSPackFinder(PackType.DATA));
        }

        @SubscribeEvent
        public static void onWorldTick(TickEvent.LevelTickEvent event) {
            if (event.level.isClientSide) {
                return;
            }
            if (event.phase == TickEvent.Phase.START) {
                return;
            }
            ShulkerAuraManager manager = ShulkerAuraManager.get(event.level);
            manager.tick(event.level);
        }
        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event){
            new TRSCommand(event.getDispatcher());
        }

    }



}
