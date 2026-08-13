package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.config.RSCommonConfig;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.entity.shulkers.ResourceShulker;
import fr.iglee42.resourcefulshulkers.entity.shulkers.TypeShulker;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.io.File;
import java.io.FileWriter;
import java.util.function.Supplier;

public class RecipesGenerator {
    public static void generate() {
        if (RSCommonConfig.GENERATE_RESOURCE_RECIPES.get())
            ShulkersManager.forEachShulker(shulker -> {
                if (shulker.definition().item().isConsideredEmptyWithoutTags()) return;
                shulker(shulker.definition());
                generatingBox(shulker.definition());
            });

        if (RSCommonConfig.GENERATE_TYPE_RECIPES.get())
            TypesManager.forEachType(type->{
                if (type.equals(TypesManager.getElementalType())) return;
                if (!type.definition().createShulker()) return;
                essence(type.definition());
            });
    }



    private static void shulker(IShulkerDefinition shulker){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), shulker.id().getPath()+"_shulker.json"));
            JsonObject recipe = new JsonObject();
            recipe.addProperty("type","resourcefulshulkers:item_infusion");
            recipe.addProperty("base_entity",getTypeShulkerId(shulker.type().registration().entityType()));
            recipe.addProperty("result_entity",getShulkerId(shulker.registration().entityType()));
            JsonArray ingredients = new JsonArray();
            for (int i = 0; i < 4; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,shulker.item().getIngredient()).getOrThrow());
            }
            for (int i = 0; i < 4; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,Ingredient.of(shulker.type().registration().essenceItem().get())).getOrThrow());
            }
            recipe.add("pedestal_ingredients",ingredients);
            writer.write(new Gson().toJson(recipe));
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating for shulker : {}", shulker.id(), exception);
        }
    }

    private static void essence(ITypeDefinition type){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), type.id().getPath()+"_essence.json"));
            JsonObject recipe = new JsonObject();
            recipe.addProperty("type","resourcefulshulkers:item_infusion");
            recipe.addProperty("base_entity",getTypeShulkerId(type.registration().entityType()));
            recipe.addProperty("result_entity","minecraft:item");
            JsonObject nbts = new JsonObject();
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id",BuiltInRegistries.ITEM.getKey(type.registration().essenceItem().get()).toString());
            itemJson.addProperty("count",4);
            nbts.add("Item",itemJson);
            recipe.add("result_nbt",nbts);
            JsonArray ingredients = new JsonArray();
            for (int i = 0; i < 4; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,Ingredient.of(RSItems.BASE_ESSENCE.get())).getOrThrow());
            }
            recipe.add("pedestal_ingredients",ingredients);
            writer.write(new Gson().toJson(recipe));
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }

    private static void generatingBox(IShulkerDefinition shulker){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), shulker.id().getPath()+"_generating_box.json"));
            JsonObject recipe = new JsonObject();
            recipe.addProperty("type","resourcefulshulkers:item_infusion");
            recipe.addProperty("base_entity",getShulkerId(shulker.registration().entityType()));
            recipe.addProperty("result_entity","minecraft:item");
            JsonObject nbts = new JsonObject();
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id",BuiltInRegistries.BLOCK.getKey(shulker.registration().generatingBox().get()).toString());
            itemJson.addProperty("count",1);
            nbts.add("Item",itemJson);
            recipe.add("result_nbt",nbts);
            JsonArray ingredients = new JsonArray();
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,shulker.item().getIngredient()).getOrThrow());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,Ingredient.of(shulker.registration().shell().get())).getOrThrow());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,Ingredient.of(shulker.type().registration().essenceItem().get())).getOrThrow());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE,Ingredient.of(Tags.Items.CHESTS_WOODEN)).getOrThrow());
            }
            recipe.add("pedestal_ingredients",ingredients);
            writer.write(new Gson().toJson(recipe));
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }

    private static String getTypeShulkerId(Supplier<EntityType<? extends TypeShulker>> shulker){
        return BuiltInRegistries.ENTITY_TYPE.getKey(shulker.get()).toString();
    }

    private static String getShulkerId(Supplier<EntityType<? extends ResourceShulker>> shulker){
        return BuiltInRegistries.ENTITY_TYPE.getKey(shulker.get()).toString();
    }
}
