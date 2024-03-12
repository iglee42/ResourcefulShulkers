package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;

public class LootTablesGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r-> generatingBox(r.id().getPath().toLowerCase()));
    }

    private static void generatingBox(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.LOOT_TABLES_PATH.toFile(), name+"_generating_box.json"));
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
                    "          \"functions\": [\n" +
                    "            {\n" +
                    "              \"function\": \"minecraft:copy_name\",\n" +
                    "              \"source\": \"block_entity\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"function\": \"minecraft:copy_nbt\",\n" +
                    "              \"ops\": [\n" +
                    "                {\n" +
                    "                  \"op\": \"replace\",\n" +
                    "                  \"source\": \"remainingDurability\",\n" +
                    "                  \"target\": \"durability\"\n" +
                    "                }\n" +
                    "              ],\n" +
                    "              \"source\": \"block_entity\"\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"name\": \"resourcefulshulkers:"+name+"_generating_box\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"rolls\": 1.0\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"random_sequence\": \"resourcefulshulkers:blocks/"+name+"_generating_box\"\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when recipes generating",exception);
        }
    }
}
