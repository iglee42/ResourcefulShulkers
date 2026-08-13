package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RSItemModelProvider extends ItemModelProvider {
    public RSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RSIds.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(RSBlocks.SHULKER_PEDESTAL.get());
        simpleBlockItem(RSBlocks.SHULKER_INFUSER.get());
        simpleBlockItem(RSBlocks.SHULKER_ABSORBER.get());
        simpleBlockItem(RSBlocks.PURPUR_TARGET.get());

        basicItem(RSItems.BASE_ESSENCE.get());
        basicItem(RSItems.UPGRADE_BASE.get());
        basicItem(RSItems.DURABILITY_UPGRADE.get());
        basicItem(RSItems.QUANTITY_UPGRADE.get());
        basicItem(RSItems.SPEED_UPGRADE.get());
        basicItem(RSItems.SHELL_UPGRADE.get());
    }
}
