package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.advancements.RSAdvancement;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RSAdvancementProvider implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        Map<ResourceLocation, AdvancementHolder> saved = new HashMap<>();
        for (RSAdvancement advancement : RSAdvancements.all()) {
            RSAdvancement parent = advancement.parent();
            AdvancementHolder holder = advancement.toVanillaBuilder(parent == null ? null : saved.get(parent.id()))
                    .save(saver, advancement.id(), existingFileHelper);
            saved.put(advancement.id(), holder);
        }
    }
}
