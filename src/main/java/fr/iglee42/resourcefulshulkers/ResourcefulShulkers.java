package fr.iglee42.resourcefulshulkers;

import com.google.common.collect.ImmutableSet;
import fr.iglee42.resourcefulshulkers.init.*;
import fr.iglee42.resourcefulshulkers.network.ModMessages;
import fr.iglee42.resourcefulshulkers.recipes.ModRecipes;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ResourcefulShulkers.MODID)
public class ResourcefulShulkers {
    public static final String MODID = "resourcefulshulkers";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final MutableComponent PREFIX = Component.literal("[").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal("ResourcefulShulkers").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal("] ").withStyle(ChatFormatting.DARK_PURPLE));



    public ResourcefulShulkers() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        TypesManager.init();
        ShulkersManager.init();

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlockEntities.MENUS.register(bus);
        ModEntities.ENTITIES.register(bus);

        ModMessages.register();
        ModRecipes.SERIALIZER.register(bus);

        PathConstant.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModCreativeTabs::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
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
