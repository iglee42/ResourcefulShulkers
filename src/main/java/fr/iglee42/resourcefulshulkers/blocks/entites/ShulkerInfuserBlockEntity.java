package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerInfuserBlockEntity extends SecondBlockEntity {

    public final VoxelShape WORKING_AREA;

    private int progress = 0;

    private boolean enabled;

    private ITickableRecipe<ShulkerInfuserBlockEntity> recipe;


    public ShulkerInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHULKER_INFUSER_BLOCK_ENTITY.get(),pos,state);
        WORKING_AREA = Shapes.box(0,0,0,1,2,1).move(pos.getX(),pos.getY(),pos.getZ());
    }


    public static void tick(Level lvl, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity entity) {
        SecondBlockEntity.tick(lvl,pos,state,entity);
        if (!lvl.isClientSide)entity.tickEntity(lvl,pos,state);
    }

    private void tickEntity(Level level, BlockPos pos, BlockState state) {

        if (progress == 15*20 || recipe == null || !hasEnoughAura() || !recipe.canContinue(level,pos,state,progress,this) || getCurrentTarget() == null){
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
                    if (hasEnoughAura()) {
                        recipe.second(level,pos,state,progress,this);
                        ShulkerAuraManager.get(level).extractAura(pos,100);
                    }
            }

            
            //Freeze Target On the Infuser

            LivingEntity target = level.getNearestEntity(level.getEntitiesOfClass(LivingEntity.class, WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());

            if (hasEnoughAura()){

                Vec3 particlePos = Vec3.atBottomCenterOf(pos);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0.5, 1.05, 0), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(-0.5, 1.05, 0), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0, 1.05, 0.5), particlePos.add(0, 1.1, 0),0);
                spawnParticle(ParticleTypes.END_ROD,(ServerLevel) level,particlePos.add(0, 1.05, -0.5), particlePos.add(0, 1.1, 0),0);

                if (target instanceof Mob m) {
                    m.setNoAi(true);
                    ShulkerAuraManager.get(level).extractAura(pos,100);
                }
            } else {
                if (target instanceof Mob m && m.isNoAi())m.setNoAi(false);
            }

        }
    }

    public LivingEntity getCurrentTarget(){
        return level.getNearestEntity(level.getEntitiesOfClass(LivingEntity.class, WORKING_AREA.bounds()), TargetingConditions.DEFAULT, null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());

    }

    //START
    public void start(ITickableRecipe<ShulkerInfuserBlockEntity> recipe){
        enabled = true;
        this.recipe = recipe;
        this.recipe.start(this.level,this.getBlockPos(),this.getBlockState(),this);
    }

    public boolean hasEnoughAura() {
        if (level.isClientSide) return false;
        return ShulkerAuraManager.get(level).getAura(getBlockPos()) >= 100;
    }




    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag,provider);
        tag.putInt("progress",progress);
        tag.putBoolean("enabled",enabled);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        progress = tag.getInt("progress");
        enabled = tag.getBoolean("enabled");
    }

}
