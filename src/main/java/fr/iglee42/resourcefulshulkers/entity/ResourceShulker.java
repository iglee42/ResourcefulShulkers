package fr.iglee42.resourcefulshulkers.entity;

import fr.iglee42.resourcefulshulkers.blocks.PurpurTargetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class ResourceShulker extends CustomShulker{

    private BlockPos target;

    public ResourceShulker(EntityType<? extends CustomShulker> p_33404_, Level p_33405_, ResourceLocation typeId) {
        super(p_33404_, p_33405_, typeId);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6,new ShulkerLaunchBulletGoal());
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (target == null) {
                for (int x = -5; x <= 5; x++) {
                    for (int y = -5; y <= 5; y++) {
                        for (int z = -5; z <= 5; z++) {
                            int finalY = y;
                            int finalX = x;
                            int finalZ = z;
                            AABB searchBox = getBoundingBox().inflate(8);
                            BlockPos currentBlockPos = blockPosition();
                            if (level().getBlockState(blockPosition().offset(x, y, z)).getBlock() instanceof PurpurTargetBlock) {
                                if (level().getEntitiesOfClass(ResourceShulker.class, searchBox, r -> r.getUUID() != getUUID() && r.getBlockTarget() != null &&  r.getBlockTarget().equals(currentBlockPos.offset(finalX, finalY, finalZ))).isEmpty())
                                    target = blockPosition().offset(x, y, z);
                            }
                        }
                    }
                }
            } else {
                if (!(level().getBlockState(target).getBlock() instanceof PurpurTargetBlock)) target = null;
            }
        }
    }

    @Nullable
    public BlockPos getBlockTarget() {
        return target;
    }

    @Override
    public void hitByShulkerBullet() {}

    @Override
    public void readAdditionalSaveData(CompoundTag p_33432_) {
        super.readAdditionalSaveData(p_33432_);
        if (p_33432_.contains("Target")) target = NbtUtils.readBlockPos(p_33432_.getCompound("Target"));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_33443_) {
        super.addAdditionalSaveData(p_33443_);
        if (target != null) p_33443_.put("Target", NbtUtils.writeBlockPos(target));
    }

    @Override
    public boolean hasTarget() {
        return target != null;
    }

    class ShulkerLaunchBulletGoal extends Goal {
        @Override
        public boolean canUse() {
            return getRawPeekAmount() == 100;
        }


        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (random.nextInt(100) < 2 && target != null) {
                AABB searchBox = getBoundingBox().inflate(4);
                if (level().getEntitiesOfClass(CustomShulkerBullet.class, searchBox, r -> r.getOwner().getUUID() == getUUID()).isEmpty()) {
                    ResourceShulker.this.level().addFreshEntity(new CustomShulkerBullet(level(), ResourceShulker.this, getBlockTarget(), getAttachFace().getAxis(), getTypeId()));
                    ResourceShulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
    }
}
