package fr.iglee42.resourcefulshulkers.shulkers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSEntities;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.function.Consumer;

public final class ShulkersManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final TreeMap<ResourceLocation, RegisteredShulker> SHULKERS = new TreeMap<>(Comparator.comparing(ResourceLocation::getPath).thenComparing(ResourceLocation::getNamespace));

    private static final Codec<ShulkerDefinition> VALIDATED_CODEC = ShulkerDefinition.CODEC.validate(
            def->{
                if (exists(def.id())) return DataResult.error(()->"Shulker already exists with id: " + def.id());
                if (!TypesManager.exists(def.typeId())) return DataResult.error(()->"Type does not exist: " + def.typeId());
                if (def.typeId().equals(TypesManager.getElementalType().id())) return DataResult.error(()->"Shulker cannot have elemental type !");
                return DataResult.success(def);
            }
    );

    private ShulkersManager() {}

    public static void load(){
        SHULKERS.clear();
        File shulkersDirectory = FMLPaths.CONFIGDIR.get().resolve(RSIds.MODID + "/shulkers/").toFile();
        if (!shulkersDirectory.exists()) {
            createShulkerDirectory(shulkersDirectory);
        }
        File[] files = shulkersDirectory.listFiles((file) -> file.getName().endsWith(".json"));
        if (files == null) {
            ResourcefulShulkers.LOGGER.error("Failed to list files in shulkers directory at: {}", shulkersDirectory.getAbsolutePath());
            return;
        }
        for (File file : files){
            try (FileInputStream inputStream = new FileInputStream(file); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)){
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                DataResult<ShulkerDefinition> parseResult = VALIDATED_CODEC.parse(JsonOps.INSTANCE, json);
                if (parseResult.error().isPresent()) throw new JsonParseException("Failed to parse ShulkerDefinition: " + parseResult.error().get().message());
                ShulkerDefinition definition = parseResult.result().orElseThrow(() -> new JsonParseException("Failed to parse ShulkerDefinition for file: " + file.getName()));
                SHULKERS.put(definition.id(), new RegisteredShulker(definition));
            } catch (IOException | JsonParseException | NullPointerException e) {
                ResourcefulShulkers.LOGGER.error("Failed to read shulker file: {}", shulkersDirectory.toURI().relativize(file.toURI()).getPath(), e);
            }
        }
        ResourcefulShulkers.LOGGER.info("Loaded {} shulker definitions", SHULKERS.size());
        registerEntries();
    }

    private static void registerEntries(){
        SHULKERS.forEach((id,shulker)->{
            shulker.setShell(RSItems.createShell(shulker.definition()));
            shulker.setupEntity(RSEntities.createResourceShulker(shulker.definition()),RSItems.createResourceShulker(shulker.definition()));
            shulker.setBulletType(RSEntities.createResourceBullet(shulker.definition()));
            shulker.setGeneratingBox(RSBlocks.createGeneratingBox(shulker.definition()));
        });
    }


    static @Nullable RegisteredShulker getShulkerFromDefinition(ShulkerDefinition definition){
        return getShulker(definition.id());
    }

    public static void forEachShulker(Consumer<RegisteredShulker> consumer){
        SHULKERS.values().forEach(consumer);
    }

    public static @Nullable RegisteredShulker getShulker(ResourceLocation id){
        return SHULKERS.get(id);
    }

    public static @Nullable ShulkerDefinition getDefinition(ResourceLocation id){
        RegisteredShulker shulker = getShulker(id);
        return shulker == null ? null : shulker.definition();
    }

    public static boolean exists(ResourceLocation id){
        return SHULKERS.containsKey(id);
    }

    private static void createShulkerDirectory(File directory) {
        if (directory.mkdirs()) {
            ResourcefulShulkers.LOGGER.info("Created shulkers directory at: {}", directory.getAbsolutePath());
            addDefaultShulkers(directory);
        } else {
            ResourcefulShulkers.LOGGER.error("Failed to create shulkers directory at: {}", directory.getAbsolutePath());
        }

    }

    private static void addDefaultShulkers(File directory){
        for (DefaultShulkerType type : DefaultShulkerType.values()) {
            try(FileWriter writer = new FileWriter(new File(directory, type.name().toLowerCase() + ".json"))) {
                writer.write(type.toJson());
                ResourcefulShulkers.LOGGER.info("Added default shulker: {}", type.name().toLowerCase());
            } catch (Exception e) {
                ResourcefulShulkers.LOGGER.error("Failed to add default shulker: {}", type.name().toLowerCase(), e);
            }

        }
    }

}
