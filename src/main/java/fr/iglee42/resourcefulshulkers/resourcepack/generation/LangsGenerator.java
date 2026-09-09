package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.ChatFormatting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LangsGenerator {

    private static Map<String,String> langs = new HashMap<>();
    public static void generate() {

        TypesManager.forEachType(type->{
            langs.put("entity."+ RSIds.MODID+"."+type.definition().id().getPath().toLowerCase()+"_shulker", type.definition().name() + " §rShulker");
            langs.put("item."+ RSIds.MODID+"."+type.definition().id().getPath().toLowerCase()+"_essence", type.definition().name() + " §rEssence");
        });

        ShulkersManager.forEachShulker(shulker->{
            String color = (shulker.definition().name().contains("§") ? "" : closestFormatting(shulker.definition().color())).toString();
            langs.put("entity."+ RSIds.MODID+"."+shulker.definition().id().getPath().toLowerCase()+"_shulker_bullet", color+shulker.definition().name() + " §rShulker Bullet");
            langs.put("entity."+ RSIds.MODID+"."+shulker.definition().id().getPath().toLowerCase()+"_shulker", color+shulker.definition().name() + " §rShulker");
            langs.put("item."+ RSIds.MODID+"."+shulker.definition().id().getPath().toLowerCase()+"_shell", color + shulker.definition().name()+ " §rShulker Shell");
            langs.put("block."+ RSIds.MODID+"."+shulker.definition().id().getPath().toLowerCase()+"_generating_box", color + shulker.definition().name()+ " §rGenerating Box");
        });

        writeFile();
    }

    private static void writeFile(){
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


    public static ChatFormatting closestFormatting(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        ChatFormatting best = ChatFormatting.WHITE;
        int bestDistance = Integer.MAX_VALUE;

        for (ChatFormatting formatting : ChatFormatting.values()) {
            Integer color = formatting.getColor();
            if (color == null) {
                continue;
            }

            int fr = (color >> 16) & 0xFF;
            int fg = (color >> 8) & 0xFF;
            int fb = color & 0xFF;

            int dr = r - fr;
            int dg = g - fg;
            int db = b - fb;

            int distance = dr * dr + dg * dg + db * db;

            if (distance < bestDistance) {
                bestDistance = distance;
                best = formatting;
            }
        }

        return best;
    }

}
