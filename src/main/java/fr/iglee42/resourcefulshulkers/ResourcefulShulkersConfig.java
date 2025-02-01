package fr.iglee42.resourcefulshulkers;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ResourcefulShulkersConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> ABSORBER_AURA;
    public static final ModConfigSpec.ConfigValue<Boolean> TIAB_PROTECTION;

    static {
        BUILDER.push("ResourcefulShulker Config");

        ABSORBER_AURA = BUILDER.comment("The total of aura given by absorber with one shulker. Prefer to have a multiple of 32").define("absorber_aura", 4096);
        TIAB_PROTECTION = BUILDER.comment("Enable the protection of Generating Box against time in a bottle").define("tiab_protection", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}