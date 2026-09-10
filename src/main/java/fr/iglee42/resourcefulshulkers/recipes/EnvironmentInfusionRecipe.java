package fr.iglee42.resourcefulshulkers.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
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
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;
import static fr.iglee42.resourcefulshulkers.utils.RSExtraCodecs.readHolderSetValues;

public class EnvironmentInfusionRecipe implements Recipe<ShulkerInfuserInput>, ITickableRecipe<ShulkerInfuserBlockEntity> {


    private final List<String> baseEntityValues;
    private @Nullable HolderSet<EntityType<?>> baseEntity;
    private final EntityType<?> resultEntity;
    private final CompoundTag resultNbt;
    private final List<String> allowedBiomesValues;
    private @Nullable HolderSet<Biome> allowedBiomes;
    private final Item particle;
    private final int minY, maxY;
    private final int auraConsumed;

    private final ResourceLocation id;

    public EnvironmentInfusionRecipe(ResourceLocation id, List<String> baseEntityValues, List<String> allowedBiomesValues, Item particle, int auraConsumed, int minY, int maxY, CompoundTag resultNbt, EntityType<?> resultEntity) {
        this.id = id;
        this.baseEntityValues = baseEntityValues;
        this.resultEntity = resultEntity;
        this.allowedBiomesValues = allowedBiomesValues;
        this.particle = particle;
        this.minY = minY;
        this.maxY = maxY;
        this.auraConsumed = auraConsumed;
        this.resultNbt = resultNbt;
    }

    private EnvironmentInfusionRecipe(ResourceLocation id, HolderSet<EntityType<?>> baseEntity, List<String> allowedBiomesValues, Item particle, int auraConsumed, int minY, int maxY, CompoundTag resultNbt, EntityType<?> resultEntity) {
        this.id = id;
        this.baseEntityValues = new ArrayList<>(baseEntity.stream().map(h->h.unwrapKey().get().location().toString()).toList());
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allowedBiomesValues = allowedBiomesValues;
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
        if (!allowedBiomes(level.registryAccess()).contains(input.getBiome())) return false;
        if (input.getBlockPos().getY() < (minY != -128 ? minY : level.getMinBuildHeight())) return false;
        if (input.getBlockPos().getY() > (maxY != -128 ? maxY : level.getMaxBuildHeight())) return false;
        if (input.getTarget() == null) return false;
        return baseEntity(level.registryAccess()).contains(input.getTarget().getType().builtInRegistryHolder());
    }

    @Override
    public ItemStack assemble(ShulkerInfuserInput input, RegistryAccess registries) {
        return getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
        if (progress >= 5 * 20) spawnItemParticle((ServerLevel) level, center, new Vec3(2, 0, 0), new Vec3(0, 4, 0));
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
        level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds()).forEach(RSAdvancements.ENVIRONMENT_INFUSION::awardTo);

        ShulkerAuraManager manager = ShulkerAuraManager.get(level);
        manager.extractAura(pos, auraConsumed, false);
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        return matches(new ShulkerInfuserInput(be), level);
    }

    public HolderSet<EntityType<?>> baseEntity(HolderLookup.Provider registries) {
        if (baseEntity == null) {
            baseEntity = RSExtraCodecs.resolveHolderSet(ForgeRegistries.ENTITY_TYPES, baseEntityValues, registries);
        }
        return baseEntity;
    }

    public EntityType<?> resultEntity() {
        return resultEntity;
    }

    public CompoundTag resultNbt() {
        return resultNbt;
    }

    public HolderSet<Biome> allowedBiomes(HolderLookup.Provider registries) {
        if (allowedBiomes == null) {
            allowedBiomes = RSExtraCodecs.resolveHolderSet(ForgeRegistries.BIOMES, allowedBiomesValues, registries);
        }
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

        @Override
        public EnvironmentInfusionRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<String> base = readHolderSetValues(json.get("base_entity"));
            List<String> biomes = readHolderSetValues(json.get("biomes"));
            Item particle = GsonHelper.getAsItem(json, "particle");
            int aura = GsonHelper.getAsInt(json, "aura", 1500);
            int minY = GsonHelper.getAsInt(json, "min_y", -128);
            int maxY = GsonHelper.getAsInt(json, "max_y", -128);
            JsonElement nbtElt = json.get("result_nbt");
            CompoundTag nbt = nbtElt != null ? CraftingHelper.getNBT(nbtElt) : new CompoundTag();
            EntityType<?> result = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(json.get("result_entity").getAsString()));
            if (result == null)
                throw new JsonParseException("Unknown entity type: " + json.get("result_entity").getAsString());
            return new EnvironmentInfusionRecipe(id, base, biomes, particle, aura, minY, maxY, nbt, result);
        }

        @Override
        public @Nullable EnvironmentInfusionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            Either<List<String>,HolderSet<EntityType<?>>> base;
            if (buf.readBoolean()) {
                base = Either.right(RSExtraCodecs.decodeHolderSet(buf, BuiltInRegistries.ENTITY_TYPE));
            } else {
                base = Either.left(buf.readCollection(ArrayList::new, FriendlyByteBuf::readUtf));
            }
            var biomes = buf.readCollection(ArrayList::new, FriendlyByteBuf::readUtf);
            Item item = buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
            int aura = buf.readInt();
            int minY = buf.readInt();
            int maxY = buf.readInt();
            CompoundTag tag = buf.readNbt();
            var result = buf.readById(BuiltInRegistries.ENTITY_TYPE);

            if (base.left().isPresent()) {
                return new EnvironmentInfusionRecipe(id, base.left().get(), biomes, item, aura, minY, maxY, tag, result);
            } else {
                return new EnvironmentInfusionRecipe(id, base.right().get(), biomes, item, aura, minY, maxY, tag, result);
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EnvironmentInfusionRecipe recipe) {
            if (recipe.baseEntity != null) {
                buf.writeBoolean(true);
                RSExtraCodecs.encodeHolderSet(buf,recipe.baseEntity,BuiltInRegistries.ENTITY_TYPE);
            } else {
                buf.writeBoolean(false);
                buf.writeCollection(recipe.baseEntityValues, FriendlyByteBuf::writeUtf);
            }
            buf.writeCollection(recipe.allowedBiomesValues, FriendlyByteBuf::writeUtf);
            buf.writeRegistryId(ForgeRegistries.ITEMS, recipe.particle);
            buf.writeInt(recipe.auraConsumed());
            buf.writeInt(recipe.minY());
            buf.writeInt(recipe.maxY());
            buf.writeNbt(recipe.resultNbt());
            buf.writeId(BuiltInRegistries.ENTITY_TYPE, recipe.resultEntity());
        }
    }
}
