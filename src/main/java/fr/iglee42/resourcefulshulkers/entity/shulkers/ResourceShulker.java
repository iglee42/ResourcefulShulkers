package fr.iglee42.resourcefulshulkers.entity.shulkers;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.blocks.entites.PurpurTargetBlockEntity;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class ResourceShulker extends CustomShulker{

    private final IShulkerDefinition shulker;

    public ResourceShulker(EntityType<? extends ResourceShulker> type, Level level, IShulkerDefinition shulker) {
        super(type, level);
        this.shulker = shulker;
    }

    public ResourceShulker(Level level, IShulkerDefinition shulker) {
        this(shulker.registration().entityType().get(), level,shulker);
    }

    public IShulkerDefinition definition() {
        return shulker;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.isCrouching()) return InteractionResult.PASS;
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (definition().registration().shulkerItem() == null) return InteractionResult.PASS;
        ItemStack stack = definition().registration().shulkerItem().get().getDefaultInstance();
        if (!player.getInventory().add(stack))
            player.drop(stack, false);
        remove(RemovalReason.KILLED);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new AttackGoal());
        this.targetSelector.addGoal(1, new CustomShulker.FindTarget());
    }

    public void setPeekAmountRaw(int amount) {
        this.currentPeekAmountO = amount;
        this.currentPeekAmount = amount;
    }

    protected class AttackGoal extends Goal {
        private int attackTime;

        public AttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            BlockPos target = ResourceShulker.this.getBlockTarget();
            return target != null && level().getBlockEntity(target) instanceof PurpurTargetBlockEntity be
                    && Objects.equals(be.ownerUUID(), ResourceShulker.this.getUUID());
        }

        @Override
        public void start() {
            this.attackTime = 20;
            ResourceShulker.this.setRawPeekAmount(100);
        }

        @Override
        public void stop() {
            ResourceShulker.this.setRawPeekAmount(0);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.attackTime--;
            BlockPos target = ResourceShulker.this.getBlockTarget();
            if (target != null) {
                ResourceShulker.this.getLookControl().setLookAt(Vec3.atCenterOf(target));
                double dist = ResourceShulker.this.blockPosition().distSqr(target);
                int targetRadius = RSServerConfig.TARGET_RADIUS.get();
                if (dist < targetRadius * targetRadius) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20 + ResourceShulker.this.random.nextInt(10) * 20 / 2;
                        ResourceShulker.this.level()
                                .addFreshEntity(new ResourceShulkerBullet(shulker,ResourceShulker.this.level(), ResourceShulker.this, ResourceShulker.this.getAttachFace().getAxis(), target));
                        ResourceShulker.this.playSound(
                                SoundEvents.SHULKER_SHOOT, 2.0F, (ResourceShulker.this.random.nextFloat() - ResourceShulker.this.random.nextFloat()) * 0.2F + 1.0F
                        );
                    }
                } else {
                    ResourceShulker.this.setTarget(null);
                }

                super.tick();
            }
        }
    }
}
