package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

public class BlockStatesGenerator {
    public static void generate() {
        ShulkersManager.forEachShulker(s->{
            generatingBox(s.definition().id().getPath().toLowerCase());
        });
    }

    private static void generatingBox(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_STATES_PATH.toFile(), name+"_generating_box.json"));
            writer.write("""
                    {
                      "variants": {
                        "": {
                          "model": "minecraft:block/shulker_box"
                        }
                      }
                    }""");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
    private static void blockState(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_STATES_PATH.toFile(), name+".json"));
            writer.write("{\n" +
                    "  \"variants\": {\n" +
                    "    \"\": {\n" +
                    "      \"model\": \""+MODID+":block/"+name+"\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
