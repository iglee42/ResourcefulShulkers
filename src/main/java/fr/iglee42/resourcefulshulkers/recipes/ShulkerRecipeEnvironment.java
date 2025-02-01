package fr.iglee42.resourcefulshulkers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerRecipeEnvironment implements Recipe<RecipeInput>, ITickableRecipe<ShulkerInfuserBlockEntity> {

    private final ResourceLocation baseEntity,resultEntity;
    private final CompoundTag resultNbt;
    private final List<String> allAllowedBiomes;
    private final List<ResourceLocation> allowedBiomes;
    private final List<ResourceLocation> allowedBiomesTags;
    private final Ingredient particle;
    private final int minY,maxY;
    private final int auraConsumed;


    public ShulkerRecipeEnvironment(ResourceLocation baseEntity, List<String> allAllowedBiomes, Ingredient particle, int auraConsumed, int minY, int maxY, CompoundTag resultNbt, ResourceLocation resultEntity) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allAllowedBiomes = allAllowedBiomes;
        this.allowedBiomes = allAllowedBiomes.stream().filter(s->!s.startsWith("#")).map(ResourceLocation::parse).toList();
        this.allowedBiomesTags = allAllowedBiomes.stream().filter(s->s.startsWith("#")).map(s->ResourceLocation.parse(s.substring(1))).toList();
        this.particle = particle;
        this.minY = minY;
        this.maxY = maxY;
        this.auraConsumed = auraConsumed;
        this.resultNbt = resultNbt;
    }
    public ShulkerRecipeEnvironment(ResourceLocation baseEntity, List<ResourceLocation> allowedBiomes, List<ResourceLocation> allowedBiomesTags, Ingredient particle, int auraConsumed, int minY, int maxY, CompoundTag resultNbt, ResourceLocation resultEntity) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allAllowedBiomes = new ArrayList<>(allowedBiomes.stream().map(ResourceLocation::toString).toList());
        this.allAllowedBiomes.addAll(allowedBiomesTags.stream().map(r->"#" + r.toString()).toList());
        this.allowedBiomes = allowedBiomes;
        this.allowedBiomesTags = allowedBiomesTags;
        this.particle = particle;
        this.auraConsumed = auraConsumed;
        this.minY = minY;
        this.maxY = maxY;
        this.resultNbt = resultNbt;
    }

    @Override
    public boolean matches(RecipeInput p_44002_, Level p_44003_) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput p_44001_, HolderLookup.Provider p_267165_) {
        return getResultItem(p_267165_);
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_267052_) {
        return ItemStack.EMPTY;
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

        Entity target = be.getCurrentTarget();

        Vec3 basePos = posi.add(0,1,0);
        Entity newEntity = BuiltInRegistries.ENTITY_TYPE.get(getResultEntity()).create(level);
        newEntity.load(resultNbt);
        newEntity.setPos(target.position());
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
        if (!level.isClientSide) {
            level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds()).forEach(p -> {
                AdvancementHolder adv = p.getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(ResourcefulShulkers.MODID,"environment_infusion"));
                Iterator<String> it = ((ServerPlayer)p).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                while (it.hasNext()){
                    String criteria = it.next();
                    ((ServerPlayer)p).getAdvancements().award(adv,criteria);
                }
            });
        }
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state,int progress,ShulkerInfuserBlockEntity be) {
        return be.getCurrentTarget() != null;
    }

    public int getAuraConsumed() {
        return auraConsumed;
    }

    public static class Type implements RecipeType<ShulkerRecipeEnvironment> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "shulker_environment_infusion";
    }
    public static class Serializer implements RecipeSerializer<ShulkerRecipeEnvironment> {

        private static final MapCodec<ShulkerRecipeEnvironment> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                ResourceLocation.CODEC.fieldOf("baseEntity").forGetter(e->e.baseEntity),
                                Codec.STRING.listOf().fieldOf("biomes").forGetter(e-> e.allAllowedBiomes),
                                Ingredient.CODEC.fieldOf("particle").forGetter(e-> e.particle),
                                Codec.INT.fieldOf("aura").orElse(1500).forGetter(e->e.auraConsumed),
                                Codec.INT.fieldOf("minY").orElse(-64).forGetter(e->e.minY),
                                Codec.INT.fieldOf("maxY").orElse(320).forGetter(e->e.maxY),
                                CompoundTag.CODEC.fieldOf("resultNbt").orElse(new CompoundTag()).forGetter(e-> e.resultNbt),
                                ResourceLocation.CODEC.fieldOf("resultEntity").forGetter(e->e.resultEntity)
                        )
                        .apply(p_340782_, ShulkerRecipeEnvironment::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerRecipeEnvironment> STREAM_CODEC = StreamCodec.of(
                ShulkerRecipeEnvironment.Serializer::toNetwork, ShulkerRecipeEnvironment.Serializer::fromNetwork
        );

        private static ShulkerRecipeEnvironment fromNetwork(RegistryFriendlyByteBuf buffer) {
            return new ShulkerRecipeEnvironment(buffer.readResourceLocation(),  buffer.readList(FriendlyByteBuf::readResourceLocation),buffer.readList(FriendlyByteBuf::readResourceLocation),Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),buffer.readInt(),buffer.readInt(),buffer.readInt(),buffer.readNbt(), buffer.readResourceLocation());
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShulkerRecipeEnvironment recipe) {
            buffer.writeResourceLocation(recipe.baseEntity);
            buffer.writeCollection(recipe.allowedBiomes, FriendlyByteBuf::writeResourceLocation);
            buffer.writeCollection(recipe.allowedBiomesTags, FriendlyByteBuf::writeResourceLocation);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.particle);
            buffer.writeInt(recipe.auraConsumed);
            buffer.writeInt(recipe.minY);
            buffer.writeInt(recipe.maxY);
            buffer.writeNbt(recipe.resultNbt);
            buffer.writeResourceLocation(recipe.resultEntity);
        }

        @Override
        public MapCodec<ShulkerRecipeEnvironment> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerRecipeEnvironment> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
