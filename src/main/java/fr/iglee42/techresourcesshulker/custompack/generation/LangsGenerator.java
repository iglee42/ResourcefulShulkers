package fr.iglee42.techresourcesshulker.custompack.generation;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public class LangsGenerator {

    private static Map<String,String> langs = new HashMap<>();
    public static void generate() {

        Types.TYPES.forEach(r->{
            langs.put("entity."+MODID+"."+r.name().toLowerCase()+"_shulker", ModsUtils.getUpperName(r.name().toLowerCase()+"_shulker","_"));
            langs.put("item."+MODID+"."+r.name().toLowerCase()+"_shell",ModsUtils.getUpperName(r.name().toLowerCase()+"_shell","_"));
            langs.put("block."+MODID+"."+r.name().toLowerCase()+"_generating_box",ModsUtils.getUpperName(r.name().toLowerCase()+"_generating_box","_"));
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
                    TechResourcesShulker.LOGGER.error("An error was detected when langs generating",e);
                }
            });
            writer.write("}");
            writer.close();
        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when langs generating",exception);
        }
    }

}
