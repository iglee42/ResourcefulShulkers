package fr.iglee42.techresourcesshulker.custompack.generation;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

public class LootTablesGenerator {
    public static void generate() {
        Types.TYPES.forEach(r->normal(r.name().toLowerCase()+"_generating_box"));
    }

    private static void normal(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.LOOT_TABLES_PATH.toFile(), name+".json"));
            writer.write("{\n" +
                    "  \"type\": \"minecraft:block\",\n" +
                    "  \"pools\": [\n" +
                    "    {\n" +
                    "      \"bonus_rolls\": 0.0,\n" +
                    "      \"conditions\": [\n" +
                    "        {\n" +
                    "          \"condition\": \"minecraft:survives_explosion\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"entries\": [\n" +
                    "        {\n" +
                    "          \"type\": \"minecraft:item\",\n" +
                    "          \"name\": \"techresourcesshulker:"+name+"\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"rolls\": 1.0\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"random_sequence\": \"techresourcesshulker:"+name+"\"\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }
}
