package fr.iglee42.techresourcesshulker.custompack.generation;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public class BlockStatesGenerator {
    public static void generate() {

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
            TechResourcesShulker.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
