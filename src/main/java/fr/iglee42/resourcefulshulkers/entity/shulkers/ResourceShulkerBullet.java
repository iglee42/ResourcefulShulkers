package fr.iglee42.resourcefulshulkers.entity.shulkers;

import com.google.common.collect.Lists;
import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.PurpurTargetBlock;
import fr.iglee42.resourcefulshulkers.blocks.entites.PurpurTargetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ResourceShulkerBullet extends ShulkerBullet {

    private final IShulkerDefinition shulker;
    @Nullable private BlockPos targetPos;

    public ResourceShulkerBullet(EntityType<? extends ShulkerBullet> type, Level level, IShulkerDefinition shulker) {
        super(type, level);
        this.shulker = shulker;
    }

    public ResourceShulkerBullet(IShulkerDefinition shulker, Level level, LivingEntity owner, Direction.Axis axis, BlockPos targetPos) {
        this(shulker.registration().bulletType().get(), level,shulker);
        this.targetPos = targetPos;
        this.setOwner(owner);
        Vec3 vec3 = owner.getBoundingBox().getCenter();
        this.moveTo(vec3.x, vec3.y, vec3.z, this.getYRot(), this.getXRot());
        this.currentMoveDirection = Direction.UP;
        this.selectNextMoveDirection(axis);
    }

    public IShulkerDefinition shulkerDefinition(){
        return shulker;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (targetPos != null){
            tag.put("targetPos", NbtUtils.writeBlockPos(targetPos));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("targetPos", CompoundTag.TAG_INT_ARRAY)){
            this.targetPos = NbtUtils.readBlockPos(tag, "targetPos").orElse(null);
        }
    }

    @Override
    protected void selectNextMoveDirection(Direction.@Nullable Axis avoidAxis) {
        BlockPos targetPos = this.blockPosition().below();
        if (this.targetPos != null) {
            targetPos = this.targetPos;
        }


        double targetX = targetPos.getX() + 0.5;
        double targetY = targetPos.getY() + 0.5;
        double targetZ = targetPos.getZ() + 0.5;
        Direction selection = null;
        if (!targetPos.closerToCenterThan(this.position(), 2.0F)) {
            BlockPos current = this.blockPosition();
            List<Direction> options = Lists.newArrayList();
            if (avoidAxis != Direction.Axis.X) {
                if (current.getX() < targetPos.getX() && this.level().isEmptyBlock(current.east())) {
                    options.add(Direction.EAST);
                } else if (current.getX() > targetPos.getX() && this.level().isEmptyBlock(current.west())) {
                    options.add(Direction.WEST);
                }
            }

            if (avoidAxis != Direction.Axis.Y) {
                if (current.getY() < targetPos.getY() && this.level().isEmptyBlock(current.above())) {
                    options.add(Direction.UP);
                } else if (current.getY() > targetPos.getY() && this.level().isEmptyBlock(current.below())) {
                    options.add(Direction.DOWN);
                }
            }

            if (avoidAxis != Direction.Axis.Z) {
                if (current.getZ() < targetPos.getZ() && this.level().isEmptyBlock(current.south())) {
                    options.add(Direction.SOUTH);
                } else if (current.getZ() > targetPos.getZ() && this.level().isEmptyBlock(current.north())) {
                    options.add(Direction.NORTH);
                }
            }

            selection = Direction.getRandom(this.random);
            if (options.isEmpty()) {
                for(int attempts = 5; !this.level().isEmptyBlock(current.relative(selection)) && attempts > 0; --attempts) {
                    selection = Direction.getRandom(this.random);
                }
            } else {
                selection = options.get(this.random.nextInt(options.size()));
            }

            targetX = this.getX() + (double)selection.getStepX();
            targetY = this.getY() + (double)selection.getStepY();
            targetZ = this.getZ() + (double)selection.getStepZ();
        }

        this.setMoveDirection(selection);
        double xa = targetX - this.getX();
        double ya = targetY - this.getY();
        double za = targetZ - this.getZ();
        double distance = Math.sqrt(xa * xa + ya * ya + za * za);
        if (distance == 0.0) {
            this.targetDeltaX = 0.0;
            this.targetDeltaY = 0.0;
            this.targetDeltaZ = 0.0;
        } else {
            this.targetDeltaX = xa / distance * 0.15;
            this.targetDeltaY = ya / distance * 0.15;
            this.targetDeltaZ = za / distance * 0.15;
        }

        this.hasImpulse = true;
        this.flightSteps = 10 + this.random.nextInt(5) * 10;
    }

    @Override
    public void checkDespawn() {}


    @Override
    public void tick() {
        baseTick();
        if (!this.level().isClientSide) {
            if (this.targetPos == null) {
                this.applyGravity();
            } else {
                this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025, -1.0, 1.0);
                this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025, -1.0, 1.0);
                this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025, -1.0, 1.0);
                Vec3 vec3 = this.getDeltaMovement();
                this.setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2, (this.targetDeltaY - vec3.y) * 0.2, (this.targetDeltaZ - vec3.z) * 0.2));
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.hitTargetOrDeflectSelf(hitresult);
            }
        }

        this.checkInsideBlocks();
        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.END_ROD, this.getX() - vec31.x, this.getY() - vec31.y + 0.15, this.getZ() - vec31.z, 0.0, 0.0, 0.0);
        } else if (this.targetPos != null) {
            if (this.flightSteps > 0) {
                this.flightSteps--;
                if (this.flightSteps == 0) {
                    this.selectNextMoveDirection(this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
                }
            }

            if (this.currentMoveDirection != null) {
                BlockPos blockpos = this.blockPosition();
                Direction.Axis direction$axis = this.currentMoveDirection.getAxis();
                if (this.level().loadedAndEntityCanStandOn(blockpos.relative(this.currentMoveDirection), this)) {
                    this.selectNextMoveDirection(direction$axis);
                } else {
                    BlockPos blockpos1 = this.targetPos;
                    if (direction$axis == Direction.Axis.X && blockpos.getX() == blockpos1.getX()
                            || direction$axis == Direction.Axis.Z && blockpos.getZ() == blockpos1.getZ()
                            || direction$axis == Direction.Axis.Y && blockpos.getY() == blockpos1.getY()) {
                        this.selectNextMoveDirection(direction$axis);
                    }
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!result.getBlockPos().equals(targetPos)) return;
        if (shulker.registration().shell() == null) return;
        if (getOwner() == null) return;
        if (this.level().getBlockEntity(result.getBlockPos()) instanceof PurpurTargetBlockEntity be){
            if (Objects.equals(be.ownerUUID(), getOwner().getUUID())){
                PurpurTargetBlock.popResource(this.level(), result.getBlockPos(), shulker.registration().shell().get().getDefaultInstance());
            }
        }
    }
}
