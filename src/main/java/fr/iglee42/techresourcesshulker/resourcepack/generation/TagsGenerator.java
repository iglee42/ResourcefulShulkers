package fr.iglee42.techresourcesshulker.resourcepack.generation;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.utils.ShulkersManager;
import fr.iglee42.techresourcesshulker.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public class TagsGenerator {

    private static List<String> shulker = new ArrayList<>();
    private static List<String> shells = new ArrayList<>();
    private static List<String> pickaxe = new ArrayList<>();
    public static void generate() {

        ShulkersManager.TYPES.forEach(r->{
            shulker.add(MODID+":"+r.id().getPath().toLowerCase()+"_shulker");
            shells.add(MODID + ":" + r.id().getPath().toLowerCase()+"_shell");
            pickaxe.add(MODID + ":" + r.id().getPath().toLowerCase()+"_generating_box");
        });

        try {
            writeTag(shulker,new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers.json"));
            writeTag(shells,new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulker_shells.json"));
            writeTag(pickaxe,new File(PathConstant.MC_MINEABLE_TAGS_PATH.toFile(), "pickaxe.json"));

        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when tags generating",exception);
        }
    }

    private static void writeTag(List<String> objects, File file) throws IOException {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace",false);
        JsonArray values = new JsonArray();
        objects.forEach(values::add);
        tag.add("values",values);
        FileWriter writer = new FileWriter(file);
        writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(tag));
        writer.close();
    }

}
