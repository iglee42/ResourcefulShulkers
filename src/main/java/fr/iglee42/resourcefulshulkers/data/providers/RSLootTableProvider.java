package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RSLootTableProvider extends LootTableProvider {
    public RSLootTableProvider(PackOutput output,  CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)), registries);
    }

    private static class Blocks extends BlockLootSubProvider{

        protected Blocks(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            dropSelf(RSBlocks.END_CITY.get());
            dropSelf(RSBlocks.END_CITY_TIER_2.get());
            dropSelf(RSBlocks.TRAINER.get());
            dropSelf(RSBlocks.PURPUR_TARGET.get());
            dropSelf(RSBlocks.SHULKER_ABSORBER.get());
            dropSelf(RSBlocks.SHULKER_PEDESTAL.get());
            dropSelf(RSBlocks.SHULKER_INFUSER.get());
            dropSelf(RSBlocks.SHULKER_HEAD.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(RSBlocks.END_CITY.get(), RSBlocks.END_CITY_TIER_2.get(), RSBlocks.TRAINER.get(), RSBlocks.PURPUR_TARGET.get(), RSBlocks.SHULKER_ABSORBER.get(), RSBlocks.SHULKER_HEAD.get(), RSBlocks.SHULKER_PEDESTAL.get(), RSBlocks.SHULKER_INFUSER.get());
        }
    }
}
