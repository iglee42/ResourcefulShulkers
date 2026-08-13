package fr.iglee42.resourcefulshulkers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerInfuserInput.PedestalEntry;
import fr.iglee42.resourcefulshulkers.registries.RSRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ItemInfusionRecipe implements Recipe<ShulkerInfuserInput>, ITickableRecipe<ShulkerInfuserBlockEntity> {

    public static final int[][] PEDESTAL_POSITION = new int[][]{
            {3, 0, 0},
            {-3, 0, 0},
            {0, 0, 3},
            {0, 0, -3},
            {2, 0, 2},
            {-2, 0, 2},
            {-2, 0, -2},
            {2, 0, -2}
    };

    private final HolderSet<EntityType<?>> baseEntity;
    private final EntityType<?> resultEntity;
    private final CompoundTag resultNBT;
    private final List<Ingredient> pedestalsIngredients;
    private final int auraConsumed;

    public ItemInfusionRecipe(HolderSet<EntityType<?>> baseEntity, List<Ingredient> pedestalsIngredients, int auraConsumed, CompoundTag resultNBT, EntityType<?> resultEntity) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.resultNBT = resultNBT;
        this.pedestalsIngredients = pedestalsIngredients.stream().filter(i -> !i.isEmpty()).toList();
        this.auraConsumed = auraConsumed;
    }

    private static RecipeMatch matchIngredients(List<Ingredient> ingredients, List<PedestalEntry> pedestals, boolean consume) {
        if (ingredients.size() > pedestals.size()) {
            return RecipeMatch.FAILED;
        }

        boolean[] used = new boolean[pedestals.size()];
        List<PedestalEntry> consumed = consume ? new ArrayList<>(ingredients.size()) : List.of();

        for (Ingredient ingredient : ingredients) {
            boolean found = false;

            for (int i = 0; i < pedestals.size(); i++) {
                if (used[i]) {
                    continue;
                }

                PedestalEntry pedestal = pedestals.get(i);

                if (ingredient.test(pedestal.stack())) {
                    used[i] = true;
                    found = true;

                    if (consume) {
                        consumed.add(pedestal);
                    }

                    break;
                }
            }

            if (!found) {
                return RecipeMatch.FAILED;
            }
        }

        return new RecipeMatch(true, consumed);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        List<Ingredient> pedestalIngredients = new ArrayList<>(pedestalsIngredients);
        for (int[] posXYZ : PEDESTAL_POSITION) {
            BlockPos pedestalBlockPos = pos.offset(posXYZ[0], posXYZ[1], posXYZ[2]);
            Vec3 pedestalPos = Vec3.atBottomCenterOf(pedestalBlockPos).add(0, 1.35, 0);
            if (!(level.getBlockEntity(pedestalBlockPos) instanceof ShulkerPedestalBlockEntity pedestal)) return;
            ItemStack stack = pedestal.getStack();
            if (stack.isEmpty()) continue;
            Optional<Ingredient> matchingIngredient = pedestalIngredients.stream().filter(i -> i.test(stack)).findFirst();
            if (matchingIngredient.isPresent()) {
                pedestalIngredients.remove(matchingIngredient.get());
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, pedestal.getStack()), (ServerLevel) level, pedestalPos, Vec3.atCenterOf(pos).add(0, 3, 0), 0);
            }
        }
    }

    @Override
    public void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        Entity target = be.getCurrentTarget();

        Entity newEntity = resultEntity.create(level);
        if (newEntity == null) return;
        newEntity.load(resultNBT);
        newEntity.setPos(target.position());
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
        level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds())
                .forEach(RSAdvancements.ITEM_INFUSION::awardTo);

        ShulkerAuraManager manager = ShulkerAuraManager.get(level);
        manager.extractAura(pos, auraConsumed, false);

        RecipeMatch match = matchIngredients(pedestalsIngredients, new ShulkerInfuserInput(be).getPedestals(), true);
        if (!match.matches()) return;
        match.consumedPedestals().forEach(p -> p.deleteItem(level));
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        return matches(new ShulkerInfuserInput(be), level);
    }

    @Override
    public boolean matches(ShulkerInfuserInput input, Level level) {
        if (!level.isClientSide) {
            ShulkerAuraManager manager = ShulkerAuraManager.get(level);
            if (manager.extractAura(input.getBlockPos(), auraConsumed, true) < auraConsumed) return false;
        }
        if (!matchIngredients(pedestalsIngredients, input.getPedestals(), false).matches()) return false;
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
        return RSRecipes.SHULKER_ITEM_INFUSE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public HolderSet<EntityType<?>> baseEntity() {
        return baseEntity;
    }

    public EntityType<?> resultEntity() {
        return resultEntity;
    }

    public CompoundTag resultNbt() {
        return resultNBT;
    }

    public List<Ingredient> pedestalsIngredients() {
        return pedestalsIngredients;
    }

    public int auraConsumed() {
        return auraConsumed;
    }

    private record RecipeMatch(boolean matches, List<PedestalEntry> consumedPedestals) {
        public static final RecipeMatch FAILED = new RecipeMatch(false, List.of());
    }

    public static class Type implements RecipeType<ItemInfusionRecipe> {
        public static final ItemInfusionRecipe.Type INSTANCE = new ItemInfusionRecipe.Type();
        public static final String ID = "item_infusion";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<ItemInfusionRecipe> {

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemInfusionRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), ItemInfusionRecipe::baseEntity,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), ItemInfusionRecipe::pedestalsIngredients,
                ByteBufCodecs.INT, ItemInfusionRecipe::auraConsumed,
                ByteBufCodecs.COMPOUND_TAG, ItemInfusionRecipe::resultNbt,
                ByteBufCodecs.registry(Registries.ENTITY_TYPE), ItemInfusionRecipe::resultEntity,
                ItemInfusionRecipe::new
        );
        private static final MapCodec<ItemInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("base_entity").forGetter(e -> e.baseEntity),
                                Ingredient.CODEC_NONEMPTY.listOf(1, 8).fieldOf("pedestal_ingredients").forGetter(e -> e.pedestalsIngredients),
                                Codec.INT.fieldOf("aura").orElse(1500).forGetter(e -> e.auraConsumed),
                                CompoundTag.CODEC.fieldOf("result_nbt").orElse(new CompoundTag()).forGetter(e -> e.resultNBT),
                                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("result_entity").forGetter(e -> e.resultEntity)
                        ).apply(instance, ItemInfusionRecipe::new)
        );

        @Override
        public MapCodec<ItemInfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemInfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
