package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModEntities;
import fr.iglee42.resourcefulshulkers.entity.CustomShulkerBullet;
import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShulkersManager {


    public static List<ShulkerType> TYPES = new ArrayList<>();
    public static Map<String, ResourceLocation> FILES = new HashMap<>();
    public static Map<ResourceLocation, RegistryObject<EntityType<ResourceShulker>>> ENTITY_TYPES = new HashMap<>();
    public static Map<ResourceLocation, RegistryObject<EntityType<CustomShulkerBullet>>> BULLET_TYPES = new HashMap<>();

    public static void init(){
        //TYPES.add(new Resource(new ResourceLocation(ResourcefulShulkers.MODID,"wood"),Items.OAK_LOG,DyeColor.BROWN,0X612B02,null));

        File dir = FMLPaths.CONFIGDIR.get().resolve(ResourcefulShulkers.MODID + "/shulkers/").toFile();
        if (!dir.exists()) createShulkerDir(dir);
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
                    ShulkerType r = JsonHelper.createRecordFromJson(ShulkerType.class,json);
                    if (r.getItem() == Items.AIR){
                        reader.close();
                        continue;
                    }
                    TYPES.add(r);
                    FILES.put(file.getPath().replace(dir.getPath(),""),r.id());
                    reader.close();
                } catch (Exception e) {
                    ResourcefulShulkers.LOGGER.error("An error occurred while loading shulkers (" + file.getName()+")", e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }

        TYPES.forEach(t->{
            ModBlocks.createBox(t.id());
            ModItems.createShell(t.id());
            ENTITY_TYPES.put(t.id(), ModEntities.createShulker(t.id()));
            BULLET_TYPES.put(t.id(), ModEntities.createBullet(t.id()));
        } );
    }

    private static void createShulkerDir(File dir) {
        if (dir.mkdirs() && dir.isDirectory()){
            for (BaseTypes t : BaseTypes.values()) {
                try (FileWriter writer = new FileWriter(new File(dir,t.name().toLowerCase() + ".json"))) {
                    writer.write(t.toJson());
                } catch (Exception e) {
                    ResourcefulShulkers.LOGGER.error("An error occurred while generating default shulkers", e);
                }

            }
        }
    }


}
