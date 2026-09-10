package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.registries.RSTags;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class RSLangProvider extends LanguageProvider {
    public RSLangProvider(PackOutput output) {
        super(output, RSIds.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        RSAdvancements.all().forEach(advancement -> {
            add(advancement.titleKey(), advancement.title());
            add(advancement.descriptionKey(), advancement.description());
        });

        add("itemGroup.resourcefulshulkers", "Resourceful Shulkers");
        add("itemGroup.resourcefulshulkers.shulkers", "Resourceful Shulkers - Customs");

        add(RSBlocks.PURPUR_TARGET.get(), "Purpur Target");
        add(RSBlocks.SHULKER_ABSORBER.get(), "Shulker Absorber");
        add(RSBlocks.SHULKER_INFUSER.get(), "Shulker Infuser");
        add(RSBlocks.SHULKER_PEDESTAL.get(), "Shulker Pedestal");
        add(RSBlocks.SHULKER_HEAD.get(), "Shulker Head");
        add(RSBlocks.END_CITY.get(), "End City");
        add(RSBlocks.END_CITY_TIER_2.get(), "End City Tier 2");
        add(RSBlocks.TRAINER.get(), "Shulker Trainer");
        add(RSBlocks.STRUCTURE.get(), "Structure Block");

        add(RSItems.BASE_ESSENCE.get(), "Base Essence");
        add(RSItems.UPGRADE_BASE.get(), "Upgrade Base");
        add(RSItems.DURABILITY_UPGRADE.get(), "Durability Upgrade");
        add(RSItems.QUANTITY_UPGRADE.get(), "Quantity Upgrade");
        add(RSItems.SPEED_UPGRADE.get(), "Speed Upgrade");
        add(RSItems.SHELL_UPGRADE.get(), "Shell Upgrade");

        add("gui.resourcefulshulkers.generating", "Generating: %s");
        add("gui.resourcefulshulkers.resource_not_found", "Resource Not Found !");
        add("gui.resourcefulshulkers.no_acceleration", "You cannot accelerate this block");
        add("gui.resourcefulshulkers.durability", "Durability: %s/%s");
        add("gui.resourcefulshulkers.reload", "Reload Needed");
        add("gui.resourcefulshulkers.choose_item", "Choose the generated item");

        add("tooltip.resourcefulshulkers.press_shift", "Press _Shift_ for more information");
        add("tooltip.resourcefulshulkers.type", "Type: %s");
        add("tooltip.resourcefulshulkers.head", "Try it on your head !");
        add("tooltip.resourcefulshulkers.shulker_pickup", "_Shulkers_ can be picked up with _sneak + right click_");
        add("tooltip.resourcefulshulkers.speed_upgrade", "Increases _Speed_");
        add("tooltip.resourcefulshulkers.durability_upgrade", "Reduces the chance of _durability loss_ by _%s%%_");
        add("tooltip.resourcefulshulkers.quantity_upgrade", "Increases the _amount_ of items generated");
        add("tooltip.resourcefulshulkers.shell_upgrade", "Increases the amount of _durability_ given when repearing a box");

        add("tooltip.resourcefulshulkers.purpur_target","_Resource Shulkers_ will _automatically target_ this block");
        add("tooltip.resourcefulshulkers.purpur_target.1","_Shulker Bullets_ that hit this block will _drop_ a _shell_");
        add("tooltip.resourcefulshulkers.purpur_target.2","Can be _targeted_ by only _one_ shulker at the same time");

        add("tooltip.resourcefulshulkers.shulker_infuser","_Right click_ to _start_ a craft");
        add("tooltip.resourcefulshulkers.shulker_infuser.1","_Sneak + Right Click_ to _display_ the position of _pedestals_");
        add("tooltip.resourcefulshulkers.shulker_infuser.2","Consumes _aura_ to _keep_ shulker with _no AI_");

        add("tooltip.resourcefulshulkers.shulker_absorber", "Place a _shulker_ on it to generate _aura_");
        add("tooltip.resourcefulshulkers.shulker_absorber.1", "A _shulker_ produce _%s aura_");

        add("tooltip.resourcefulshulkers.end_city", "A powerful block that can hold _%s shulkers_ and _generate_ items");
        add("tooltip.resourcefulshulkers.shulker_trainer", "A powerful block that can hold _%s shulkers_ and _help_ them to produce shells");

        add("message.resoucefulshulkers.obstructed_block", "Position obstructed by block ! Clear a 3x3x3 area.");
        add("message.resoucefulshulkers.obstructed_entity", "Position obstructed by entity ! Clear a 3x3x3 area.");
    }
}
