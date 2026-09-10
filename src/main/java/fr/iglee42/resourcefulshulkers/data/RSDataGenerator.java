package fr.iglee42.resourcefulshulkers.data;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.data.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = RSIds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RSDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        generator.addProvider(event.includeServer(),new RSItemTagsProvider(output, registries, helper));
        generator.addProvider(event.includeServer(),new RSEntityTagsProvider(output, registries, helper));
        generator.addProvider(event.includeClient(), new RSBlockStatesProvider(output, helper));
        generator.addProvider(event.includeClient(), new RSItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new RSLangProvider(output));
        generator.addProvider(event.includeServer(),new RSLootTableProvider(output));
        generator.addProvider(event.includeServer(),new RSRecipeProvider(output));
        generator.addProvider(event.includeServer(),new ForgeAdvancementProvider(output, registries, helper, List.of(new RSAdvancementProvider())));
    }

}
