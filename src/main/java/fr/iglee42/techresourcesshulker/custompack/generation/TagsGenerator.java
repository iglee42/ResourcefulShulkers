package fr.iglee42.techresourcesshulker.custompack.generation;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagsGenerator {

    private static List<String> shulker = new ArrayList<>();
    public static void generate() {

        Types.TYPES.forEach(r->shulker.add(r.name().toLowerCase()+"_shulker"));

        try {
            JsonObject tag = new JsonObject();
            tag.addProperty("replace",false);
            JsonArray shulker = new JsonArray();
            shulker.forEach(shulker::add);
            tag.add("values",shulker);

            writeTag(tag,new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers.json"));

        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when tags generating",exception);
        }
    }

    private static void writeTag(JsonObject tag, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(tag));
        writer.close();
    }

}
