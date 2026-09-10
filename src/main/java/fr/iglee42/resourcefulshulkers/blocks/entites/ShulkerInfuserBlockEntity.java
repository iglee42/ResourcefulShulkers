package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerInfuserInput;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSRecipes;
import fr.iglee42.resourcefulshulkers.utils.CommonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerInfuserBlockEntity extends SecondBlockEntity {

    private int progress = 0;
    private ITickableRecipe<ShulkerInfuserBlockEntity> recipe;


    public ShulkerInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(RSBlockEntities.SHULKER_INFUSER.get(), pos, state);
    }

    public void tickEntity(Level level, BlockPos pos, BlockState state) {
        SecondBlockEntity.tick(level, pos, state, this);
        if (level.isClientSide) return;
        if (recipe == null && level.getBestNeighborSignal(pos) > 0){
            recipe = findRecipe().orElse(null);
        }
        if (recipe == null) return;
        if (!recipe.canContinue(level, pos, state, progress, this)) {
            progress = 0;
            recipe = null;
            return;
        }

        recipe.tick(level, pos, state, progress, this);
        progress++;

        if (progress < RSServerConfig.INFUSER_DURATION.get()) return;

        spawnParticle(ParticleTypes.FIREWORK, (ServerLevel) level, Vec3.atBottomCenterOf(pos).add(0, 1, 0), Vec3.atBottomCenterOf(pos).add(0, 2, 0), 50);
        recipe.finish(level, pos, state, this);
        progress = 0;
        recipe = null;

    }

    @Override
    protected void second(Level level, BlockPos pos, BlockState state, SecondBlockEntity secondBlockEntity) {
        if (level.isClientSide) return;

        Entity target = getCurrentTarget();
        ShulkerAuraManager manager = ShulkerAuraManager.get(level);


        int noAiAura = RSServerConfig.INFUSER_NO_AI_AURA.get();
        if (noAiAura > -1) {
            if (manager.extractAura(pos, noAiAura, true) >= noAiAura) {
                Vec3 particlePos = Vec3.atBottomCenterOf(pos);
                spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, particlePos.add(0.5, 1.05, 0), particlePos.add(0, 1.1, 0), 0);
                spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, particlePos.add(-0.5, 1.05, 0), particlePos.add(0, 1.1, 0), 0);
                spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, particlePos.add(0, 1.05, 0.5), particlePos.add(0, 1.1, 0), 0);
                spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, particlePos.add(0, 1.05, -0.5), particlePos.add(0, 1.1, 0), 0);

                if (target instanceof Mob m) {
                    m.setSilent(true);
                    m.setNoAi(true);
                    manager.extractAura(pos, noAiAura, false);
                }
            } else if (target instanceof Mob m && m.isNoAi()) {
                m.setSilent(false);
                m.setNoAi(false);
            }
        }

        if (recipe == null) return;
        recipe.second(level, pos, state, progress, this);



    }

    public List<ShulkerInfuserInput.PedestalEntry> getPedestals(){
        if (level == null || level.isClientSide) return List.of();
        List<ShulkerInfuserInput.PedestalEntry> pedestals = new ArrayList<>();
        for (int[] offset : ItemInfusionRecipe.PEDESTAL_POSITION){
            BlockPos pos = getBlockPos().offset(offset[0], offset[1], offset[2]);
            if (!(level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity pedestal)) continue;
            pedestals.add(new ShulkerInfuserInput.PedestalEntry(pos, pedestal.getStack()));
        }
        return pedestals;
    }

    public Entity getCurrentTarget() {
        return CommonUtils.getEntityOnBlock((ServerLevel) level, getBlockPos());
    }

    public void start(ITickableRecipe<ShulkerInfuserBlockEntity> recipe) {
        this.recipe = recipe;
        this.recipe.start(this.level, this.getBlockPos(), this.getBlockState(), this);
    }

    public Optional<ITickableRecipe<ShulkerInfuserBlockEntity>> findRecipe(){
        if (level == null || level.isClientSide) return Optional.empty();
        return RSRecipes.findShulkerInfuserRecipe(level, this);
    }

}
