package fr.iglee42.techresourcesshulker.events;

import fr.iglee42.techresourcesshulker.init.ModEntities;
import fr.iglee42.techresourcesshulker.command.TRSCommand;
import fr.iglee42.techresourcesshulker.utils.ShulkersManager;
import fr.iglee42.techresourcesshulker.resourcepack.PackType;
import fr.iglee42.techresourcesshulker.resourcepack.TRSPackFinder;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.techresourcesshulker.recipes.ShulkerRecipeEnvironnement;
import fr.iglee42.techresourcesshulker.aura.ShulkerAuraManager;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

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
        public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, ShulkerRecipeEnvironnement.Type.ID, ShulkerRecipeEnvironnement.Type.INSTANCE);
            Registry.register(Registry.RECIPE_TYPE, ShulkerItemInfusionRecipe.Type.ID, ShulkerItemInfusionRecipe.Type.INSTANCE);
        }


    }

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.FORGE)
    public static class Forge{

        @SubscribeEvent
        public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget().getType() == EntityType.SHULKER && event.getPlayer().isCrouching()){
                event.getPlayer().addItem(new ItemStack(ModItems.SHULKER_ITEM.get()));
                event.getTarget().remove(Entity.RemovalReason.KILLED);
            }
        }

        @SubscribeEvent
        public static void onServerStart(final ServerAboutToStartEvent event) {
            event.getServer().getPackRepository().addPackFinder(new TRSPackFinder(PackType.DATA));
        }

        @SubscribeEvent
        public static void onWorldTick(TickEvent.WorldTickEvent event) {
            if (event.world.isClientSide) {
                return;
            }
            if (event.phase == TickEvent.Phase.START) {
                return;
            }
            ShulkerAuraManager manager = ShulkerAuraManager.get(event.world);
            manager.tick(event.world);
        }
        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event){
            new TRSCommand(event.getDispatcher());
        }

    }



}
