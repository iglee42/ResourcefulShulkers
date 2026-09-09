package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagsGenerator {

    private static List<String> elementalShulker = new ArrayList<>();
    private static List<String> essences = new ArrayList<>();
    private static List<String> resourceShulker = new ArrayList<>();
    private static List<String> shells = new ArrayList<>();
    private static List<String> pickaxe = new ArrayList<>();
    public static void generate() {

        TypesManager.forEachType(t->{
            if (t.definition().equals(TypesManager.getElementalType())) return;
            try {
                if(t.definition().createShulker()) {
                    writeTag(List.of(BuiltInRegistries.ENTITY_TYPE.getKey(t.entityType().get()).toString()), new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers/elemental/" + t.definition().id().getPath() + ".json"));
                    writeTag(List.of(BuiltInRegistries.ENTITY_TYPE.getKey(t.entityType().get()).toString()), new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulkers/elemental/" + t.definition().id().getPath() + ".json"));
                    elementalShulker.add("#resourcefulshulkers:shulkers/elemental/"+t.definition().id().getPath());
                }
                writeTag(List.of(BuiltInRegistries.ITEM.getKey(t.essenceItem().get()).toString()), new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "essences/"+t.definition().id().getPath()+".json"));
                essences.add("#resourcefulshulkers:essences/"+t.definition().id().getPath());
            } catch (IOException e) {
                ResourcefulShulkers.LOGGER.error("An error was detected when tags generating for type {}", t.definition().id(),e);
            }

        });

        ShulkersManager.forEachShulker(s->{
            try {
                writeTag(List.of(BuiltInRegistries.ENTITY_TYPE.getKey(s.entityType().get()).toString()), new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers/resource/" + s.definition().id().getPath() + ".json"));
                writeTag(List.of(BuiltInRegistries.ENTITY_TYPE.getKey(s.entityType().get()).toString()), new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulkers/resource/" + s.definition().id().getPath() + ".json"));
                resourceShulker.add("#resourcefulshulkers:shulkers/resource/"+s.definition().id().getPath());
                writeTag(List.of(BuiltInRegistries.ITEM.getKey(s.shell().get()).toString()), new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulker_shells/resource/"+s.definition().id().getPath()+".json"));
                shells.add("#resourcefulshulkers:shulker_shells/resource/"+s.definition().id().getPath());
            } catch (IOException e) {
                ResourcefulShulkers.LOGGER.error("An error was detected when tags generating for shulker {}", s.definition().id(),e);
            }


            pickaxe.add(BuiltInRegistries.BLOCK.getKey(s.generatingBox().get()).toString());
        });

        try {
            writeTag(elementalShulker,new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers/elemental.json"));
            writeTag(elementalShulker,new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulkers/elemental.json"));
            writeTag(essences,new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "essences.json"));

            writeTag(resourceShulker,new File(PathConstant.ENTITY_TYPES_TAGS_PATH.toFile(), "shulkers/resource.json"));
            writeTag(resourceShulker,new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulkers/resource.json"));
            writeTag(shells,new File(PathConstant.ITEMS_TAGS_PATH.toFile(), "shulker_shells/resource.json"));

            writeTag(pickaxe,new File(PathConstant.MC_MINEABLE_TAGS_PATH.toFile(), "pickaxe.json"));

        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when global tags generating",exception);
        }
    }

    private static void writeTag(List<String> objects, File file) throws IOException {
        file.getParentFile().mkdirs();
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
