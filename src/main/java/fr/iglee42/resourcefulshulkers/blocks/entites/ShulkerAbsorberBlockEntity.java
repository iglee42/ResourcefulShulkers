package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.utils.CommonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static fr.iglee42.igleelib.api.utils.ModsUtils.spawnParticle;

public class ShulkerAbsorberBlockEntity extends SecondBlockEntity {

    private int progress;

    public ShulkerAbsorberBlockEntity(BlockPos pos, BlockState state) {
        super(RSBlockEntities.SHULKER_ABSORBER.get(),pos,state);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        SecondBlockEntity.tick(level,pos,state,this);
        if (level.isClientSide) return;
        Vec3 posi = Vec3.atCenterOf(pos.above());
        if (progress > 0) {
            if (getCurrentTarget().getType().equals(EntityType.SHULKER)) {
                spawnRotatedParticle((ServerLevel) level, new ItemParticleOption(ParticleTypes.ITEM, Items.SHULKER_SHELL.getDefaultInstance()),posi, new Vec3(0.5,0,0), new Vec3(1.5, -1, 0),0);
                if (progress > 2) {
                    spawnRotatedParticle((ServerLevel) level, ParticleTypes.END_ROD, posi, new Vec3(0.7,2,0), new Vec3(0, 320 - posi.y, 0),16);
                }
            }
        }
    }


    private void spawnRotatedParticle(ServerLevel level, ParticleOptions options, Vec3 pos, Vec3 startOffset, Vec3 endOffset, int count) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Vec3 start = switch (direction) {
                case SOUTH -> pos.add(-startOffset.x, startOffset.y, -startOffset.z);
                case WEST -> pos.add(startOffset.z, startOffset.y, -startOffset.x);
                case EAST -> pos.add(-startOffset.z, startOffset.y, startOffset.x);
                default -> pos.add(startOffset.x, startOffset.y, startOffset.z);
            };
            Vec3 end = switch (direction) {
                case SOUTH -> pos.add(-endOffset.x, endOffset.y, -endOffset.z);
                case WEST -> pos.add(endOffset.z, endOffset.y, -endOffset.x);
                case EAST -> pos.add(-endOffset.z, endOffset.y, endOffset.x);
                default -> pos.add(endOffset.x, endOffset.y, endOffset.z);
            };
            spawnParticle(options, level, start, end, count);
        }
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity secondBlockEntity) {
        if (level.isClientSide) return;
        ShulkerAuraManager manager = ShulkerAuraManager.get(level);

        if (manager.insertAura(blockPos, RSServerConfig.ABSORBER_AURA.get(), true) >= RSServerConfig.ABSORBER_AURA.get() && getCurrentTarget() != null && getCurrentTarget().getType() == EntityType.SHULKER){
            progress++;
            ((Shulker)getCurrentTarget()).setNoAi(true);
            level.sendBlockUpdated(blockPos,blockState,blockState,Block.UPDATE_CLIENTS);
            if (progress == RSServerConfig.ABSORBER_DURATION.get()){
                manager.insertAura(blockPos, RSServerConfig.ABSORBER_AURA.get(), false);
                getCurrentTarget().remove(Entity.RemovalReason.KILLED);
                Block.popResource(level,blockPos.above(),new ItemStack(RSItems.SHULKER_HEAD.get()));
                progress = 0;
            }
        } else {
            progress = 0;
            level.sendBlockUpdated(blockPos,blockState,blockState,Block.UPDATE_CLIENTS);
        }
    }

    public Entity getCurrentTarget(){
        return CommonUtils.getEntityOnBlock((ServerLevel) level,getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress",progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("progress",progress);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
