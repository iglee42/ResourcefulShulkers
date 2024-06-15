package fr.iglee42.resourcefulshulkers.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModRecipes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.commons.compress.utils.Lists;

import java.util.Iterator;
import java.util.List;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerRecipeEnvironnement implements Recipe<SimpleContainer>, ITickableRecipe<ShulkerInfuserBlockEntity> {

    private final String baseEntity;
    private final ResourceLocation resultEntity;
    private final List<String> allowedBiomes;
    private final Holder<Item> particle;
    private final int minY,maxY;
    private final int auraConsummed;


    public ShulkerRecipeEnvironnement(String baseEntity, ResourceLocation resultEntity, List<String> allowedBiomes, Holder<Item> particle, int minY, int maxY, int auraConsummed) {
        this.baseEntity = baseEntity;
        this.resultEntity = resultEntity;
        this.allowedBiomes = allowedBiomes;
        this.particle = particle;
        this.minY = minY;
        this.maxY = maxY;
        this.auraConsummed = auraConsummed;
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
        return ModRecipes.ENVIRONNEMENT_SERIALIZER.get();
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

    public List<String> getAllowedBiomes() {
        return allowedBiomes;
    }


    public Holder<Item> getParticle() {
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

            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(2,0,0),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(-2,0,0),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(0,0,2),posi.add(0,4,0),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(0,0,-2),posi.add(0,4,0),0);
        }
        if (progress >= 10*20){
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(0.5,2.05,0.5),posi.add(0.5,1.05,0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(-0.5,2.05,-0.5),posi.add(-0.5,1.05,-0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(-0.5,2.05,0.5),posi.add(-0.5,1.05,0.5),0);
            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, getParticle().value().getDefaultInstance()),(ServerLevel) level,posi.add(0.5,2.05,-0.5),posi.add(0.5,1.05,-0.5),0);
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
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getParticle().value().getDefaultInstance()),(ServerLevel) level, particle, particle,0);
            }
        }
        if (progress >= 5*20 && progress <= 10*20){
            for (double t = 0; t <= 2 * Math.PI; t += 0.003) {
                double vx = 2 * Math.cos(t);
                double vz = 2 * Math.sin(t);
                Vec3 particle = new Vec3(posi.x + vx, posi.y()+ 0.05, posi.z + vz);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getParticle().value().getDefaultInstance()),(ServerLevel) level, particle, particle,0);
            }
        }
    }

    @Override
    public void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be) {
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        Mob target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, be.WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        Vec3 basePos = posi.add(0,1,0);
        Entity newEntity = BuiltInRegistries.ENTITY_TYPE.get(getResultEntity()).create(level);
        newEntity.setPos(target.position());
        target.remove(Entity.RemovalReason.KILLED);
        level.addFreshEntity(newEntity);
        if (!level.isClientSide) {
            level.getEntitiesOfClass(Player.class, Shapes.box(0, -2.5, 0, 5, 5, 5).move(pos.getX(), pos.getY(), pos.getZ()).bounds()).forEach(p -> {
                AdvancementHolder adv = p.getServer().getAdvancements().get(new ResourceLocation(ResourcefulShulkers.MODID,"environment_infusion"));
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
        Mob target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, be.WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        return target != null;
    }

    public boolean match(Level level,BlockPos pos,Entity target){
        boolean flag;

        if (getBaseEntity().startsWith("#")){
            TagKey<EntityType<?>> tagKey = EntityTypeTags.create(getBaseEntity().substring(1));
            HolderSet.Named<EntityType<?>> tag = BuiltInRegistries.ENTITY_TYPE.getOrCreateTag(tagKey);
            flag = tag.stream().anyMatch(h->h.value().equals(target.getType()));
        } else {
            flag = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).equals(new ResourceLocation(getBaseEntity()));
        }
        Holder<Biome> b = level.getBiomeManager().getBiome(pos);
        boolean flag1 = getAllowedBiomes().stream().anyMatch(biome->{
            if (biome.startsWith("#")){
                TagKey<Biome> tagKey = BiomeTags.create(getBaseEntity().substring(1));
                return b.is(tagKey);
            } else {
                return b.is(new ResourceLocation(biome));
            }
        });
        boolean flag2 = pos.getY() >= getMinY() && pos.getY() <= getMaxY();
        return flag && flag1 && flag2;
    }

    public int getAuraConsummed() {
        return auraConsummed;
    }

    public static class Type implements RecipeType<ShulkerRecipeEnvironnement> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "shulker_environnement_infusion";
    }
    public static class Serializer implements RecipeSerializer<ShulkerRecipeEnvironnement> {

        private static final MapCodec<ShulkerRecipeEnvironnement> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                Codec.STRING.fieldOf("baseEntity").forGetter(p_301310_ -> p_301310_.baseEntity),
                                ResourceLocation.CODEC.fieldOf("resultEntity").forGetter(p_301310_ -> p_301310_.resultEntity),
                                Codec.STRING.listOf().fieldOf("allowedBiomes").orElse(Lists.newArrayList()).forGetter(p_301310_ -> p_301310_.allowedBiomes),
                                ItemStack.ITEM_NON_AIR_CODEC.fieldOf("particleItem").forGetter(p_301310_->p_301310_.particle),
                                Codec.INT.fieldOf("minY").orElseGet(()->-64).forGetter(p_301310_->p_301310_.minY),
                                Codec.INT.fieldOf("maxY").orElseGet(()->320).forGetter(p_301310_->p_301310_.maxY),
                                Codec.INT.fieldOf("aura").orElseGet(()->1500).forGetter(p_301310_->p_301310_.auraConsummed)
                        )
                        .apply(p_340782_, ShulkerRecipeEnvironnement::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerRecipeEnvironnement> STREAM_CODEC = StreamCodec.of(
                ShulkerRecipeEnvironnement.Serializer::toNetwork, ShulkerRecipeEnvironnement.Serializer::fromNetwork
        );


        @Override
        public MapCodec<ShulkerRecipeEnvironnement> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerRecipeEnvironnement> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShulkerRecipeEnvironnement fromNetwork(RegistryFriendlyByteBuf buffer) {
            return new ShulkerRecipeEnvironnement(buffer.readUtf(),buffer.readResourceLocation(), buffer.readList(FriendlyByteBuf::readUtf),ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buffer),buffer.readInt(),buffer.readInt(),buffer.readInt());
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ShulkerRecipeEnvironnement recipe) {
            buffer.writeUtf(recipe.baseEntity);
            buffer.writeResourceLocation(recipe.resultEntity);
            buffer.writeCollection(recipe.allowedBiomes, FriendlyByteBuf::writeUtf);
            ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buffer,recipe.particle);
            buffer.writeInt(recipe.minY);
            buffer.writeInt(recipe.maxY);
            buffer.writeInt(recipe.auraConsummed);
        }
    }
}
