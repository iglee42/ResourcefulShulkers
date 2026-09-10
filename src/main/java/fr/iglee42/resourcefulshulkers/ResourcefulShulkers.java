package fr.iglee42.resourcefulshulkers;

import fr.iglee42.resourcefulshulkers.config.RSClientConfig;
import fr.iglee42.resourcefulshulkers.config.RSCommonConfig;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.network.RSPayloads;
import fr.iglee42.resourcefulshulkers.registries.RSRecipes;
import fr.iglee42.resourcefulshulkers.registries.*;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RSIds.MODID)
public class ResourcefulShulkers {

    public static final Logger LOGGER = LogManager.getLogger("Resourceful Shulkers");


    public ResourcefulShulkers(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        TypesManager.load();
        ShulkersManager.load();

        RSItems.ITEMS.register(bus);
        RSBlocks.BLOCKS.register(bus);
        RSCreativeTabs.CREATIVE_TABS.register(bus);
        RSBlockEntities.BLOCK_ENTITIES.register(bus);
        RSBlockEntities.MENUS.register(bus);
        RSEntities.ENTITIES.register(bus);

        RSRecipes.SERIALIZER.register(bus);

        RSPayloads.register();

        PathConstant.init();

        bus.addListener(RSCreativeTabs::addCreative);

        context.registerConfig(ModConfig.Type.CLIENT, RSClientConfig.SPEC,"resourcefulshulkers/client.toml");
        context.registerConfig(ModConfig.Type.COMMON, RSCommonConfig.SPEC,"resourcefulshulkers/common.toml");
        context.registerConfig(ModConfig.Type.SERVER, RSServerConfig.SPEC,"resourcefulshulkers/server.toml");

    }

}
