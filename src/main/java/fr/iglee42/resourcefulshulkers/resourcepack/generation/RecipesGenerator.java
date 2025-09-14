package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;

public class RecipesGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r->{
            generatingBox(r.id().getPath().toLowerCase(),r.type().getPath(),r.item());
            shulker(r.id().getPath().toLowerCase(),r.item(),r.type().getPath());
        });
    }



    private static void shulker(String name,Ingredient item,String type){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_shulker.json"));
            JsonObject recipe = new JsonObject();
            recipe.addProperty("type","resourcefulshulkers:shulker_item_infusion");
            recipe.addProperty("baseEntity","resourcefulshulkers:"+type+"_shulker");
            recipe.addProperty("resultEntity","resourcefulshulkers:"+name+"_shulker");
            JsonArray ingredients = new JsonArray();
            for (int i = 0; i < 4; i++) {
                ingredients.add(item.toJson());
            }
            for (int i = 0; i < 4; i++) {
                ingredients.add(Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation("resourcefulshulkers:"+type+"_essence"))).toJson());
            }
            recipe.add("pedestalsIngredients",ingredients);
            writer.write(new Gson().toJson(recipe));
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }

    private static void generatingBox(String name, String type, Ingredient item){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_generating_box.json"));
            JsonObject recipe = new JsonObject();
            recipe.addProperty("type","resourcefulshulkers:shulker_item_infusion");
            recipe.addProperty("baseEntity","resourcefulshulkers:"+name+"_shulker");
            recipe.addProperty("resultEntity","minecraft:item");
            JsonObject nbts = new JsonObject();
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id","resourcefulshulkers:"+name+"_generating_box");
            itemJson.addProperty("Count",1);
            nbts.add("Item",itemJson);
            recipe.add("resultNbt",nbts);
            JsonArray ingredients = new JsonArray();
            for (int i = 0; i < 2; i++) {
                ingredients.add(item.toJson());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation("resourcefulshulkers:"+name+"_shell"))).toJson());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation("resourcefulshulkers:"+type+"_essence"))).toJson());
            }
            for (int i = 0; i < 2; i++) {
                ingredients.add(Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:chest"))).toJson());
            }
            recipe.add("pedestalsIngredients",ingredients);
            writer.write(new Gson().toJson(recipe));
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }
}
