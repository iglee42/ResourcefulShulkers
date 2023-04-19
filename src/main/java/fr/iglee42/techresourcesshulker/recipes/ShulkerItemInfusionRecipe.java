package fr.iglee42.techresourcesshulker.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShulkerItemInfusionRecipe implements Recipe<SimpleContainer>,ITickableRecipe {


    private final ResourceLocation id;
    private final ResourceLocation baseEntity,resultEntity;
    private final Ingredient[] pedestalsIngredients;

    public ShulkerItemInfusionRecipe(ResourceLocation id, ResourceLocation baseEntity, ResourceLocation resultEntity, Ingredient... pedestalsIngredients) {
        this.id = id;
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        for (int i = 0; i < 8 ; i++){
            if (pedestalsIngredients[i] == null)pedestalsIngredients[i] = Ingredient.EMPTY;
        }
        this.pedestalsIngredients = pedestalsIngredients;
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        return false;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level p_44003_) {
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHULKER_ITEM_INFUSE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShulkerItemInfusionRecipe> {
        private Type() { }
        public static final ShulkerItemInfusionRecipe.Type INSTANCE = new ShulkerItemInfusionRecipe.Type();
        public static final String ID = "shulker_item_infusion";
    }
    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShulkerItemInfusionRecipe> {
        public @NotNull ShulkerItemInfusionRecipe fromJson(ResourceLocation rs, JsonObject json) {
            JsonArray array = json.getAsJsonArray("pedestalsIngredients");
            if (array.size() < 1) throw new JsonSyntaxException("The recipe requires at least 1 pedestal ingredient");
            if (array.size() > 8) throw new JsonSyntaxException("There is too many pedestals ingredients");
            Ingredient[] ingredients = new Ingredient[8];
            AtomicInteger index = new AtomicInteger();
            array.forEach(je->{
                Ingredient ingredient =  Ingredient.fromJson(je);
                ingredient.getItems()[0].setCount(1);
                ingredients[index.get()] = ingredient;
                index.getAndIncrement();
            });
            return new ShulkerItemInfusionRecipe(rs, new ResourceLocation(JsonHelper.getString(json,"baseEntity")), new ResourceLocation(JsonHelper.getString(json,"resultEntity")),ingredients);
        }

        public ShulkerItemInfusionRecipe fromNetwork(ResourceLocation rs, FriendlyByteBuf buffer) {
           return new ShulkerItemInfusionRecipe(rs, buffer.readResourceLocation(),buffer.readResourceLocation(), buffer.readList(Ingredient::fromNetwork).toArray(new Ingredient[8]));
        }

        public void toNetwork(FriendlyByteBuf buffer, ShulkerItemInfusionRecipe recipe) {
            buffer.writeResourceLocation(recipe.baseEntity);
            buffer.writeResourceLocation(recipe.resultEntity);
            buffer.writeCollection(Arrays.stream(recipe.pedestalsIngredients).toList(),(buf, ingr) -> ingr.toNetwork(buf));
        }
    }
}
