package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class TypesManager {


    public static Map<ResourceLocation,String> TYPES = new HashMap<>();;

    public static void init(){
        Arrays.stream(Base.values()).forEach(b-> TYPES.put(new ResourceLocation(MODID,b.name().toLowerCase()),b.displayName));
        File dir = FMLPaths.CONFIGDIR.get().resolve(MODID + "/types/").toFile();
        dir.mkdirs();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
            if (files == null)
                return;
            for (File file : files) {
                JsonObject json;
                InputStreamReader reader = null;
                try {
                    JsonParser parser = new JsonParser();
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    json = parser.parse(reader).getAsJsonObject();
                    if (json.get("id").getAsString().isEmpty()) throw new NullPointerException("The id can't be empty ! (" + file.getName() + ")" );
                    if (json.get("displayName").getAsString().isEmpty()) throw new NullPointerException("The display name can't be empty ! (" + file.getName() + ")" );
                    TYPES.put(new ResourceLocation(MODID,json.get("id").getAsString()),json.get("displayName").getAsString());
                    reader.close();
                } catch (Exception e) {
                    ResourcefulShulkers.LOGGER.error("An error occurred while loading types (" + file.getName()+")", e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }
    }


    public static String getTierDisplayName(String type){
        return TYPES.getOrDefault(new ResourceLocation(MODID,type),TYPES.get(TYPES.keySet().stream().toList().get(0)));
    }

    public static boolean isTierExist(String type){
        return TYPES.containsKey(new ResourceLocation(MODID,type));
    }

    private enum Base{
        ELEMENTAL("\u00a7dElemental"),
        OVERWORLD("\u00a72Overworld"),
        SKY("\u00a7bSky"),
        NETHER("\u00a74Nether"),
        END("\u00a7eEnd")
        ;
        public final String displayName;

        Base(String displayName) {
            this.displayName = displayName;
        }
    }


}
