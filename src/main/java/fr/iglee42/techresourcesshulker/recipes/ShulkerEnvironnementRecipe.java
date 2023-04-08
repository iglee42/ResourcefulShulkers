package fr.iglee42.techresourcesshulker.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;

public class ShulkerEnvironnementRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ResourceLocation baseEntity,resultEntity;
    private final List<ResourceLocation> allowedBiomes;
    private final List<ResourceLocation> allowedBiomesTags;
    private final Ingredient particle;
    private final int minY,maxY;

    public ShulkerEnvironnementRecipe(ResourceLocation id, ResourceLocation baseEntity, ResourceLocation resultEntity, List<ResourceLocation> allowedBiomes, List<ResourceLocation> allowedBiomesTags, Ingredient particle, int minY, int maxY) {
        this.id = id;
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allowedBiomes = allowedBiomes;
        this.allowedBiomesTags = allowedBiomesTags;
        this.particle = particle;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public boolean matches(SimpleContainer p_44002_, Level p_44003_) {
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return ItemStack.EMPTY;
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
        return ModRecipes.SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public ResourceLocation getBaseEntity() {
        return baseEntity;
    }

    public ResourceLocation getResultEntity() {
        return resultEntity;
    }

    public List<ResourceLocation> getAllowedBiomes() {
        return allowedBiomes;
    }

    public List<ResourceLocation> getAllowedBiomesTags() {
        return allowedBiomesTags;
    }

    public Ingredient getParticle() {
        return particle;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public static class Type implements RecipeType<ShulkerEnvironnementRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "shulker_environnement_recipe";
    }
    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShulkerEnvironnementRecipe> {
        public ShulkerEnvironnementRecipe fromJson(ResourceLocation rs, JsonObject json) {
            JsonArray biomes = json.getAsJsonArray("biomes");
            if (biomes == null){
                throw new JsonSyntaxException("Missing \"biomes\" array in the recipe");
            }
            List<ResourceLocation> allowedBiomes = new ArrayList<>(),allowedBiomesTags = new ArrayList<>();
            biomes.forEach(j->{
                String object = j.getAsString();
                if (!object.contains(":")){
                    throw new JsonSyntaxException("The array \"biomes\" contains a not valid indenfier : \""+object+"\"");
                }

                if (object.startsWith("#")){
                    ResourceLocation br = new ResourceLocation(object.substring(1));

                    if (ForgeRegistries.BIOMES.tags().isKnownTagName(new TagKey<>(Registry.BIOME_REGISTRY,br))){
                        allowedBiomesTags.add(br);
                    } else {
                        throw new JsonSyntaxException("The biome tag \""+ br +"\" is invalid");
                    }
                } else {
                    ResourceLocation br = new ResourceLocation(object);
                    if (ForgeRegistries.BIOMES.containsKey(br)){
                        allowedBiomes.add(br);
                    } else {
                        throw new JsonSyntaxException("The biome \""+ br +"\" is invalid");
                    }
                }

            });

            return new ShulkerEnvironnementRecipe(rs,
                    new ResourceLocation(JsonHelper.getString(json,"baseEntity")),
                    new ResourceLocation(JsonHelper.getString(json,"resultEntity")),
                    allowedBiomes,
                    allowedBiomesTags,
                    Ingredient.of(JsonHelper.getItem(json,"particleItem")),
                    JsonHelper.getIntOrDefault(json,"minY",-64),
                    JsonHelper.getIntOrDefault(json,"maxY",320));
        }

        public ShulkerEnvironnementRecipe fromNetwork(ResourceLocation rs, FriendlyByteBuf buffer) {
            return new ShulkerEnvironnementRecipe(rs, buffer.readResourceLocation(),buffer.readResourceLocation(), buffer.readList(FriendlyByteBuf::readResourceLocation),buffer.readList(FriendlyByteBuf::readResourceLocation),Ingredient.fromNetwork(buffer),buffer.readInt(),buffer.readInt());
        }

        public void toNetwork(FriendlyByteBuf buffer, ShulkerEnvironnementRecipe recipe) {
            buffer.writeResourceLocation(recipe.baseEntity);
            buffer.writeResourceLocation(recipe.resultEntity);
            buffer.writeCollection(recipe.allowedBiomes, FriendlyByteBuf::writeResourceLocation);
            buffer.writeCollection(recipe.allowedBiomesTags, FriendlyByteBuf::writeResourceLocation);
            recipe.particle.toNetwork(buffer);
            buffer.writeInt(recipe.minY);
            buffer.writeInt(recipe.maxY);
        }
    }
}
