package fr.iglee42.techresourcesshulker.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.recipes.ShulkerEnvironnementRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class ShulkerInfuserBlockEntity extends SecondBlockEntity {

    private final VoxelShape WORKING_AREA;

    private EnergyStorage energyStorage = new EnergyStorage(1000000,10000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };
    private LazyOptional<EnergyStorage> lazyEnergy = LazyOptional.empty();

    private int progress = 0;

    private boolean enabled;

    private Mob target;
    private ShulkerEnvironnementRecipe recipe;


    public ShulkerInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.SHULKER_INFUSER_BLOCK_ENTITY.get(),pos,state);
        WORKING_AREA = Shapes.box(0,0,0,1,2,1).move(pos.getX(),pos.getY(),pos.getZ());
    }


    public static void tick(Level lvl, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity entity) {
        SecondBlockEntity.tick(lvl,pos,state,entity);
        if (!lvl.isClientSide)entity.tickEntity(lvl,pos,state);
    }

    private void tickEntity(Level level, BlockPos pos, BlockState state) {
        target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

        Vec3 posi = Vec3.atBottomCenterOf(pos);

        if (progress == 15*20 || target == null || recipe == null){
            enabled = false;
            progress = 0;
        }
        if (enabled){
            if (progress >= 5*20 && progress <= 10*20) {
                
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(2,0,0),posi.add(0,4,0),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(-2,0,0),posi.add(0,4,0),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(0,0,2),posi.add(0,4,0),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(0,0,-2),posi.add(0,4,0),0);
            }
            if (progress >= 10*20){
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(0.5,2.05,0.5),posi.add(0.5,1.05,0.5),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(-0.5,2.05,-0.5),posi.add(-0.5,1.05,-0.5),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(-0.5,2.05,0.5),posi.add(-0.5,1.05,0.5),0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level,posi.add(0.5,2.05,-0.5),posi.add(0.5,1.05,-0.5),0);
            }
            if (progress == 15*20-1){
                Vec3 basePos = posi.add(0,1,0);
                spawnParticle(ParticleTypes.FIREWORK,(ServerLevel) level,basePos,basePos.add(0,1,0),50);
                Entity newEntity = ForgeRegistries.ENTITIES.getValue(recipe.getResultEntity()).create(level);
                newEntity.setPos(target.position());
                target.remove(Entity.RemovalReason.KILLED);
                level.addFreshEntity(newEntity);
            }
            progress++;
        }
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, SecondBlockEntity secondBlockEntity) {
        Vec3 posi = Vec3.atBottomCenterOf(pos);

        if (!level.isClientSide) {

            if (enabled){
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
                        spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level, particle, particle,0);
                    }
                }
                    if (progress >= 5*20 && progress <= 10*20){
                        for (double t = 0; t <= 2 * Math.PI; t += 0.003) {
                            double vx = 2 * Math.cos(t);
                            double vz = 2 * Math.sin(t);
                            Vec3 particle = new Vec3(posi.x + vx, posi.y()+ 0.05, posi.z + vz);
                            spawnParticle(new ItemParticleOption(ParticleTypes.ITEM,recipe.getParticle().getItems()[0]),(ServerLevel) level, particle, particle,0);
                        }
                    }
            }



            if (hasEnougthEnergyToWork()){
                Vec3 particlePos = Vec3.atBottomCenterOf(pos);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0.5, 1.05, 0), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(-0.5, 1.05, 0), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0, 1.05, 0.5), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0, 1.05, -0.5), particlePos.add(0, 1.1, 0),0);

                if (target != null) {
                    target.setNoAi(true);
                    energyStorage.extractEnergy(1000, false);
                }
            } else {
                if (target != null && target.isNoAi())target.setNoAi(false);
            }

        }
    }

    public void enable(ShulkerEnvironnementRecipe recipe){
        enabled = true;
        this.recipe = recipe;
    }

    private boolean hasEnougthEnergyToWork() {
        return energyStorage.getEnergyStored() >= 1000;
    }


    private static <T extends ParticleOptions> void spawnParticle(T particleType, ServerLevel level , Vec3 fromPos, Vec3 goPos, int count) {
        if (level == null || level.isClientSide())
            return;


        double x = fromPos.x();
        double y = fromPos.y();
        double z = fromPos.z();

        double velX = goPos.x() - fromPos.x();
        double velY = goPos.y() - fromPos.y();
        double velZ = goPos.z() - fromPos.z();

        level.sendParticles(particleType, x, y, z, count, velX, velY, velZ, 0.09D);
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergy.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergy = LazyOptional.of(()->energyStorage);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy",energyStorage.serializeNBT());
        tag.putInt("progress",progress);
        tag.putBoolean("enabled",enabled);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("energy"));
        progress = tag.getInt("progress");
        enabled = tag.getBoolean("enabled");
    }
}
