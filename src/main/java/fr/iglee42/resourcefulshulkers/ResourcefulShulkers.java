package fr.iglee42.resourcefulshulkers;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.JsonOps;
import fr.iglee42.igleelib.IgleeLibrary;
import fr.iglee42.resourcefulshulkers.init.*;
import fr.iglee42.resourcefulshulkers.recipes.ModRecipes;
import fr.iglee42.resourcefulshulkers.resourcepack.PackType;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.resourcepack.RSPackFinder;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ResourcefulShulkers.MODID)
public class ResourcefulShulkers {
    public static final String MODID = "resourcefulshulkers";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final MutableComponent PREFIX = Component.literal("[").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal("ResourcefulShulkers").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal("] ").withStyle(ChatFormatting.DARK_PURPLE));



    public ResourcefulShulkers(IEventBus bus, ModContainer container) {
        IgleeLibrary.addClassParser(Ingredient.class,je-> {
            if (je.isJsonPrimitive() && je.getAsJsonPrimitive().isString()){
                String item = je.getAsString();
                if (item.startsWith("#")){
                    TagKey<Item> key = TagKey.create(Registries.ITEM,ResourceLocation.parse(item.substring(1)));
                    return Ingredient.of(key);
                } else
                    return Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(je.getAsString())));
            }
            return Ingredient.CODEC_NONEMPTY.parse(JsonOps.INSTANCE,je).getOrThrow();
        });
        TypesManager.init();
        ShulkersManager.init();

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlockEntities.MENUS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModComponents.COMPONENTS.register(bus);

        ModRecipes.SERIALIZER.register(bus);

        PathConstant.init();

        bus.addListener(this::commonSetup);
        bus.addListener(ModCreativeTabs::addCreative);

        container.registerConfig(ModConfig.Type.COMMON,ResourcefulShulkersConfig.SPEC,"resourcefulshulkers/common.toml");
        container.registerConfig(ModConfig.Type.CLIENT,ResourcefulShulkersConfig.Client.SPEC,"resourcefulshulkers/common.toml");

        try {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Minecraft.getInstance().getResourcePackRepository().addPackFinder(new RSPackFinder(PackType.RESOURCE));
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
