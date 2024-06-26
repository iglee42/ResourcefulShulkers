package fr.iglee42.resourcefulshulkers.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;


public abstract class CustomShulker extends AbstractGolem implements Enemy {
   private static final UUID COVERED_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
   private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_UUID, "Covered armor bonus", 20.0D, AttributeModifier.Operation.ADDITION);
   public static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(CustomShulker.class, EntityDataSerializers.DIRECTION);

   protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(CustomShulker.class, EntityDataSerializers.BYTE);
   protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(CustomShulker.class, EntityDataSerializers.BYTE);

   static final Vector3f FORWARD = Util.make(() -> {
      Vec3i vec3i = Direction.SOUTH.getNormal();
      return new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
   });
   private final ResourceLocation typeId;
   private float currentPeekAmountO;
   private float currentPeekAmount;
   @Nullable
   private BlockPos clientOldAttachPosition;
   private int clientSideTeleportInterpolation;

   public CustomShulker(EntityType<? extends CustomShulker> p_33404_, Level p_33405_, ResourceLocation typeId) {
      super(p_33404_, p_33405_);
      this.xpReward = 5;
      this.lookControl = new CustomShulker.ShulkerLookControl(this);
      this.typeId = typeId;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
      this.goalSelector.addGoal(7, new CustomShulker.ShulkerPeekGoal());
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
   }

   protected Entity.MovementEmission getMovementEmission() {
      return Entity.MovementEmission.NONE;
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.SHULKER_AMBIENT;
   }

   public void playAmbientSound() {
      if (!this.isClosed()) {
         super.playAmbientSound();
      }

   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.SHULKER_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource p_33457_) {
      return this.isClosed() ? SoundEvents.SHULKER_HURT_CLOSED : SoundEvents.SHULKER_HURT;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
      this.entityData.define(DATA_PEEK_ID, (byte)0);
      this.entityData.define(DATA_COLOR_ID, (byte)16);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D);
   }

   protected BodyRotationControl createBodyControl() {
      return new CustomShulker.ShulkerBodyRotationControl(this);
   }

   public void readAdditionalSaveData(CompoundTag p_33432_) {
      super.readAdditionalSaveData(p_33432_);
      this.setAttachFace(Direction.from3DDataValue(p_33432_.getByte("AttachFace")));
      this.entityData.set(DATA_PEEK_ID, p_33432_.getByte("Peek"));
      if (p_33432_.contains("Color", 99)) {
         this.entityData.set(DATA_COLOR_ID, p_33432_.getByte("Color"));
      }

   }

   public void addAdditionalSaveData(CompoundTag p_33443_) {
      super.addAdditionalSaveData(p_33443_);
      p_33443_.putByte("AttachFace", (byte)this.getAttachFace().get3DDataValue());
      p_33443_.putByte("Peek", this.entityData.get(DATA_PEEK_ID));
      p_33443_.putByte("Color", this.entityData.get(DATA_COLOR_ID));
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide && !this.isPassenger() && !this.canStayAt(this.blockPosition(), this.getAttachFace())) {
         this.findNewAttachment();
      }

      if (this.updatePeekAmount()) {
         this.onPeekAmountChange();
      }


      if (this.level().isClientSide) {
         if (this.clientSideTeleportInterpolation > 0) {
            --this.clientSideTeleportInterpolation;
         } else {
            this.clientOldAttachPosition = null;
         }
      }

   }

   private void findNewAttachment() {
      Direction direction = this.findAttachableSurface(this.blockPosition());
      if (direction != null) {
         this.setAttachFace(direction);
      } else {
         this.teleportSomewhere();
      }

   }

   protected AABB makeBoundingBox() {
      float f = getPhysicalPeek(this.currentPeekAmount);
      Direction direction = this.getAttachFace().getOpposite();
      float f1 = this.getType().getWidth() / 2.0F;
      return getProgressAabb(direction, f).move(this.getX() - (double)f1, this.getY(), this.getZ() - (double)f1);
   }

   private static float getPhysicalPeek(float p_149769_) {
      return 0.5F - Mth.sin((0.5F + p_149769_) * (float)Math.PI) * 0.5F;
   }

   private boolean updatePeekAmount() {
      this.currentPeekAmountO = this.currentPeekAmount;
      float f = (float)this.getRawPeekAmount() * 0.01F;
      if (this.currentPeekAmount == f) {
         return false;
      } else {
         if (this.currentPeekAmount > f) {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, f, 1.0F);
         } else {
            this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, f);
         }

         return true;
      }
   }

   private void onPeekAmountChange() {
      this.reapplyPosition();
      float f = getPhysicalPeek(this.currentPeekAmount);
      float f1 = getPhysicalPeek(this.currentPeekAmountO);
      Direction direction = this.getAttachFace().getOpposite();
      float f2 = f - f1;
      if (!(f2 <= 0.0F)) {
         for(Entity entity : this.level().getEntities(this, getProgressDeltaAabb(direction, f1, f).move(this.getX() - 0.5D, this.getY(), this.getZ() - 0.5D), EntitySelector.NO_SPECTATORS.and((p_149771_) -> {
            return !p_149771_.isPassengerOfSameVehicle(this);
         }))) {
            if (!(entity instanceof CustomShulker) && !entity.noPhysics) {
               entity.move(MoverType.SHULKER, new Vec3((double)(f2 * (float)direction.getStepX()), (double)(f2 * (float)direction.getStepY()), (double)(f2 * (float)direction.getStepZ())));
            }
         }

      }
   }

   public static AABB getProgressAabb(Direction p_149791_, float p_149792_) {
      return getProgressDeltaAabb(p_149791_, -1.0F, p_149792_);
   }

   public static AABB getProgressDeltaAabb(Direction p_149794_, float p_149795_, float p_149796_) {
      double d0 = (double)Math.max(p_149795_, p_149796_);
      double d1 = (double)Math.min(p_149795_, p_149796_);
      return (new AABB(BlockPos.ZERO)).expandTowards((double)p_149794_.getStepX() * d0, (double)p_149794_.getStepY() * d0, (double)p_149794_.getStepZ() * d0).contract((double)(-p_149794_.getStepX()) * (1.0D + d1), (double)(-p_149794_.getStepY()) * (1.0D + d1), (double)(-p_149794_.getStepZ()) * (1.0D + d1));
   }

   public double getMyRidingOffset() {
      EntityType<?> entitytype = this.getVehicle().getType();
      return entitytype != EntityType.BOAT && entitytype != EntityType.MINECART ? super.getMyRidingOffset() : 0.1875D - this.getVehicle().getPassengersRidingOffset();
   }

   public boolean startRiding(Entity p_149773_, boolean p_149774_) {
      if (this.level().isClientSide()) {
         this.clientOldAttachPosition = null;
         this.clientSideTeleportInterpolation = 0;
      }

      this.setAttachFace(Direction.DOWN);
      return super.startRiding(p_149773_, p_149774_);
   }

   public void stopRiding() {
      super.stopRiding();
      if (this.level().isClientSide) {
         this.clientOldAttachPosition = this.blockPosition();
      }

      this.yBodyRotO = 0.0F;
      this.yBodyRot = 0.0F;
   }

   @Nullable
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_149780_, DifficultyInstance p_149781_, MobSpawnType p_149782_, @Nullable SpawnGroupData p_149783_, @Nullable CompoundTag p_149784_) {
      this.setYRot(0.0F);
      this.yHeadRot = this.getYRot();
      this.setOldPosAndRot();
      return super.finalizeSpawn(p_149780_, p_149781_, p_149782_, p_149783_, p_149784_);
   }

   public void move(MoverType p_33424_, Vec3 p_33425_) {
      if (p_33424_ == MoverType.SHULKER_BOX) {
         this.teleportSomewhere();
      } else {
         super.move(p_33424_, p_33425_);
      }

   }

   public Vec3 getDeltaMovement() {
      return Vec3.ZERO;
   }

   public void setDeltaMovement(Vec3 p_149804_) {
   }

   public void setPos(double p_33449_, double p_33450_, double p_33451_) {
      BlockPos blockpos = this.blockPosition();
      if (this.isPassenger()) {
         super.setPos(p_33449_, p_33450_, p_33451_);
      } else {
         super.setPos((double)Mth.floor(p_33449_) + 0.5D, (double)Mth.floor(p_33450_ + 0.5D), (double)Mth.floor(p_33451_) + 0.5D);
      }

      if (this.tickCount != 0) {
         BlockPos blockpos1 = this.blockPosition();
         if (!blockpos1.equals(blockpos)) {
            this.entityData.set(DATA_PEEK_ID, (byte)0);
            this.hasImpulse = true;
            if (this.level().isClientSide && !this.isPassenger() && !blockpos1.equals(this.clientOldAttachPosition)) {
               this.clientOldAttachPosition = blockpos;
               this.clientSideTeleportInterpolation = 6;
               this.xOld = this.getX();
               this.yOld = this.getY();
               this.zOld = this.getZ();
            }
         }

      }
   }

   @Nullable
   protected Direction findAttachableSurface(BlockPos p_149811_) {
      for(Direction direction : Direction.values()) {
         if (this.canStayAt(p_149811_, direction)) {
            return direction;
         }
      }

      return null;
   }

   boolean canStayAt(BlockPos p_149786_, Direction p_149787_) {
      if (this.isPositionBlocked(p_149786_)) {
         return false;
      } else {
         Direction direction = p_149787_.getOpposite();
         if (!this.level().loadedAndEntityCanStandOnFace(p_149786_.relative(p_149787_), this, direction)) {
            return false;
         } else {
            AABB aabb = getProgressAabb(direction, 1.0F).move(p_149786_).deflate(1.0E-6D);
            return this.level().noCollision(this, aabb);
         }
      }
   }

   private boolean isPositionBlocked(BlockPos p_149813_) {
      BlockState blockstate = this.level().getBlockState(p_149813_);
      if (blockstate.isAir()) {
         return false;
      } else {
         boolean flag = blockstate.is(Blocks.MOVING_PISTON) && p_149813_.equals(this.blockPosition());
         return !flag;
      }
   }

   protected boolean teleportSomewhere() {
      if (!this.isNoAi() && this.isAlive()) {
         BlockPos blockpos = this.blockPosition();

         for(int i = 0; i < 5; ++i) {
            BlockPos blockpos1 = blockpos.offset(Mth.randomBetweenInclusive(this.random, -4, 4), Mth.randomBetweenInclusive(this.random, -4, 4), Mth.randomBetweenInclusive(this.random, -4, 4));
            if (blockpos1.getY() > this.level().getMinBuildHeight() && this.level().isEmptyBlock(blockpos1) && this.level().getWorldBorder().isWithinBounds(blockpos1) && this.level().noCollision(this, (new AABB(blockpos1)).deflate(1.0E-6D))) {
               Direction direction = this.findAttachableSurface(blockpos1);
               if (direction != null) {
                  net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                  if (event.isCanceled()) direction = null;
                  blockpos1 = new BlockPos((int) event.getTargetX(), (int) event.getTargetY(), (int) event.getTargetZ());
               }

               if (direction != null) {
                  this.unRide();
                  this.setAttachFace(direction);
                  this.playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
                  this.setPos((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY(), (double)blockpos1.getZ() + 0.5D);
                  this.entityData.set(DATA_PEEK_ID, (byte)0);
                  this.setTarget((LivingEntity)null);
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void lerpTo(double p_33411_, double p_33412_, double p_33413_, float p_33414_, float p_33415_, int p_33416_, boolean p_33417_) {
      this.lerpSteps = 0;
      this.setPos(p_33411_, p_33412_, p_33413_);
      this.setRot(p_33414_, p_33415_);
   }

   public boolean hurt(DamageSource p_33421_, float p_33422_) {
      if (this.isClosed()) {
         Entity entity = p_33421_.getDirectEntity();
         if (entity instanceof AbstractArrow) {
            return false;
         }
      }
      if (!super.hurt(p_33421_, p_33422_)) {
         return false;
      } else {
            if (p_33421_.is(DamageTypes.MOB_PROJECTILE)) {
               Entity entity1 = p_33421_.getDirectEntity();
               if (entity1 != null && entity1.getType() == EntityType.SHULKER_BULLET) {
                  hitByShulkerBullet();
               }
            }
            return true;
      }
   }
   @Override
   protected InteractionResult mobInteract(Player player, InteractionHand p_21473_) {
      if (player.isCrouching()){
         player.addItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ForgeRegistries.ENTITY_TYPES.getKey(this.getType()))));
         this.remove(RemovalReason.KILLED);
         return InteractionResult.SUCCESS;
      }
      return super.mobInteract(player, p_21473_);
   }

   @org.jetbrains.annotations.Nullable
   @Override
   public ItemStack getPickResult() {
      return new ItemStack(ForgeRegistries.ITEMS.getValue(ForgeRegistries.ENTITY_TYPES.getKey(this.getType())));
   }


   public abstract void hitByShulkerBullet();

   boolean isClosed() {
      return this.getRawPeekAmount() == 0;
   }



   public boolean canBeCollidedWith() {
      return this.isAlive();
   }

   public Direction getAttachFace() {
      return this.entityData.get(DATA_ATTACH_FACE_ID);
   }

   private void setAttachFace(Direction p_149789_) {
      this.entityData.set(DATA_ATTACH_FACE_ID, p_149789_);
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> p_33434_) {
      if (DATA_ATTACH_FACE_ID.equals(p_33434_)) {
         this.setBoundingBox(this.makeBoundingBox());
      }

      super.onSyncedDataUpdated(p_33434_);
   }

   int getRawPeekAmount() {
      return this.entityData.get(DATA_PEEK_ID);
   }

   void setRawPeekAmount(int p_33419_) {
      if (!this.level().isClientSide) {
         this.getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER);
         if (p_33419_ == 0) {
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
            this.playSound(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
            this.gameEvent(GameEvent.CONTAINER_CLOSE);
         } else {
            this.playSound(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
            this.gameEvent(GameEvent.CONTAINER_OPEN);
         }
      }

      this.entityData.set(DATA_PEEK_ID, (byte)p_33419_);
   }

   public float getClientPeekAmount(float p_33481_) {
      return Mth.lerp(p_33481_, this.currentPeekAmountO, this.currentPeekAmount);
   }

   protected float getStandingEyeHeight(Pose p_33438_, EntityDimensions p_33439_) {
      return 0.5F;
   }

   public void recreateFromPacket(ClientboundAddEntityPacket p_149798_) {
      super.recreateFromPacket(p_149798_);
      this.yBodyRot = 0.0F;
      this.yBodyRotO = 0.0F;
   }

   public int getMaxHeadXRot() {
      return 180;
   }

   public int getMaxHeadYRot() {
      return 180;
   }

   public void push(Entity p_33474_) {
   }

   public float getPickRadius() {
      return 0.0F;
   }

   public Optional<Vec3> getRenderPosition(float p_149767_) {
      if (this.clientOldAttachPosition != null && this.clientSideTeleportInterpolation > 0) {
         double d0 = (double)((float)this.clientSideTeleportInterpolation - p_149767_) / 6.0D;
         d0 *= d0;
         BlockPos blockpos = this.blockPosition();
         double d1 = (double)(blockpos.getX() - this.clientOldAttachPosition.getX()) * d0;
         double d2 = (double)(blockpos.getY() - this.clientOldAttachPosition.getY()) * d0;
         double d3 = (double)(blockpos.getZ() - this.clientOldAttachPosition.getZ()) * d0;
         return Optional.of(new Vec3(-d1, -d2, -d3));
      } else {
         return Optional.empty();
      }
   }

   void setColor(DyeColor p_149778_) {
      this.entityData.set(DATA_COLOR_ID, (byte)p_149778_.getId());
   }

   @Nullable
   public DyeColor getColor() {
      byte b0 = this.entityData.get(DATA_COLOR_ID);
      return b0 != 16 && b0 <= 15 ? DyeColor.byId(b0) : null;
   }

   public ResourceLocation getTypeId() {
      return typeId;
   }

   public abstract boolean hasTarget();

   static class ShulkerBodyRotationControl extends BodyRotationControl {
      public ShulkerBodyRotationControl(Mob p_149816_) {
         super(p_149816_);
      }

      public void clientTick() {
      }
   }



   class ShulkerLookControl extends LookControl {
      public ShulkerLookControl(Mob p_149820_) {
         super(p_149820_);
      }

      protected void clampHeadRotationToBody() {
      }

      protected Optional<Float> getYRotD() {
         Direction direction = CustomShulker.this.getAttachFace().getOpposite();
         Vector3f vector3f = direction.getRotation().transform(new Vector3f(CustomShulker.FORWARD));
         Vec3i vec3i = direction.getNormal();
         Vector3f vector3f1 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
         vector3f1.cross(vector3f);
         double d0 = this.wantedX - CustomShulker.this.getX();
         double d1 = this.wantedY - CustomShulker.this.getEyeY();
         double d2 = this.wantedZ - CustomShulker.this.getZ();
         Vector3f vector3f2 = new Vector3f((float)d0, (float)d1, (float)d2);
         float f = vector3f1.dot(vector3f2);
         float f1 = vector3f.dot(vector3f2);
         return !(Math.abs(f) > 1.0E-5F) && !(Math.abs(f1) > 1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2((double)(-f), (double)f1) * (double)(180F / (float)Math.PI)));
      }

      protected Optional<Float> getXRotD() {
         return Optional.of(0.0F);
      }
   }


   class ShulkerPeekGoal extends Goal {
      private int peekTime;

      public boolean canUse() {
         return CustomShulker.this.getTarget() == null && CustomShulker.this.random.nextInt(reducedTickDelay(40)) == 0 && CustomShulker.this.canStayAt(CustomShulker.this.blockPosition(), CustomShulker.this.getAttachFace());
      }

      public boolean canContinueToUse() {
         return CustomShulker.this.getTarget() == null && this.peekTime > 0;
      }

      public void start() {
         if (random.nextInt(10) <6){
            peekTime = 0;
            return;
         }
         if (random.nextInt(5) < 2 && hasTarget()){
            this.peekTime = this.adjustedTickDelay(20 * (1 + CustomShulker.this.random.nextInt(5,10)));
            CustomShulker.this.setRawPeekAmount(100);
         } else {
            this.peekTime = this.adjustedTickDelay(20 * (1 + CustomShulker.this.random.nextInt(3)));
            CustomShulker.this.setRawPeekAmount(30);
         }
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
