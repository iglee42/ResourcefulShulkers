package fr.iglee42.resourcefulshulkers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.init.ModRecipes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private final String baseEntity;
    private final ResourceLocation resultEntity;
    private final CompoundTag resultNBT;
    private final NonNullList<Ingredient> pedestalsIngredients;
    private final int auraConsummed;

    public ShulkerItemInfusionRecipe(String baseEntity, ResourceLocation resultEntity,NonNullList<Ingredient> pedestalsIngredients,CompoundTag resultNBT,int auraConsummed) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.resultNBT = resultNBT;
        this.pedestalsIngredients = pedestalsIngredients;
        this.auraConsummed = auraConsummed;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        if (level.isClientSide) return;
        List<Ingredient> pedestalIngredients = new ArrayList<>(pedestalsIngredients);
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
                    if (getPedestalsIngredients().stream().filter(i -> !i.isEmpty()).count() >= 4) {
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
        List<Ingredient> pedestalIngredients = new ArrayList<>(pedestalsIngredients);
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
        Entity newEntity = BuiltInRegistries.ENTITY_TYPE.get(getResultEntity()).create(level);
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
                AdvancementHolder adv = p.getServer().getAdvancements().get(new ResourceLocation(ResourcefulShulkers.MODID,"item_infusion"));
                Iterator<String> it = ((ServerPlayer)p).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                while (it.hasNext()){
                    String criteria = it.next();
                    ((ServerPlayer)p).getAdvancements().award(adv,criteria);
                }
            });
        }
    }

    public boolean match(Level level,BlockPos pos,Entity target){
        boolean flag = true;
        List<Ingredient> pedestalIngredients = new ArrayList<>(getPedestalsIngredients());
        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
            if (pedestalIngredients.stream().anyMatch(i->i.test(stack)))
                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
        }

        boolean flag1;

        if (getBaseEntity().startsWith("#")){
            TagKey<EntityType<?>> tagKey = EntityTypeTags.create(getBaseEntity().substring(1));
            HolderSet.Named<EntityType<?>> tag = BuiltInRegistries.ENTITY_TYPE.getOrCreateTag(tagKey);
            flag1 = tag.stream().anyMatch(h->h.value().equals(target.getType()));
        } else {
            flag1 = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).equals(new ResourceLocation(getBaseEntity()));
        }

        boolean flag2 = pedestalIngredients.isEmpty();

        return flag && flag1 && flag2;
    }

    @Override
    public boolean canContinue(Level level, BlockPos pos, BlockState state, int progress, ShulkerInfuserBlockEntity be) {
        List<Ingredient> pedestalIngredients = new ArrayList<>(pedestalsIngredients);
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
    public ItemStack assemble(SimpleContainer p_44001_, HolderLookup.Provider p_336092_) {
        return getResultItem(p_336092_);
    }


    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_336125_) {
        return ItemStack.EMPTY;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public String getBaseEntity() {
        return baseEntity;
    }

    public ResourceLocation getResultEntity() {
        return resultEntity;
    }

    public NonNullList<Ingredient> getPedestalsIngredients() {
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

    public static class Serializer implements RecipeSerializer<ShulkerItemInfusionRecipe> {

        private static final MapCodec<ShulkerItemInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                Codec.STRING.fieldOf("baseEntity").forGetter(p_301310_ -> p_301310_.baseEntity),
                                ResourceLocation.CODEC.fieldOf("resultEntity").forGetter(p_301310_ -> p_301310_.resultEntity),
                                Ingredient.CODEC_NONEMPTY.listOf()
                                        .fieldOf("pedestalsIngredients")
                                        .flatXmap(
                                                p_301021_ -> {
                                                    Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                                    if (aingredient.length == 0) {
                                                        return DataResult.error(() -> "The recipe requires at least 1 pedestal ingredient");
                                                    } else {
                                                        return aingredient.length > 8
                                                                ? DataResult.error(() -> "There is too many pedestals ingredients. The maximum is: %s".formatted(8))
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(p_301310_->p_301310_.pedestalsIngredients),
                                CompoundTag.CODEC.fieldOf("resultNBT").orElse(new CompoundTag()).forGetter(p_301310_->p_301310_.resultNBT),
                                Codec.INT.fieldOf("aura").orElseGet(()->1500).forGetter(p_301310_->p_301310_.auraConsummed)
                        )
                        .apply(p_340782_, ShulkerItemInfusionRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerItemInfusionRecipe> STREAM_CODEC = StreamCodec.of(
                ShulkerItemInfusionRecipe.Serializer::toNetwork, ShulkerItemInfusionRecipe.Serializer::fromNetwork
        );


        @Override
        public MapCodec<ShulkerItemInfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerItemInfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShulkerItemInfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String baseEntity = buffer.readUtf();
            ResourceLocation resultEntity = buffer.readResourceLocation();
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            return new ShulkerItemInfusionRecipe(baseEntity,resultEntity,nonnulllist,buffer.readNbt(),buffer.readInt());
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShulkerItemInfusionRecipe recipe) {
            buffer.writeUtf(recipe.baseEntity);
            buffer.writeResourceLocation(recipe.resultEntity);
            buffer.writeVarInt(recipe.pedestalsIngredients.size());
            for (Ingredient ingredient : recipe.pedestalsIngredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            buffer.writeNbt(recipe.resultNBT);
            buffer.writeInt(recipe.auraConsummed);
        }
    }
}
