package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.recipes.ShulkerEnvironnementRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

public class Events {

    @EventBusSubscriber(modid = TechResourcesShulker.MODID,bus = EventBusSubscriber.Bus.MOD)
    public static class Mod{
        @SubscribeEvent
        public static void registerAttribute(EntityAttributeCreationEvent event) {
            event.put(ModContent.OVERWORLD_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.SKY_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.NETHER_SHULKER.get(), CustomShulker.createAttributes().build());
            event.put(ModContent.END_SHULKER.get(), CustomShulker.createAttributes().build());
        }

        @SubscribeEvent
        public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, ShulkerEnvironnementRecipe.Type.ID,ShulkerEnvironnementRecipe.Type.INSTANCE);
        }
    }

}
