package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class LangsGenerator {

    private static Map<String,String> langs = new HashMap<>();
    public static void generate() {

        ShulkersManager.TYPES.forEach(r->{
            langs.put("entity."+MODID+"."+r.id().getPath().toLowerCase()+"_shulker", ModsUtils.getUpperName(r.id().getPath().toLowerCase()+"_shulker","_"));
            langs.put("item."+MODID+"."+r.id().getPath().toLowerCase()+"_shell",ModsUtils.getUpperName(r.id().getPath().toLowerCase()+"_shulker_shell","_"));
            langs.put("block."+MODID+"."+r.id().getPath().toLowerCase()+"_generating_box",ModsUtils.getUpperName(r.id().getPath().toLowerCase()+"_generating_box","_"));
        });

        try {
            FileWriter writer = new FileWriter(new File(PathConstant.LANGS_PATH.toFile(), "en_us.json"));
            writer.write("{\n");
            AtomicInteger index = new AtomicInteger(-1);
            langs.forEach((key,translation) -> {
                try {
                    index.getAndIncrement();
                    writer.write("  \"" + key + "\": \"" + translation + "\"" + (index.get() != langs.size() - 1? ",":"") + "\n");

                } catch (IOException e) {
                    ResourcefulShulkers.LOGGER.error("An error was detected when langs generating",e);
                }
            });
            writer.write("}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when langs generating",exception);
        }
    }

}
