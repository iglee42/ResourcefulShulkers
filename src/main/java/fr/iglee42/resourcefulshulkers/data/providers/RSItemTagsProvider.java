package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import static fr.iglee42.resourcefulshulkers.registries.RSTags.Items.*;

public class RSItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {


    public RSItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(packOutput, Registries.ITEM, registries, (it) -> it.builtInRegistryHolder().key(), RSIds.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BEE_DROPS)
                .add(Items.HONEY_BLOCK, Items.HONEYCOMB);

        tag(CHICKEN_DROPS)
                .add(Items.CHICKEN, Items.FEATHER);

        tag(COW_DROPS)
                .add(Items.BEEF, Items.LEATHER);

        tag(FISH_DROPS)
                .add(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);

        tag(FROG_DROPS)
                .add(Items.OCHRE_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT, Items.VERDANT_FROGLIGHT);

        tag(RABBIT_DROPS)
                .add(Items.RABBIT, Items.RABBIT_HIDE, Items.RABBIT_FOOT);

        tag(SHEEP_DROPS)
                .add(Items.MUTTON)
                        .addTag(ItemTags.WOOL);

        tag(SPIDER_DROPS)
                .add(Items.SPIDER_EYE, Items.STRING);

        tag(ZOMBIE_DROPS)
                .add(Items.ROTTEN_FLESH, Items.ZOMBIE_HEAD);

        tag(CREEPER_DROPS)
                .add(Items.GUNPOWDER, Items.CREEPER_HEAD);

        tag(SKELETON_DROPS)
                .add(Items.BONE, Items.ARROW, Items.SKELETON_SKULL);

        tag(WARDEN_DROPS)
                .add(Items.ECHO_SHARD, Items.SCULK);

        tag(OBSIDIAN_DROPS)
                .add(Items.OBSIDIAN,Items.CRYING_OBSIDIAN);

        tag(WITHER_DROPS)
                .add(Items.WITHER_SKELETON_SKULL,Items.NETHER_STAR, Items.WITHER_ROSE);

        tag(ENDER_DRAGON_DROPS)
                .add(Items.DRAGON_BREATH,Items.DRAGON_EGG,Items.DRAGON_HEAD);

        tag(GUARDIAN_DROPS)
                .add(Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD);

        tag(XP_DROPS)
                .add(Items.EXPERIENCE_BOTTLE)
                .addOptional(ResourceLocation.fromNamespaceAndPath("create","experience_nugget"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("ars_nouveau","experience_gem"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("mob_grinding_utils","solid_xp_baby"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("forbidden_arcanus","xpetrified_orb"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("actuallyadditions","solidified_experience"));

        tag(SHULKERS)
                .add(RSItems.SHULKER.get())
                .addOptionalTag(ELEMENTAL_SHULKERS)
                .addOptionalTag(RESOURCE_SHULKERS);

        tag(SHULKER_SHELLS)
                .add(Items.SHULKER_SHELL)
                .addOptionalTag(RESOURCE_SHULKER_SHELLS);
    }
}
