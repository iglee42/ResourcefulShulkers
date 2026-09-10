package fr.iglee42.resourcefulshulkers.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class RSCommonConfig {
    public static final Builder BUILDER = new Builder();
    public static final ForgeConfigSpec SPEC;

    public static final BooleanValue GENERATE_RESOURCE_RECIPES;
    public static final BooleanValue GENERATE_TYPE_RECIPES;


    static {

        BUILDER.push("data_generation");
        GENERATE_RESOURCE_RECIPES = BUILDER.comment("Does the mod should generate the recipes for custom resource shulkers").define("generate_resource_recipes", true);
        GENERATE_TYPE_RECIPES = BUILDER.comment("Does the mod should generate the recipes for custom shulkers type").define("generate_type_recipes", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
