package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModelsGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r->{
            itemFromParent(r.id().getPath().toLowerCase()+"_shell","item/generated",new TextureKey("layer0","resourcefulshulkers:item/shulker_shell"));
            itemFromParent(r.id().getPath().toLowerCase()+"_shulker",MODID+":item/shulker",new TextureKey("0",r.getTexture().toString().replace(".png","")));
            itemFromParent(r.id().getPath().toLowerCase()+"_generating_box","item/shulker_box");
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
            ResourcefulShulkers.LOGGER.error("An error was detected when models generating",exception);
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
            ResourcefulShulkers.LOGGER.error("An error was detected when models generating",exception);
        }

    }
}
