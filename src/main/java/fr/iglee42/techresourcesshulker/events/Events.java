package fr.iglee42.techresourcesshulker.events;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.techresourcesshulker.recipes.ShulkerRecipeEnvironnement;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class Events {

    @EventBusSubscriber(modid = TechResourcesShulker.MODID,bus = EventBusSubscriber.Bus.MOD)
    public static class Mod{

        @SubscribeEvent
        public static void registerAttribute(EntityAttributeCreationEvent event) {
            event.put(ModContent.OVERWORLD_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.SKY_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.NETHER_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.END_SHULKER.get(), CustomShulker.createAttributes().build());
            Types.TYPES.forEach(r->{
                event.put(Types.ENTITY_TYPES.get(r.id()).get(),CustomShulker.createAttributes().build());
            });
        }
        @SubscribeEvent
        public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, ShulkerRecipeEnvironnement.Type.ID, ShulkerRecipeEnvironnement.Type.INSTANCE);
            Registry.register(Registry.RECIPE_TYPE, ShulkerItemInfusionRecipe.Type.ID, ShulkerItemInfusionRecipe.Type.INSTANCE);
        }
    }

    @EventBusSubscriber(modid = TechResourcesShulker.MODID,bus = EventBusSubscriber.Bus.FORGE)
    public static class Forge{


    }

}
