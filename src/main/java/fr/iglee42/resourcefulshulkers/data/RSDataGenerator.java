package fr.iglee42.resourcefulshulkers.data;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.data.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RSIds.MODID)
public class RSDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        event.addProvider(new RSItemTagsProvider(output, registries, helper));
        event.addProvider(new RSEntityTagsProvider(output, registries, helper));
        event.addProvider(new RSBlockStatesProvider(output, helper));
        event.addProvider(new RSItemModelProvider(output, helper));
        event.addProvider(new RSLangProvider(output));
        event.addProvider(new RSLootTableProvider(output,registries));
        event.addProvider(new RSRecipeProvider(output,registries));
        event.addProvider(new AdvancementProvider(output, registries, helper, List.of(new RSAdvancementProvider())));
    }

}
