package fr.iglee42.resourcefulshulkers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class RSIds {
    public static final String MODID = "resourcefulshulkers";
    public static final MutableComponent PREFIX = Component.literal("[").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal("ResourcefulShulkers").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal("] ").withStyle(ChatFormatting.DARK_PURPLE));

    private RSIds() {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
