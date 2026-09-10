package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.registries.RSTags;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import static fr.iglee42.resourcefulshulkers.registries.RSTags.Entities.*;

public class RSEntityTagsProvider extends EntityTypeTagsProvider {


    public RSEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, RSIds.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(SHULKERS)
                .add(EntityType.SHULKER)
                .addOptionalTag(ELEMENTAL_SHULKERS)
                .addOptionalTag(RESOURCE_SHULKERS);

    }
}
