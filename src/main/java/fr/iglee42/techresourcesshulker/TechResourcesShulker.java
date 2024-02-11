package fr.iglee42.techresourcesshulker;

import com.google.common.collect.ImmutableSet;
import fr.iglee42.techresourcesshulker.init.ModBlockEntities;
import fr.iglee42.techresourcesshulker.init.ModEntities;
import fr.iglee42.techresourcesshulker.utils.ShulkersManager;
import fr.iglee42.techresourcesshulker.resourcepack.PackType;
import fr.iglee42.techresourcesshulker.resourcepack.PathConstant;
import fr.iglee42.techresourcesshulker.resourcepack.TRSPackFinder;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.network.ModMessages;
import fr.iglee42.techresourcesshulker.recipes.ModRecipes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
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

@Mod(TechResourcesShulker.MODID)
public class TechResourcesShulker {
    public static final String MODID = "techresourcesshulker";
    public static final CreativeModeTab GROUP = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.OVERWORLD_ESSENCE.get());
        }
    };

    public static final CreativeModeTab SHULKERS_GROUP = new CreativeModeTab(MODID+
            ".shulkers") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SHULKER_ITEM.get());
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();

    public static final MutableComponent PREFIX = new TextComponent("[").withStyle(ChatFormatting.DARK_PURPLE).append(new TextComponent("TechResourcesShulker").withStyle(ChatFormatting.LIGHT_PURPLE)).append(new TextComponent("] ").withStyle(ChatFormatting.DARK_PURPLE));



    public TechResourcesShulker() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ShulkersManager.init();

        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlockEntities.MENUS.register(bus);
        ModEntities.ENTITIES.register(bus);

        ModMessages.register();
        ModRecipes.SERIALIZER.register(bus);

        PathConstant.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

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
