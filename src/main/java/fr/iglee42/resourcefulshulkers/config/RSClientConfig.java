package fr.iglee42.resourcefulshulkers.config;

import fr.iglee42.resourcefulshulkers.aura.AuraOverlay;
import net.neoforged.neoforge.common.ModConfigSpec;

public class RSClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.EnumValue<AuraOverlay.Anchor> AURA_BAR_ANCHOR;
    public static final ModConfigSpec.IntValue AURA_BAR_OFFSET_X;
    public static final ModConfigSpec.IntValue AURA_BAR_OFFSET_Y;

    static {

        BUILDER.push("aura_bar");
        AURA_BAR_ANCHOR = BUILDER.comment(" The anchor of the aura bar.").defineEnum("anchor", AuraOverlay.Anchor.TOP_RIGHT);
        AURA_BAR_OFFSET_X = BUILDER.comment(" The x offset of the aura bar, it is based on the anchor of the screen , if the anchor is on the right, the bar will be moved to the left").defineInRange("x_offset", 0,0,Integer.MAX_VALUE);
        AURA_BAR_OFFSET_Y = BUILDER.comment(" The y offset of the aura bar, it is based on the anchor of the screen , if the anchor is on the bottom, the bar will be moved up").defineInRange("y_offset", 0,0,Integer.MAX_VALUE);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
