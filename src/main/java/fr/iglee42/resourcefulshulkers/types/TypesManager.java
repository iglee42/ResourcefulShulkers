package fr.iglee42.resourcefulshulkers.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.shulkers.RegisteredShulker;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public final class TypesManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<ResourceLocation, RegisteredType> TYPES = new HashMap<>();

    private static final Codec<TypeDefinition> VALIDATED_CODEC = TypeDefinition.CODEC.validate(
            def->{
                if (def.id().equals(ResourceLocation.fromNamespaceAndPath(MODID,"elemental"))) return DataResult.error(()->"Type cannot be elemental type !");
                if (exists(def.id())) return DataResult.error(()->"Type already exists with id: " + def.id());
                return DataResult.success(def);
            }
    );

    private TypesManager() {}

    public static void load(){
        TYPES.clear();
        File typesDirectory = FMLPaths.CONFIGDIR.get().resolve(ResourcefulShulkers.MODID + "/types/").toFile();
        if (!typesDirectory.exists()) {
            createTypeDirectory(typesDirectory);
        }
        File[] files = typesDirectory.listFiles((file) -> file.getName().endsWith(".json"));
        if (files == null) {
            ResourcefulShulkers.LOGGER.error("Failed to list files in types directory at: {}", typesDirectory.getAbsolutePath());
            return;
        }
        TYPES.put(ResourceLocation.fromNamespaceAndPath(MODID,"elemental"), new RegisteredType(new TypeDefinition(ResourceLocation.fromNamespaceAndPath(MODID,"elemental"),"\u00a7dElemental", false)));
        for (File file : files){
            try (FileInputStream inputStream = new FileInputStream(file); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)){
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                DataResult<TypeDefinition> parseResult = VALIDATED_CODEC.parse(JsonOps.INSTANCE, json);
                if (parseResult.error().isPresent()) throw new JsonParseException("Failed to parse TypeDefinition: " + parseResult.error().get().message());
                TypeDefinition definition = parseResult.result().orElseThrow(() -> new JsonParseException("Failed to parse TypeDefinition for file: " + file.getName()));
                TYPES.put(definition.id(), new RegisteredType(definition));
            } catch (IOException | JsonParseException | NullPointerException e) {
                ResourcefulShulkers.LOGGER.error("Failed to read type file: {}", typesDirectory.toURI().relativize(file.toURI()).getPath(), e);
            }
        }
        ResourcefulShulkers.LOGGER.info("Loaded {} type definitions", TYPES.size());
        registerEntries();
    }

    private static void registerEntries(){
        for (RegisteredType type : TYPES.values()) {
            if (type.definition().equals(getElementalType())) continue;

            type.setEssenceItem(RSItems.createEssence(type.definition()));
            if (type.definition().createShulker())
                type.setShulkerItem(RSItems.createTypeShulker(type.definition()));
        }
    }

    public static void forEachType(Consumer<RegisteredType> consumer){
        TYPES.values().forEach(consumer);
    }

    static @Nullable RegisteredType getTypeFromDefinition(TypeDefinition definition){
        return getType(definition.id());
    }


    public static @Nullable RegisteredType getType(ResourceLocation id){
        return TYPES.get(id);
    }

    public static @Nullable TypeDefinition getDefinition(ResourceLocation id){
        RegisteredType type = getType(id);
        return type == null ? null : type.definition();
    }

    public static boolean exists(ResourceLocation id){
        return TYPES.containsKey(id);
    }

    public static @NotNull TypeDefinition getElementalType(){
        TypeDefinition type = getDefinition(ResourceLocation.fromNamespaceAndPath(MODID,"elemental"));
        if (type == null) throw new IllegalStateException("Elemental type is missing!");
        return type;
    }

    private static void createTypeDirectory(File directory) {
        if (directory.mkdirs()) {
            ResourcefulShulkers.LOGGER.info("Created types directory at: {}", directory.getAbsolutePath());
            addDefaultTypes(directory);
        } else {
            ResourcefulShulkers.LOGGER.error("Failed to create types directory at: {}", directory.getAbsolutePath());
        }

    }

    private static void addDefaultTypes(File directory){
        for (Default type : Default.values()) {
            try(FileWriter writer = new FileWriter(new File(directory, type.name().toLowerCase() + ".json"))) {
                writer.write(type.toJson());
                ResourcefulShulkers.LOGGER.info("Added default type: {}", type.name().toLowerCase());
            } catch (Exception e) {
                ResourcefulShulkers.LOGGER.error("Failed to add default type: {}", type.name().toLowerCase(), e);
            }

        }
    }

    private enum Default {
        OVERWORLD("\u00a72Overworld"),
        SKY("\u00a7bSky"),
        NETHER("\u00a74Nether"),
        END("\u00a7eEnd");
        public final String displayName;

        Default(String displayName) {
            this.displayName = displayName;
        }

        public String toJson(){
            JsonObject obj = new JsonObject();
            obj.addProperty("id",MODID + ":"+name().toLowerCase());
            obj.addProperty("name", displayName);
            return GSON.toJson(obj);
        }
    }
}
