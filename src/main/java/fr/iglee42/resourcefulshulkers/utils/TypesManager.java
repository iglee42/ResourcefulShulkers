package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.entity.TypeShulker;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModEntities;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class TypesManager {


    public static List<Type> TYPES = new ArrayList<>();
    public static Map<ResourceLocation, RegistryObject<EntityType<TypeShulker>>> ENTITY_TYPES = new HashMap<>();


    public static void init() {
        Arrays.stream(Base.values()).forEach(b -> TYPES.add(new Type(new ResourceLocation(MODID,b.name().toLowerCase()),b.displayName,b.haveEntity)));
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
                    if (json.get("id").getAsString().isEmpty()) throw new NullPointerException("The id can't be empty ! (" + file.getName() + ")");
                    //if (json.get("displayName").getAsString().isEmpty())
                    //    throw new NullPointerException("The display name can't be empty ! (" + file.getName() + ")");
                    //TYPES.put(new ResourceLocation(MODID, json.get("id").getAsString()), json.get("displayName").getAsString());
                    TYPES.add(JsonHelper.createRecordFromJson(Type.class,json));
                    reader.close();
                } catch (Exception e) {
                    ResourcefulShulkers.LOGGER.error("An error occurred while loading types (" + file.getName() + ")", e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }
        TYPES.forEach(t->{
           if (!t.id().equals(new ResourceLocation(MODID,"elemental"))) ModItems.createEssence(t.id());
           if (t.shouldCreateEntity())ENTITY_TYPES.put(t.id(), ModEntities.createTypeShulker(t.id()));
           //BULLET_TYPES.put(t.id(), ModEntities.createBullet(t.id()));
        } );
    }


    public static String getTierDisplayName(ResourceLocation id) {
        return TYPES.stream().filter(r->r.id().equals(id)).findAny().orElse(TYPES.get(0)).displayName();
    }

    public static boolean doesTierExist(ResourceLocation id) {
        return TYPES.stream().anyMatch(t->t.id().equals(id));
    }

    private enum Base {
        ELEMENTAL("\u00a7dElemental",false),
        OVERWORLD("\u00a72Overworld"),
        SKY("\u00a7bSky"),
        NETHER("\u00a74Nether"),
        END("\u00a7eEnd");
        public final String displayName;
        public final boolean haveEntity;

        Base(String displayName) {
            this(displayName,true);
        }
        Base(String displayName,boolean haveEntity) {
            this.displayName = displayName;
            this.haveEntity = haveEntity;
        }
    }
}

