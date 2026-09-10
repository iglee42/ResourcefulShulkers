package fr.iglee42.resourcefulshulkers.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerInfuserInput.PedestalEntry;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;
import static fr.iglee42.resourcefulshulkers.utils.RSExtraCodecs.readHolderSetValues;

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

    private final List<String> baseEntityValues;
    private @Nullable HolderSet<EntityType<?>> baseEntity;
    private final EntityType<?> resultEntity;
    private final CompoundTag resultNBT;
    private final List<Ingredient> pedestalsIngredients;
    private final int auraConsumed;

    private final ResourceLocation id;

    public ItemInfusionRecipe(ResourceLocation id, List<String> baseEntityValues, List<Ingredient> pedestalsIngredients, int auraConsumed, CompoundTag resultNBT, EntityType<?> resultEntity) {
        this.id = id;
        this.baseEntityValues = baseEntityValues;
        this.resultEntity = resultEntity;
        this.resultNBT = resultNBT;
        this.pedestalsIngredients = pedestalsIngredients.stream().filter(i -> !i.isEmpty()).toList();
        this.auraConsumed = auraConsumed;
    }

    private ItemInfusionRecipe(ResourceLocation id, HolderSet<EntityType<?>> baseEntity, List<Ingredient> pedestalsIngredients, int auraConsumed, CompoundTag resultNBT, EntityType<?> resultEntity) {
        this.id = id;
        this.baseEntityValues = new ArrayList<>(baseEntity.stream().map(h->h.unwrapKey().get().location().toString()).toList());
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
        return RSRecipes.SHULKER_ITEM_INFUSE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
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

        @Override
        public ItemInfusionRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<String> base = readHolderSetValues(json.get("base_entity"));
            List<Ingredient> ingredients = itemsFromJson(json.getAsJsonArray("pedestal_ingredients"));
            int aura = GsonHelper.getAsInt(json, "aura", 1500);
            JsonElement nbtElt = json.get("result_nbt");
            CompoundTag nbt = nbtElt != null ? CraftingHelper.getNBT(nbtElt) : new CompoundTag();
            EntityType<?> result = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(json.get("result_entity").getAsString()));
            if (result == null) throw new JsonParseException("Unknown entity type: " + json.get("result_entity").getAsString());
            return new ItemInfusionRecipe(id, base, ingredients, aura, nbt, result);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray array) {
            if (array.isEmpty()) throw new JsonParseException("Too few ingredients for item infusion recipes");
            if (array.size() > 8) throw new JsonParseException("Too many ingredients for item infusion recipes");
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < array.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(array.get(i), false);
                nonnulllist.add(ingredient);
            }

            return nonnulllist;
        }

        @Override
        public @Nullable ItemInfusionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Either<List<String>,HolderSet<EntityType<?>>> base;
            if (buf.readBoolean()) {
                base = Either.right(RSExtraCodecs.decodeHolderSet(buf, BuiltInRegistries.ENTITY_TYPE));
            } else {
                base = Either.left(buf.readCollection(ArrayList::new, FriendlyByteBuf::readUtf));
            }
            List<Ingredient> ingredients = buf.readList(Ingredient::fromNetwork);
            int aura = buf.readInt();
            CompoundTag nbt = buf.readNbt();
            var result = buf.readById(BuiltInRegistries.ENTITY_TYPE);
            if (base.left().isPresent()){
                return new ItemInfusionRecipe(id, base.left().get(), ingredients, aura, nbt, result);
            } else {
                return new ItemInfusionRecipe(id, base.right().get(), ingredients, aura, nbt, result);
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemInfusionRecipe recipe) {
            if (recipe.baseEntity != null) {
                buf.writeBoolean(true);
                RSExtraCodecs.encodeHolderSet(buf,recipe.baseEntity,BuiltInRegistries.ENTITY_TYPE);
            } else {
                buf.writeBoolean(false);
                buf.writeCollection(recipe.baseEntityValues, FriendlyByteBuf::writeUtf);
            }
            buf.writeCollection(recipe.pedestalsIngredients(), (b, i) -> i.toNetwork(b));
            buf.writeInt(recipe.auraConsumed());
            buf.writeNbt(recipe.resultNbt());
            buf.writeRegistryId(ForgeRegistries.ENTITY_TYPES, recipe.resultEntity());
        }
    }
}
