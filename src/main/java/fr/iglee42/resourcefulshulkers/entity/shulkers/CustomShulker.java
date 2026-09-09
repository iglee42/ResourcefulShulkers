package fr.iglee42.resourcefulshulkers.entity.shulkers;

import fr.iglee42.resourcefulshulkers.blocks.entites.PurpurTargetBlockEntity;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public abstract class CustomShulker extends Shulker {

    private @Nullable BlockPos target;

    public CustomShulker(EntityType<? extends CustomShulker> p_33404_, Level p_33405_) {
        super(p_33404_, p_33405_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
        this.goalSelector.addGoal(7, new PeekGoal());
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!super.hurt(source, damage))
            return false;
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            Entity directEntity = source.getDirectEntity();
            if (directEntity != null && directEntity instanceof ResourceShulkerBullet bullet)
                hitByResourceBullet(bullet);
        }
        return true;
    }

    protected void hitByResourceBullet(ResourceShulkerBullet bullet) {
    }

    public @Nullable BlockPos getBlockTarget() {
        return target;
    }

    public BlockPos setBlockTarget(@Nullable BlockPos pos) {
        this.target = pos;
        return pos;
    }

    @Override
    protected void hitByShulkerBullet() {
    }

    protected class FindTarget extends Goal {

        private final int randomInterval;
        private @Nullable BlockPos target;

        public FindTarget() {
            this.randomInterval = reducedTickDelay(10);
        }

        private static boolean canSeeBlock(CustomShulker shulker, Vec3 eyePos, BlockPos pos) {

            Level level = shulker.level();

            Vec3 center = Vec3.atCenterOf(pos);

            BlockHitResult result = level.clip(new ClipContext(
                    eyePos,
                    center,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    shulker
            ));

            return result.getType() == HitResult.Type.BLOCK
                    && result.getBlockPos().equals(pos);
        }

        @Override
        public boolean canUse() {
            if (this.randomInterval > 0 && getRandom().nextInt(this.randomInterval) != 0) {
                return false;
            }
            findTarget();
            return target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return CustomShulker.this.getBlockTarget() != null &&
                    canSeeBlock(CustomShulker.this, CustomShulker.this.getEyePosition(), CustomShulker.this.getBlockTarget())
                    && CustomShulker.this.level().getBlockEntity(CustomShulker.this.getBlockTarget()) instanceof PurpurTargetBlockEntity be
                    && Objects.equals(be.ownerUUID(), CustomShulker.this.getUUID());
        }

        protected void findTarget() {
            BlockPos origin = CustomShulker.this.blockPosition();

            Vec3 eyePos = CustomShulker.this.getEyePosition();

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            double bestDistance = Double.MAX_VALUE;
            BlockPos best = null;

            int targetRadius = RSServerConfig.TARGET_RADIUS.getAsInt();
            for (int dx = -targetRadius; dx <= targetRadius; dx++) {
                for (int dy = -targetRadius; dy <= targetRadius; dy++) {
                    for (int dz = -targetRadius; dz <= targetRadius; dz++) {

                        mutable.set(origin.getX() + dx,
                                origin.getY() + dy,
                                origin.getZ() + dz);

                        double dist = mutable.distSqr(origin);

                        if (dist >= bestDistance)
                            continue;

                        BlockState state = level().getBlockState(mutable);

                        if (state.isAir())
                            continue;

                        if (state.getCollisionShape(CustomShulker.this.level(), mutable).isEmpty())
                            continue;

                        if (!(level().getBlockEntity(mutable) instanceof PurpurTargetBlockEntity be))
                            continue;

                        if (be.ownerUUID() != null)
                            continue;

                        if (!canSeeBlock(CustomShulker.this, eyePos, mutable))
                            continue;

                        bestDistance = dist;
                        best = mutable.immutable();
                    }
                }
            }

            this.target = best;
        }

        @Override
        public void start() {
            CustomShulker.this.setBlockTarget(target);
            if (target != null && CustomShulker.this.level().getBlockEntity(target) instanceof PurpurTargetBlockEntity be)
                be.setOwnerUUID(CustomShulker.this.getUUID());
        }

        @Override
        public void stop() {
            if (target != null && CustomShulker.this.level().getBlockEntity(target) instanceof PurpurTargetBlockEntity be)
                be.setOwnerUUID(null);
            target = null;
            CustomShulker.this.setBlockTarget(null);
        }
    }

    class PeekGoal extends Goal {
        private int peekTime;

        PeekGoal() {
        }

        public boolean canUse() {
            return CustomShulker.this.getTarget() == null &&
                    CustomShulker.this.random.nextInt(reducedTickDelay(40)) == 0 &&
                    CustomShulker.this.canStayAt(CustomShulker.this.blockPosition(), CustomShulker.this.getAttachFace());
        }

        public boolean canContinueToUse() {
            return CustomShulker.this.getTarget() == null && this.peekTime > 0;
        }

        public void start() {
            this.peekTime = this.adjustedTickDelay(20 * (1 + CustomShulker.this.random.nextInt(3)));
            CustomShulker.this.setRawPeekAmount(30);
        }

        public void stop() {
            if (CustomShulker.this.getTarget() == null) {
                CustomShulker.this.setRawPeekAmount(0);
            }

        }

        public void tick() {
            --this.peekTime;
        }
    }
}
