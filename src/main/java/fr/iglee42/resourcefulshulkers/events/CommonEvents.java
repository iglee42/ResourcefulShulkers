package fr.iglee42.resourcefulshulkers.events;

import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.command.TRSCommand;
import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModEntities;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.resourcepack.PackType;
import fr.iglee42.resourcefulshulkers.resourcepack.TRSPackFinder;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.Utils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

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
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event){
            event.registerBlock(Capabilities.ItemHandler.BLOCK,
                    ((level, pos, state, be, context) -> !Utils.isSideDirection(context) ? ((GeneratingBoxBlockEntity)be).getInventory() : ((GeneratingBoxBlockEntity)be).getUpgrades()),
                    ModBlocks.getAllBox());
        }

    }

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME)
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
        public static void onWorldTick(LevelTickEvent.Post event) {
            if (event.getLevel().isClientSide) {
                return;
            }

            ShulkerAuraManager manager = ShulkerAuraManager.get(event.getLevel());
            manager.tick(event.getLevel());
        }
        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event){
            new TRSCommand(event.getDispatcher());
        }



    }



}
