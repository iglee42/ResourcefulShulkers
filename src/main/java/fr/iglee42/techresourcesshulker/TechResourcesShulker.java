package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.custompack.PackType;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;
import fr.iglee42.techresourcesshulker.custompack.TRSPackFinder;
import fr.iglee42.techresourcesshulker.custompack.generation.*;
import fr.iglee42.techresourcesshulker.events.ClientEvents;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.network.ModMessages;
import fr.iglee42.techresourcesshulker.recipes.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TechResourcesShulker.MODID)
public class TechResourcesShulker {
    public static final String MODID = "techresourcesshulker";
    public static final CreativeModeTab GROUP = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SHULKER_ITEM.get());
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();


    private static boolean hasGenerated;

    public TechResourcesShulker() {
        hasGenerated = false;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientEvents::onTextureStitch);
            bus.addListener(ClientEvents::registerItemColors);
        });
        Types.init();

        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModContent.register(bus);

        ModMessages.register();
        ModRecipes.SERIALIZER.register(bus);

        PathConstant.init();


        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);



        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
        try {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new TRSPackFinder(PackType.RESOURCE));
            }
        } catch (Exception ignored) {
        }
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    public void onServerStart(final ServerAboutToStartEvent event) {
        event.getServer().getPackRepository().addPackFinder(new TRSPackFinder(PackType.DATA));

    }


    public static void generateData() {
        if (!hasGenerated) {
            if (!ModLoader.isLoadingStateValid()) {
                return;
            }
            ModelsGenerator.generate();
            BlockStatesGenerator.generate();
            LangsGenerator.generate();
            TagsGenerator.generate();
            LootTablesGenerator.generate();

            hasGenerated = true;
        }
    }

    public static void injectDatapackFinder(PackRepository resourcePacks) {
        if (DistExecutor.unsafeRunForDist(() -> () -> resourcePacks != Minecraft.getInstance().getResourcePackRepository(), () -> () -> true)) {
            resourcePacks.addPackFinder(new TRSPackFinder(PackType.DATA));
        }
    }



}
