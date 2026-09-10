package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.igleelib.common.init.ModItem;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RSRecipeProvider extends RecipeProvider {
    public RSRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RSItems.BASE_ESSENCE.get())
                .pattern(" D ")
                .pattern("IBI")
                .pattern(" D ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', ModItem.BLAZE_SHARD.get())
                .unlockedBy("has", has(ModItem.BLAZE_SHARD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RSBlocks.SHULKER_ABSORBER.get())
                .pattern("RSR")
                .pattern("SDS")
                .pattern("GEG")
                .define('S', Items.PURPUR_SLAB)
                .define('R', Items.END_ROD)
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('E', Items.END_STONE_BRICKS)
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .unlockedBy("has", has(Items.END_ROD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RSBlocks.SHULKER_INFUSER.get())
                .pattern(" S ")
                .pattern("RGR")
                .pattern(" E ")
                .define('S', Items.PURPUR_SLAB)
                .define('R', Items.END_ROD)
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('E', Items.END_STONE_BRICKS)
                .unlockedBy("has", has(Items.END_ROD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RSBlocks.SHULKER_PEDESTAL.get())
                .pattern("S")
                .pattern("E")
                .pattern("S")
                .define('S', Items.PURPUR_SLAB)
                .define('E', Items.END_STONE_BRICKS)
                .unlockedBy("has", has(Items.END_STONE_BRICKS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RSItems.UPGRADE_BASE.get())
                .pattern(" E ")
                .pattern("ESE")
                .pattern(" E ")
                .define('S', Items.SMOOTH_STONE)
                .define('E', RSItems.BASE_ESSENCE.get())
                .unlockedBy("has", has(RSItems.BASE_ESSENCE.get()))
                .save(output);
    }
}
