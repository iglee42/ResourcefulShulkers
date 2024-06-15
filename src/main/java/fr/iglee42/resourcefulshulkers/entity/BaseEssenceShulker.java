package fr.iglee42.resourcefulshulkers.entity;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BaseEssenceShulker extends CustomShulker{
    public BaseEssenceShulker(EntityType<? extends CustomShulker> p_33404_, Level p_33405_,DyeColor color) {
        super(p_33404_, p_33405_,new ResourceLocation(ResourcefulShulkers.MODID,"base_essence"));
        this.setColor(color);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6,new BaseEssenceShulker.ShulkerLaunchBulletGoal());
    }


    public void hitByShulkerBullet() {
        Vec3 newPos = Vec3.atBottomCenterOf(blockPosition().offset(Mth.randomBetweenInclusive(this.random, -4, 4), 0, Mth.randomBetweenInclusive(this.random, -4, 4)));
        Vec3 newShulkerPos = this.position();
        AABB aabb = this.getBoundingBox();
        if (!this.isClosed()) {
            int i = this.level().getEntities(this.getType(), aabb.inflate(8.0D), Entity::isAlive).size();
            float f = (float)(i - 1) / 5.0F;
            if (!(this.level().random.nextFloat() < f)) {
                BaseEssenceShulker customShulker = (BaseEssenceShulker) this.getType().create(level());
                DyeColor dyecolor = this.getColor();
                if (dyecolor != null) {
                    customShulker.setColor(dyecolor);
                }
                this.moveTo(newPos);
                customShulker.moveTo(newShulkerPos);
                this.level().addFreshEntity(customShulker);
            }
        }
    }

    @Override
    public boolean hasTarget() {
        return false;
    }

    class ShulkerLaunchBulletGoal extends Goal {
        @Override
        public boolean canUse() {
            return BaseEssenceShulker.this.getRawPeekAmount() == 100;
        }



        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (random.nextInt(50) < 2){
                BaseEssenceShulker target = BaseEssenceShulker.this.level().getNearestEntity(BaseEssenceShulker.this.level().getEntitiesOfClass(BaseEssenceShulker.class, BaseEssenceShulker.this.getBoundingBox().inflate(8), (entity) -> entity.getUUID() != BaseEssenceShulker.this.getUUID() && !entity.hasEffect(MobEffects.LEVITATION)), TargetingConditions.DEFAULT, BaseEssenceShulker.this, BaseEssenceShulker.this.getX(), BaseEssenceShulker.this.getEyeY(), BaseEssenceShulker.this.getZ());
                if (target != null) {
                    BaseEssenceShulker.this.level().addFreshEntity(new ShulkerBullet(BaseEssenceShulker.this.level(), BaseEssenceShulker.this, target, BaseEssenceShulker.this.getAttachFace().getAxis()));
                    BaseEssenceShulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (BaseEssenceShulker.this.random.nextFloat() - BaseEssenceShulker.this.random.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
    }
}
