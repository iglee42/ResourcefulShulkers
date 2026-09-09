package fr.iglee42.resourcefulshulkers.advancements;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RSAdvancements {

    public static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/block/purpur_block.png");

    private static final List<RSAdvancement> ALL = new ArrayList<>();

    public static final RSAdvancement ROOT = register(RSAdvancement.builder("root")
            .icon(RSItems.SHULKER)
            .title("Resourceful Shulkers")
            .description("Root advancement")
            .background(BACKGROUND)
            .toast(false)
            .announce(false)
            .criterion("shulker", InventoryChangeTrigger.TriggerInstance.hasItems(RSItems.SHULKER.get())));

    public static final RSAdvancement AURA = register(RSAdvancement.builder("aura")
            .parent(ROOT)
            .icon(RSBlocks.SHULKER_ABSORBER)
            .title("Why is this named aura ?")
            .description("Create shulker aura with a shulker absorber"));

    public static final RSAdvancement ITEM_INFUSION = register(RSAdvancement.builder("item_infusion")
            .parent(AURA)
            .icon(RSBlocks.PURPUR_TARGET)
            .title("The power of infusion")
            .description("Use the item mode of the shulker infuser"));

    public static final RSAdvancement ENVIRONMENT_INFUSION = register(RSAdvancement.builder("environment_infusion")
            .parent(AURA)
            .icon(RSIds.id("overworld_shulker"), RSItems.SHULKER)
            .title("You suck the world")
            .description("Use the environment mode of the shulker infuser"));

    public static final RSAdvancement HEAD = register(RSAdvancement.builder("head")
            .parent(AURA)
            .icon(RSItems.SHULKER_HEAD)
            .title("T'as la grosse tête")
            .description("Wear a shulker head")
            .hidden(true));

    public static final RSAdvancement TRAINER = register(RSAdvancement.builder("shulker_trainer")
            .parent(ITEM_INFUSION)
            .icon(RSBlocks.TRAINER)
            .title("Do you think they eat eggs?")
            .description("Create a shulker trainer with the shulker infuser")
            .frame(AdvancementType.GOAL)
            .criterion("craft_shulker_trainer", InventoryChangeTrigger.TriggerInstance.hasItems(RSBlocks.TRAINER.get())));

    public static final RSAdvancement END_CITY = register(RSAdvancement.builder("end_city")
            .parent(ITEM_INFUSION)
            .icon(RSBlocks.END_CITY)
            .title("The City at the End of the mod?")
            .description("Create an end city with the shulker infuser")
            .frame(AdvancementType.GOAL)
            .criterion("craft_end_city", InventoryChangeTrigger.TriggerInstance.hasItems(RSBlocks.END_CITY.get())));

    public static final RSAdvancement END_CITY_2 = register(RSAdvancement.builder("end_city_2")
            .parent(END_CITY)
            .icon(RSBlocks.END_CITY_TIER_2)
            .title("Too much resources !")
            .description("Craft a tier 2 end city with the shulker infuser")
            .frame(AdvancementType.CHALLENGE)
            .hidden(true)
            .criterion("craft_end_city", InventoryChangeTrigger.TriggerInstance.hasItems(RSBlocks.END_CITY_TIER_2.get())));

    private RSAdvancements() {}

    public static List<RSAdvancement> all() {
        return Collections.unmodifiableList(ALL);
    }

    private static RSAdvancement register(RSAdvancement.Builder builder) {
        RSAdvancement advancement = builder.build();
        ALL.add(advancement);
        return advancement;
    }
}
