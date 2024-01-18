package fr.iglee42.techresourcesshulker.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerRecipeEnvironnement implements Recipe<SimpleContainer>, ITickableRecipe<ShulkerInfuserBlockEntity> {

    private final ResourceLocation id;
    private final ResourceLocation baseEntity,resultEntity;
    private final List<ResourceLocation> allowedBiomes;
    private final List<ResourceLocation> allowedBiomesTags;
    private final Ingredient particle;
    private final int minY,maxY;

    public ShulkerRecipeEnvironnement(ResourceLocation id, ResourceLocation baseEntity, ResourceLocation resultEntity, List<ResourceLocation> allowedBiomes, List<ResourceLocation> allowedBiomesTags, Ingredient particle, int minY, int maxY) {
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

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        if (progress >= 5*20 && progress <= 10*20) {

            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(2,0,0),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(-2,0,0),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(0,0,2),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(0,0,-2),posi.add(0,4,0),0);
        }
        if (progress >= 10*20){
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(0.5,2.05,0.5),posi.add(0.5,1.05,0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(-0.5,2.05,-0.5),posi.add(-0.5,1.05,-0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(-0.5,2.05,0.5),posi.add(-0.5,1.05,0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().getItems()[0]),(ServerLevel) level,posi.add(0.5,2.05,-0.5),posi.add(0.5,1.05,-0.5),0);
        }

    }

    @Override
    public void second(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        if (progress < 5*20) {
            int y = 0;
            if (progress / 20 == 0) y = 5;
            if (progress / 20 == 1) y = 4;
            if (progress / 20 == 2) y = 3;
            if (progress / 20 == 3) y = 2;
            if (progress / 20 == 4) y = 1;
            for (double t = 0; t <= 2 * Math.PI; t += 0.001) {
                double vx = 2 * Math.cos(t);
                double vz = 2 * Math.sin(t);
                Vec3 particle = new Vec3(posi.x + vx, posi.y() + y + 0.05, posi.z + vz);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getParticle().getItems()[0]),(ServerLevel) level, particle, particle,0);
            }
        }
        if (progress >= 5*20 && progress <= 10*20){
            for (double t = 0; t <= 2 * Math.PI; t += 0.003) {
                double vx = 2 * Math.cos(t);
                double vz = 2 * Math.sin(t);
                Vec3 particle = new Vec3(posi.x + vx, posi.y()+ 0.05, posi.z + vz);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getParticle().getItems()[0]),(ServerLevel) level, particle, particle,0);
            }
        }
    }

    @Override
    public void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be) {
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        Mob target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, be.WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        Vec3 basePos = posi.add(0,1,0);
        Entity newEntity = ForgeRegistries.ENTITIES.getValue(getResultEntity()).create(level);
        newEntity.setPos(target.position());
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state,int progress,ShulkerInfuserBlockEntity be) {
        Mob target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, be.WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        return target != null;
    }

    public static class Type implements RecipeType<ShulkerRecipeEnvironnement> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "shulker_environnement_infusion";
    }
    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShulkerRecipeEnvironnement> {
        public ShulkerRecipeEnvironnement fromJson(ResourceLocation rs, JsonObject json) {
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
                    allowedBiomesTags.add(br);
                } else {
                    ResourceLocation br = new ResourceLocation(object);
                    if (ForgeRegistries.BIOMES.containsKey(br)){
                        allowedBiomes.add(br);
                    } else {
                        throw new JsonSyntaxException("The biome \""+ br +"\" is invalid");
                    }
                }

            });

            return new ShulkerRecipeEnvironnement(rs,
                    new ResourceLocation(JsonHelper.getString(json,"baseEntity")),
                    new ResourceLocation(JsonHelper.getString(json,"resultEntity")),
                    allowedBiomes,
                    allowedBiomesTags,
                    Ingredient.of(JsonHelper.getItem(json,"particleItem")),
                    JsonHelper.getIntOrDefault(json,"minY",-64),
                    JsonHelper.getIntOrDefault(json,"maxY",320));
        }

        public ShulkerRecipeEnvironnement fromNetwork(ResourceLocation rs, FriendlyByteBuf buffer) {
            return new ShulkerRecipeEnvironnement(rs, buffer.readResourceLocation(),buffer.readResourceLocation(), buffer.readList(FriendlyByteBuf::readResourceLocation),buffer.readList(FriendlyByteBuf::readResourceLocation),Ingredient.fromNetwork(buffer),buffer.readInt(),buffer.readInt());
        }

        public void toNetwork(FriendlyByteBuf buffer, ShulkerRecipeEnvironnement recipe) {
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
