package fr.iglee42.resourcefulshulkers.resourcepack.generation;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.resourcepack.PathConstant;

import java.io.File;
import java.io.FileWriter;

public class RecipesGenerator {
    public static void generate() {
        ShulkersManager.TYPES.forEach(r->{
            if (r.item().startsWith("#")) {
                shulkerWithTag(r.id().getPath().toLowerCase(),r.item().substring(1),r.type());
            } else{
                shulker(r.id().getPath().toLowerCase(),r.item(),r.type());
            }
            generatingBox(r.id().getPath().toLowerCase(),r.type());
        });
    }



    private static void shulkerWithTag(String name,String tag,String type){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_shulker.json"));
            writer.write("{\n" +
                    "  \"neoforge:conditions\": [\n" +
                    "    {\n" +
                    "      \"type\": \"neoforge:not\",\n" +
                    "      \"value\": {\n" +
                    "        \"type\": \"neoforge:tag_empty\",\n" +
                    "        \"tag\": \""+tag+"\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"type\": \"resourcefulshulkers:shulker_item_infusion\",\n" +
                    "  \"baseEntity\": \"resourcefulshulkers:"+type+"_shulker\",\n" +
                    "  \"resultEntity\": \"resourcefulshulkers:"+name+"_shulker\",\n" +
                    "  \"pedestalsIngredients\": [\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"tag\": \""+tag+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"tag\": \""+tag+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"tag\": \""+tag+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"tag\": \""+tag+"\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }

    private static void shulker(String name,String item,String type){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_shulker.json"));
            writer.write("{\n" +
                    "        \"type\": \"resourcefulshulkers:shulker_item_infusion\",\n" +
                    "        \"baseEntity\": \"resourcefulshulkers:"+type+"_shulker\",\n" +
                    "        \"resultEntity\": \"resourcefulshulkers:"+name+"_shulker\",\n" +
                    "        \"pedestalsIngredients\": [\n" +
                    "          {\n" +
                    "            \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "          },{\n" +
                    "            \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "          },{\n" +
                    "            \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "          },{\n" +
                    "            \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "          },{\n" +
                    "            \"item\": \""+item+"\"\n" +
                    "          },{\n" +
                    "            \"item\": \""+item+"\"\n" +
                    "          },{\n" +
                    "            \"item\": \""+item+"\"\n" +
                    "          },{\n" +
                    "            \"item\": \""+item+"\"\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }


    private static void generatingBox(String name,String type){
        try {
            FileWriter writer = new FileWriter(new File(PathConstant.RECIPES_PATH.toFile(), name+"_generating_box.json"));
            writer.write("{\n" +
                    "  \"type\": \"resourcefulshulkers:shulker_item_infusion\",\n" +
                    "  \"baseEntity\": \"resourcefulshulkers:"+name+"_shulker\",\n" +
                    "  \"resultEntity\": \"minecraft:item\",\n" +
                    "  \"resultNbt\": {\n" +
                    "    \"Item\": {\n" +
                    "      \"id\": \"resourcefulshulkers:"+name+"_generating_box\",\n" +
                    "      \"Count\": 1\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"pedestalsIngredients\": [\n" +
                    "    {\n" +
                    "      \"item\": \"resourcefulshulkers:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"item\": \"resourcefulshulkers:"+name+"_shell\"\n" +
                    "    },{\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },{\n" +
                    "      \"item\": \"resourcefulshulkers:"+type+"_essence\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    },{\n" +
                    "      \"item\": \"minecraft:chest\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            writer.close();
        } catch (Exception exception){
            ResourcefulShulkers.LOGGER.error("An error was detected when blockstates generating",exception);
        }
    }
}
