package fr.iglee42.resourcefulshulkers;

import fr.iglee42.resourcefulshulkers.config.RSClientConfig;
import fr.iglee42.resourcefulshulkers.config.RSCommonConfig;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.registries.RSRecipes;
import fr.iglee42.resourcefulshulkers.registries.*;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RSIds.MODID)
public class ResourcefulShulkers {

    public static final Logger LOGGER = LogManager.getLogger("Resourceful Shulkers");


    public ResourcefulShulkers(IEventBus bus, ModContainer container) {
        TypesManager.load();
        ShulkersManager.load();

        RSItems.ITEMS.register(bus);
        RSBlocks.BLOCKS.register(bus);
        RSCreativeTabs.CREATIVE_TABS.register(bus);
        RSBlockEntities.BLOCK_ENTITIES.register(bus);
        RSBlockEntities.MENUS.register(bus);
        RSEntities.ENTITIES.register(bus);
        RSDataComponents.COMPONENTS.register(bus);

        RSRecipes.SERIALIZER.register(bus);

        PathConstant.init();

        bus.addListener(RSCreativeTabs::addCreative);

        container.registerConfig(ModConfig.Type.CLIENT, RSClientConfig.SPEC,"resourcefulshulkers/client.toml");
        container.registerConfig(ModConfig.Type.COMMON, RSCommonConfig.SPEC,"resourcefulshulkers/common.toml");
        container.registerConfig(ModConfig.Type.SERVER, RSServerConfig.SPEC,"resourcefulshulkers/server.toml");

    }

}
