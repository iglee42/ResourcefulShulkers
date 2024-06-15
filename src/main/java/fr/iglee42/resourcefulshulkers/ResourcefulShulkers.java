package fr.iglee42.resourcefulshulkers;

import com.google.common.collect.ImmutableSet;
import fr.iglee42.resourcefulshulkers.events.CommonEvents;
import fr.iglee42.resourcefulshulkers.init.*;
import fr.iglee42.resourcefulshulkers.init.ModRecipes;
import fr.iglee42.resourcefulshulkers.resourcepack.PackType;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.resourcepack.TRSPackFinder;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ResourcefulShulkers.MODID)
public class ResourcefulShulkers {
    public static final String MODID = "resourcefulshulkers";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final MutableComponent PREFIX = Component.literal("[").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal("ResourcefulShulkers").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal("] ").withStyle(ChatFormatting.DARK_PURPLE));



    public ResourcefulShulkers(IEventBus bus, ModContainer container) {
        TypesManager.init();
        ShulkersManager.init();

        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlockEntities.MENUS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);

        ModDataComponents.COMPONENTS.register(bus);
        ModRecipes.RECIPES.register(bus);
        ModRecipes.SERIALIZER.register(bus);

        ModRecipeSerializers.RECIPES.register(bus);

        PathConstant.init();

        bus.addListener(this::commonSetup);
        bus.addListener(ModCreativeTabs::addCreative);

        //NeoForge.EVENT_BUS.register(this);
        try {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new TRSPackFinder(PackType.RESOURCE));
            }
        } catch (Exception ignored) {
        }
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        //Code from Tinker's Construct
        event.enqueueWork(() -> {
            ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
            builder.addAll(BlockEntityType.SKULL.validBlocks);
            builder.add(ModBlocks.SHULKER_HEAD.get(),ModBlocks.WALL_SHULKER_HEAD.get());
            BlockEntityType.SKULL.validBlocks = builder.build();
        });
    }



}
