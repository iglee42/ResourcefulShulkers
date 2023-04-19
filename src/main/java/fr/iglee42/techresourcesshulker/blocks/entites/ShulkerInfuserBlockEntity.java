package fr.iglee42.techresourcesshulker.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.recipes.ITickableRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerInfuserBlockEntity extends SecondBlockEntity {

    public final VoxelShape WORKING_AREA;

    private EnergyStorage energyStorage = new EnergyStorage(1000000,10000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };
    private LazyOptional<EnergyStorage> lazyEnergy = LazyOptional.empty();

    private int progress = 0;

    private boolean enabled;

    private ITickableRecipe recipe;


    public ShulkerInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.SHULKER_INFUSER_BLOCK_ENTITY.get(),pos,state);
        WORKING_AREA = Shapes.box(0,0,0,1,2,1).move(pos.getX(),pos.getY(),pos.getZ());
    }


    public static void tick(Level lvl, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity entity) {
        SecondBlockEntity.tick(lvl,pos,state,entity);
        if (!lvl.isClientSide)entity.tickEntity(lvl,pos,state);
    }

    private void tickEntity(Level level, BlockPos pos, BlockState state) {

        if (progress == 15*20 || recipe == null || !hasEnoughEnergy()){
            enabled = false;
            progress = 0;
        }
        //Tick Recipe
        if (enabled){
            recipe.tick(level,pos,state,progress,this);
            //Finish Recipe
            if (progress == 15*20-1){
                spawnParticle(ParticleTypes.FIREWORK,(ServerLevel) level,Vec3.atBottomCenterOf(pos).add(0,1,0),Vec3.atBottomCenterOf(pos).add(0,2,0),50);
                recipe.finish(level,pos,state,this);
            }
            progress++;
        }
    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, SecondBlockEntity secondBlockEntity) {
        if (!level.isClientSide) {
            //Second recipe
            if (enabled){
                    if (hasEnoughEnergy()) {
                        recipe.second(level,pos,state,progress,this);
                        energyStorage.extractEnergy(1000, false);
                    }
            }

            
            //Freeze Target On the Infuser

            Mob target = level.getNearestEntity(level.getEntitiesOfClass(Mob.class, WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

            if (hasEnoughEnergy()){

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

    //START
    public void start(ITickableRecipe recipe){
        enabled = true;
        this.recipe = recipe;
        this.recipe.start(this.level,this.getBlockPos(),this.getBlockState(),this);
    }

    public boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= 1000;
    }


 

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
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
