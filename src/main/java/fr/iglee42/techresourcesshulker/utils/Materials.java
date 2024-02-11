package fr.iglee42.techresourcesshulker.utils;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.Sheets.SHULKER_SHEET;

public class Materials {
    public static final Material OVERWORLD_SHULKER_TEXTURE = new Material(SHULKER_SHEET,new ResourceLocation(TechResourcesShulker.MODID,"entity/base_essence/overworld"));
    public static final Material SKY_SHULKER_TEXTURE = new Material(SHULKER_SHEET,new ResourceLocation(TechResourcesShulker.MODID,"entity/base_essence/sky"));
    public static final Material NETHER_SHULKER_TEXTURE = new Material(SHULKER_SHEET,new ResourceLocation(TechResourcesShulker.MODID,"entity/base_essence/nether"));

}
