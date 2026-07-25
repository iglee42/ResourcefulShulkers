package fr.iglee42.resourcefulshulkers;

import fr.iglee42.resourcefulshulkers.init.*;
import fr.iglee42.resourcefulshulkers.recipes.ModRecipes;
import fr.iglee42.resourcefulshulkers.registries.RSCreativeTabs;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.resourcepack.PackType;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.resourcepack.RSPackFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ResourcefulShulkers.MODID)
public class ResourcefulShulkers {
    public static final String MODID = "resourcefulshulkers";

    public static final Logger LOGGER = LogManager.getLogger("Resourceful Shulkers");

    public static final MutableComponent PREFIX = Component.literal("[").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal("ResourcefulShulkers").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal("] ").withStyle(ChatFormatting.DARK_PURPLE));



    public ResourcefulShulkers(IEventBus bus, ModContainer container) {
        fr.iglee42.resourcefulshulkers.types.TypesManager.load();
        fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager.load();
        //TypesManager.init();

        RSItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        RSCreativeTabs.CREATIVE_TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlockEntities.MENUS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModComponents.COMPONENTS.register(bus);

        ModRecipes.SERIALIZER.register(bus);

        PathConstant.init();

        bus.addListener(RSCreativeTabs::addCreative);

        container.registerConfig(ModConfig.Type.COMMON,ResourcefulShulkersConfig.SPEC,"resourcefulshulkers/common.toml");
        container.registerConfig(ModConfig.Type.CLIENT,ResourcefulShulkersConfig.Client.SPEC,"resourcefulshulkers/client.toml");

        try {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new RSPackFinder(PackType.RESOURCE));
            }
        } catch (Exception ignored) {
        }
    }

}
