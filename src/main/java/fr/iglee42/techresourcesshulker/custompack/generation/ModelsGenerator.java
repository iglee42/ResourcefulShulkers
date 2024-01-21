package fr.iglee42.techresourcesshulker.custompack.generation;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.custompack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public class ModelsGenerator {
    public static void generate() {
        Types.TYPES.forEach(r->{
            itemFromParent(r.name().toLowerCase()+"_shell","item/generated",new TextureKey("layer0","techresourcesshulker:item/shulker_shell"));
            itemFromParent(r.name().toLowerCase()+"_shulker",MODID+":item/shulker",new TextureKey("0","minecraft:entity/shulker/shulker_"+r.color().getName().toLowerCase()));
        });
    }

    private static void itemFromBlock(String name){
        itemFromParent(name,MODID + ":block/" + name);
    }
    private static void itemFromParent(String name, String parent, TextureKey... textureKeys){
        String jsonBase =   "{\n"+
                            "   \"parent\": \""+ parent +"\""+(textureKeys.length > 0 ? ",":"")+"\n";
        StringBuilder builder = new StringBuilder(jsonBase);
        if (textureKeys.length > 0){
            builder.append("   \"textures\": {\n");
            for (int i = 0; i < textureKeys.length; i++){
                builder.append(textureKeys[i].toJson());
                if (i != textureKeys.length - 1) builder.append(",");
                builder.append("\n");
            }
            builder.append("    }\n");
        }
        builder.append("}");
        generateItem(name,builder.toString());
    }
    private static void generateItem(String name, String fileText) {
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.ITEM_MODELS_PATH.toFile(), name+".json"));
            writer.write(fileText);
            writer.close();
        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when models generating",exception);
        }

    }
    private static void blockFromParent(String name,String parent,TextureKey... textureKeys){
        String jsonBase =   "{\n"+
                            "   \"parent\": \""+ parent +"\",\n";
        StringBuilder builder = new StringBuilder(jsonBase);
        if (textureKeys.length > 0){
            builder.append("   \"textures\": {\n");
            for (int i = 0; i < textureKeys.length; i++){
                builder.append(textureKeys[i].toJson());
                if (i != textureKeys.length - 1) builder.append(",");
                builder.append("\n");
            }
            builder.append("    }\n");
        }
        builder.append("}");
        generateBlock(name,builder.toString());
    }
    private static void generateBlock(String name, String fileText) {
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.BLOCK_MODELS_PATH.toFile(), name+".json"));
            writer.write(fileText);
            writer.close();
        } catch (Exception exception){
            TechResourcesShulker.LOGGER.error("An error was detected when models generating",exception);
        }

    }
}
