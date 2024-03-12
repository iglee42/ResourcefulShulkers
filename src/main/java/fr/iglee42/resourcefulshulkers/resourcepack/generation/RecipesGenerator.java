package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;

public class RecipesGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r->{
            generatingBox(r.id().getPath().toLowerCase());
        });
    }

    private static void generatingBox(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_generating_box.json"));
            writer.write("{\n" +
                    "  \"type\": \"resourcefulshulkers:shulker_item_infusion\",\n" +
                    "  \"baseEntity\": \"resourcefulshulkers:"+name+"_shulker\",\n" +
                    "  \"resultEntity\": \"minecraft:item\",\n" +
                    "  \"resultNbt\": {\n" +
                    "    \"Item\": {\n" +
                    "      \"id\": \"resourcefulshulkers:"+name+"_generating_box\",\n" +
                    "      \"Count\": 1\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"pedestalsIngredients\": [\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"item\": \"resourcefulshulkers:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"tag\": \"resourcefulshulkers:essences\"\n" +
                    "    },{\n" +
                    "      \"tag\": \"resourcefulshulkers:essences\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}