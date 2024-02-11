package fr.iglee42.techresourcesshulker.resourcepack.generation;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.utils.ShulkersManager;
import fr.iglee42.techresourcesshulker.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public class BlockStatesGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r->{
            generatingBox(r.id().getPath().toLowerCase());
        });
    }

    private static void generatingBox(String name){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_STATES_PATH.toFile(), name+"_generating_box.json"));
            writer.write("{\n" +
                    "  \"variants\": {\n" +
                    "    \"\": {\n" +
                    "      \"model\": \"minecraft:block/shulker_box\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when blockstates generating",exception);
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
            TechResourcesShulker.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
