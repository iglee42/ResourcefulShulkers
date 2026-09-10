package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.advancements.RSAdvancement;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RSAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Map<ResourceLocation, Advancement> saved = new HashMap<>();
        for (RSAdvancement advancement : RSAdvancements.all()) {
            RSAdvancement parent = advancement.parent();
            Advancement holder = advancement.toVanillaBuilder(parent == null ? null : saved.get(parent.id()))
                    .save(saver, advancement.id(), existingFileHelper);
            saved.put(advancement.id(), holder);
        }
    }
}
