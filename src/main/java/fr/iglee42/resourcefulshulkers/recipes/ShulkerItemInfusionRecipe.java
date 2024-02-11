package fr.iglee42.resourcefulshulkers.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.iglee42.igleelib.api.utils.JsonHelper;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerItemInfusionRecipe implements Recipe<SimpleContainer>, ITickableRecipe<ShulkerInfuserBlockEntity> {


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

    private final ResourceLocation id;
    private final ResourceLocation baseEntity, resultEntity;
    private final CompoundTag resultNBT;
    private final Ingredient[] pedestalsIngredients;
    private final int auraConsummed;

    public ShulkerItemInfusionRecipe(ResourceLocation id, ResourceLocation baseEntity, ResourceLocation resultEntity,CompoundTag resultNBT,int auraConsummed, Ingredient... pedestalsIngredients) {
        this.id = id;
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.resultNBT = resultNBT;
        for (int i = 0; i < 8; i++) {
            if (pedestalsIngredients[i] == null) pedestalsIngredients[i] = Ingredient.EMPTY;
        }
        this.pedestalsIngredients = pedestalsIngredients;
        this.auraConsummed = auraConsummed;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(getPedestalsIngredients()).toList());
        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
        for (int[] posXYZ : PEDESTAL_POSITION) {
            BlockPos pedestalBlockPos = pos.offset(posXYZ[0], posXYZ[1], posXYZ[2]);
            Vec3 pedestalPos = Vec3.atBottomCenterOf(pedestalBlockPos).add(0, 1.35, 0);
            ItemStack stack = ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack();
            if (!stack.isEmpty() && pedestalIngredients.stream().anyMatch(i -> i.test(stack))) {
                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
                if (progress <= 150)
                    spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, pedestalPos, Vec3.atCenterOf(pos).add(0, 3, 0), 0);
                if (progress > 150 && progress % 2 == 0) {
                    if (Arrays.stream(getPedestalsIngredients()).filter(i -> !i.isEmpty()).count() >= 4) {
                        if ((posXYZ[0] == 2 && posXYZ[2] == -2) || (posXYZ[0] == 3))
                            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(0.5, 0.5 + be.getCurrentTarget().getBbHeight(), -0.5), Vec3.atCenterOf(pos).add(0.5, 0.5, -0.5), 0);
                        if ((posXYZ[0] == -2 && posXYZ[2] == -2) || (posXYZ[2] == -3))
                            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(-0.5, 0.5 + be.getCurrentTarget().getBbHeight(), -0.5), Vec3.atCenterOf(pos).add(-0.5, 0.5, -0.5), 0);
                        if ((posXYZ[0] == -2 && posXYZ[2] == 2) || (posXYZ[0] == -3))
                            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(-0.5, 0.5 + be.getCurrentTarget().getBbHeight(), 0.5), Vec3.atCenterOf(pos).add(-0.5, 0.5, 0.5), 0);
                        if ((posXYZ[0] == 2 && posXYZ[2] == 2) || (posXYZ[2] == 3))
                            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(0.5, 0.5 + be.getCurrentTarget().getBbHeight(), 0.5), Vec3.atCenterOf(pos).add(0.5, 0.5, 0.5), 0);
                    } else {
                        spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(0.5, 1.5, -0.5), Vec3.atCenterOf(pos).add(0.5, 0.5, -0.5), 0);
                        spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(-0.5, 1.5, -0.5), Vec3.atCenterOf(pos).add(-0.5, 0.5, -0.5), 0);
                        spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(-0.5, 1.5, 0.5), Vec3.atCenterOf(pos).add(-0.5, 0.5, 0.5), 0);
                        spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, ((ShulkerPedestalBlockEntity) level.getBlockEntity(pedestalBlockPos)).getStack()), (ServerLevel) level, Vec3.atCenterOf(pos).add(0.5, 1.5, 0.5), Vec3.atCenterOf(pos).add(0.5, 0.5, 0.5), 0);

                    }
                }
            }
        }
    }

    @Override
    public void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be) {
        List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(getPedestalsIngredients()).toList());
        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) return;
            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
            if (pedestalIngredients.stream().anyMatch(i->i.test(stack))) {
                pedestalIngredients.remove(pedestalIngredients.stream().filter(i -> i.test(stack)).findFirst().get());
                ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).setStack(new ItemStack(Items.AIR));
            }
        }
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        LivingEntity target = level.getNearestEntity(level.getEntitiesOfClass(LivingEntity.class, be.WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        Vec3 basePos = posi.add(0,1,0);
        Entity newEntity = ForgeRegistries.ENTITIES.getValue(getResultEntity()).create(level);
        newEntity.load(resultNBT);
        newEntity.setPos(target.position());

        /*resultNBT.getAllKeys().forEach(s->{
            newEntity.getPersistentData().remove(s);
            newEntity.getPersistentData().put(s,resultNBT.get(s));
        });*/
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
        if (!level.isClientSide) {
            level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds()).forEach(p -> {
                Advancement adv = p.getServer().getAdvancements().getAdvancement(new ResourceLocation(ResourcefulShulkers.MODID,"item_infusion"));
                Iterator<String> it = ((ServerPlayer)p).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                while (it.hasNext()){
                    String criteria = it.next();
                    ((ServerPlayer)p).getAdvancements().award(adv,criteria);
                }
            });
        }
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(getPedestalsIngredients()).toList());
        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) return false;
            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
            if (pedestalIngredients.stream().anyMatch(i->i.test(stack)))
                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
        }
        return pedestalIngredients.isEmpty();
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

    public ResourceLocation getBaseEntity() {
        return baseEntity;
    }

    public ResourceLocation getResultEntity() {
        return resultEntity;
    }

    public Ingredient[] getPedestalsIngredients() {
        return pedestalsIngredients;
    }

    public CompoundTag getResultNBT() {
        return resultNBT;
    }

    public int getAuraConsummed() {
        return auraConsummed;
    }

    public static class Type implements RecipeType<ShulkerItemInfusionRecipe> {
        public static final ShulkerItemInfusionRecipe.Type INSTANCE = new ShulkerItemInfusionRecipe.Type();
        public static final String ID = "shulker_item_infusion";
        private Type() {}
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShulkerItemInfusionRecipe> {
        public @NotNull ShulkerItemInfusionRecipe fromJson(ResourceLocation rs, JsonObject json) {
            JsonArray array = json.getAsJsonArray("pedestalsIngredients");
            if (array.isEmpty()) throw new JsonSyntaxException("The recipe requires at least 1 pedestal ingredient");
            if (array.size() > 8) throw new JsonSyntaxException("There is too many pedestals ingredients");
            Ingredient[] ingredients = new Ingredient[8];
            AtomicInteger index = new AtomicInteger();
            array.forEach(je -> {
                Ingredient ingredient = Ingredient.fromJson(je);
                ingredient.getItems()[0].setCount(1);
                ingredients[index.get()] = ingredient;
                index.getAndIncrement();
            });
            CompoundTag tag = new CompoundTag();
            if (json.has("resultNbt")){
                try {
                    tag = TagParser.parseTag(new Gson().toJson(json.getAsJsonObject("resultNbt")));
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            return new ShulkerItemInfusionRecipe(rs, new ResourceLocation(JsonHelper.getString(json, "baseEntity")), new ResourceLocation(JsonHelper.getString(json, "resultEntity")),tag,JsonHelper.getIntOrDefault(json,"aura",1500), ingredients);
        }

        public ShulkerItemInfusionRecipe fromNetwork(ResourceLocation rs, FriendlyByteBuf buffer) {
            return new ShulkerItemInfusionRecipe(rs, buffer.readResourceLocation(), buffer.readResourceLocation(),buffer.readNbt(),buffer.readInt(), buffer.readList(Ingredient::fromNetwork).toArray(new Ingredient[8]));
        }

        public void toNetwork(FriendlyByteBuf buffer, ShulkerItemInfusionRecipe recipe) {
            buffer.writeResourceLocation(recipe.baseEntity);
            buffer.writeResourceLocation(recipe.resultEntity);
            buffer.writeNbt(recipe.resultNBT);
            buffer.writeInt(recipe.auraConsummed);
            buffer.writeCollection(Arrays.stream(recipe.pedestalsIngredients).toList(), (buf, ingr) -> ingr.toNetwork(buf));
        }
    }
}
