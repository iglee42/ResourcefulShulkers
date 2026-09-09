package fr.iglee42.resourcefulshulkers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.registries.RSRecipes;
import fr.iglee42.resourcefulshulkers.utils.RSExtraCodecs;
import net.minecraft.core.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class EnvironmentInfusionRecipe implements Recipe<ShulkerInfuserInput>, ITickableRecipe<ShulkerInfuserBlockEntity> {

    private final HolderSet<EntityType<?>> baseEntity;
    private final EntityType<?> resultEntity;
    private final CompoundTag resultNbt;
    private final HolderSet<Biome> allowedBiomes;
    private final Item particle;
    private final int minY, maxY;
    private final int auraConsumed;


    public EnvironmentInfusionRecipe(HolderSet<EntityType<?>> baseEntity, HolderSet<Biome> allowedBiomes, Item particle, int auraConsumed, int minY, int maxY, CompoundTag resultNbt, EntityType<?> resultEntity) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allowedBiomes = allowedBiomes;
        this.particle = particle;
        this.minY = minY;
        this.maxY = maxY;
        this.auraConsumed = auraConsumed;
        this.resultNbt = resultNbt;
    }

    @Override
    public boolean matches(ShulkerInfuserInput input, Level level) {
        if (level instanceof ServerLevel) {
            ShulkerAuraManager auraManager = ShulkerAuraManager.get(level);
            if (auraManager.extractAura(input.getBlockPos(), auraConsumed, true) < auraConsumed) return false;
        }
        if (!allowedBiomes.contains(input.getBiome())) return false;
        if (input.getBlockPos().getY() < (minY != -128 ? minY : level.getMinBuildHeight())) return false;
        if (input.getBlockPos().getY() > (maxY != -128 ? maxY : level.getMaxBuildHeight())) return false;
        if (input.getTarget() == null) return false;
        return input.getTarget().getType().is(baseEntity);
    }

    @Override
    public ItemStack assemble(ShulkerInfuserInput input, HolderLookup.Provider registries) {
        return getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return RSRecipes.SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        Vec3 center = Vec3.atBottomCenterOf(pos);
        if (progress >= 5 * 20)
            spawnItemParticle((ServerLevel) level, center, new Vec3(2, 0, 0), new Vec3(0, 4, 0));
    }

    private void spawnItemParticle(ServerLevel level, Vec3 pos, Vec3 startOffset, Vec3 endOffset) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Vec3 start = switch (direction) {
                case SOUTH -> pos.add(-startOffset.x, startOffset.y, -startOffset.z);
                case WEST -> pos.add(startOffset.z, startOffset.y, -startOffset.x);
                case EAST -> pos.add(-startOffset.z, startOffset.y, startOffset.x);
                default -> pos.add(startOffset.x, startOffset.y, startOffset.z);
            };
            Vec3 end = switch (direction) {
                case SOUTH -> pos.add(-endOffset.x, endOffset.y, -endOffset.z);
                case WEST -> pos.add(endOffset.z, endOffset.y, -endOffset.x);
                case EAST -> pos.add(-endOffset.z, endOffset.y, endOffset.x);
                default -> pos.add(endOffset.x, endOffset.y, endOffset.z);
            };
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, particle.getDefaultInstance()), level, start, end, 0);
        }
    }

    private void spawnCircleParticle(ServerLevel level, Vec3 center, float increment, int yOffset) {
        for (double t = 0; t <= 2 * Math.PI; t += increment) {
            double vx = 2 * Math.cos(t);
            double vz = 2 * Math.sin(t);
            Vec3 particle = new Vec3(center.x + vx, center.y() + yOffset + 0.05, center.z + vz);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, this.particle.getDefaultInstance()), level, particle, particle, 0);
        }
    }

    @Override
    public void second(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        Vec3 center = Vec3.atBottomCenterOf(pos);

        if (progress < 5 * 20) {
            int y = 5 - progress / 20;
            spawnCircleParticle((ServerLevel) level, center, 0.001F, y);
        }
        if (progress >= 5 * 20) {
            spawnCircleParticle((ServerLevel) level, center, 0.003F, 0);
        }
    }

    @Override
    public void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        Entity target = be.getCurrentTarget();
        Entity newEntity = resultEntity.create(level);
        if (newEntity == null) return;
        newEntity.load(resultNbt);
        newEntity.setPos(target.position());
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
        level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds())
                .forEach(RSAdvancements.ENVIRONMENT_INFUSION::awardTo);

        ShulkerAuraManager manager = ShulkerAuraManager.get(level);
        manager.extractAura(pos, auraConsumed, false);
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        return matches(new ShulkerInfuserInput(be), level);
    }

    public HolderSet<EntityType<?>> baseEntity() {
        return baseEntity;
    }

    public EntityType<?> resultEntity() {
        return resultEntity;
    }

    public CompoundTag resultNbt() {
        return resultNbt;
    }

    public HolderSet<Biome> allowedBiomes() {
        return allowedBiomes;
    }

    public Item particle() {
        return particle;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    public int auraConsumed() {
        return auraConsumed;
    }

    public static class Type implements RecipeType<EnvironmentInfusionRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "environment_infusion";
        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<EnvironmentInfusionRecipe> {

        public static final StreamCodec<RegistryFriendlyByteBuf, EnvironmentInfusionRecipe> STREAM_CODEC = RSExtraCodecs.composite(
                ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), EnvironmentInfusionRecipe::baseEntity,
                ByteBufCodecs.holderSet(Registries.BIOME), EnvironmentInfusionRecipe::allowedBiomes,
                ByteBufCodecs.registry(Registries.ITEM), EnvironmentInfusionRecipe::particle,
                ByteBufCodecs.INT, EnvironmentInfusionRecipe::auraConsumed,
                ByteBufCodecs.INT, EnvironmentInfusionRecipe::minY,
                ByteBufCodecs.INT, EnvironmentInfusionRecipe::maxY,
                ByteBufCodecs.COMPOUND_TAG, EnvironmentInfusionRecipe::resultNbt,
                ByteBufCodecs.registry(Registries.ENTITY_TYPE), EnvironmentInfusionRecipe::resultEntity,
                EnvironmentInfusionRecipe::new
        );
        private static final MapCodec<EnvironmentInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("base_entity").forGetter(e -> e.baseEntity),
                                Biome.LIST_CODEC.fieldOf("biomes").forGetter(e -> e.allowedBiomes),
                                BuiltInRegistries.ITEM.byNameCodec().fieldOf("particle").forGetter(e -> e.particle),
                                Codec.INT.fieldOf("aura").orElse(1500).forGetter(e -> e.auraConsumed),
                                Codec.INT.fieldOf("min_y").orElse(-128).forGetter(e -> e.minY),
                                Codec.INT.fieldOf("max_y").orElse(-128).forGetter(e -> e.maxY),
                                CompoundTag.CODEC.fieldOf("result_nbt").orElse(new CompoundTag()).forGetter(e -> e.resultNbt),
                                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("result_entity").forGetter(e -> e.resultEntity)
                        )
                        .apply(p_340782_, EnvironmentInfusionRecipe::new)
        );

        @Override
        public MapCodec<EnvironmentInfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnvironmentInfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
