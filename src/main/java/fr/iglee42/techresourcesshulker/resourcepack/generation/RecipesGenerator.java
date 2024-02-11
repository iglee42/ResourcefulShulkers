package fr.iglee42.techresourcesshulker.resourcepack.generation;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.utils.ShulkersManager;
import fr.iglee42.techresourcesshulker.resourcepack.PathConstant;

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
                    "  \"type\": \"techresourcesshulker:shulker_item_infusion\",\n" +
                    "  \"baseEntity\": \"techresourcesshulker:"+name+"_shulker\",\n" +
                    "  \"resultEntity\": \"minecraft:item\",\n" +
                    "  \"resultNbt\": {\n" +
                    "    \"Item\": {\n" +
                    "      \"id\": \"techresourcesshulker:"+name+"_generating_box\",\n" +
                    "      \"Count\": 1\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"pedestalsIngredients\": [\n" +
                    "    {\n" +
                    "      \"item\": \"techresourcesshulker:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"item\": \"techresourcesshulker:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"tag\": \"techresourcesshulker:essences\"\n" +
                    "    },{\n" +
                    "      \"tag\": \"techresourcesshulker:essences\"\n" +
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
            TechResourcesShulker.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
