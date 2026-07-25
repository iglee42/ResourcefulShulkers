package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import fr.iglee42.resourcefulshulkers.utils.CommonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
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

    public static final int MAX_PROGRESS = 32;

    private boolean enable;
    private int progress;

    public ShulkerAbsorberBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHULKER_ABSORBER_BLOCK_ENTITY.get(),pos,state);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        SecondBlockEntity.tick(level,pos,state,this);
        ModsUtils.debugSign(level,pos,progress+"");
        if (level.isClientSide) return;
        Vec3 posi = Vec3.atCenterOf(pos.above());
        if (enable) {
            /*if (getCurrentTarget().getType().equals(EntityType.SHULKER)) {
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SHULKER_SHELL)), (ServerLevel) level, posi.add(0.5, 0, 0), posi.add(1.5, -1, 0), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SHULKER_SHELL)), (ServerLevel) level, posi.add(-0.5, 0, 0), posi.add(-1.5, -1, 0), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SHULKER_SHELL)), (ServerLevel) level, posi.add(0, 0, 0.5), posi.add(0, -1, 1.5), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SHULKER_SHELL)), (ServerLevel) level, posi.add(0, 0, -0.5), posi.add(0, -1, -1.5), 0);

                if (progress > 2) {
                    spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, posi.add(0.7, 2, 0), posi.add(0, 320 - posi.y, 0), 32);
                    spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, posi.add(-0.7, 2, 0), posi.add(0, 320 - posi.y, 0), 32);
                    spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, posi.add(0, 2, 0.7), posi.add(0, 320 - posi.y, 0), 32);
                    spawnParticle(ParticleTypes.END_ROD, (ServerLevel) level, posi.add(0, 2, -0.7), posi.add(0, 320 - posi.y, 0), 32);
                }
            } else if (getCurrentTarget() instanceof ResourceShulker s && ResourcefulShulkersConfig.ABSORBER_WORKS_AS_TARGET.get()) {
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RSItems.getShellById(s.getTypeId()))), (ServerLevel) level, posi.add(0.5, 0, 0), posi.add(1.5, -1, 0), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RSItems.getShellById(s.getTypeId()))), (ServerLevel) level, posi.add(-0.5, 0, 0), posi.add(-1.5, -1, 0), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RSItems.getShellById(s.getTypeId()))), (ServerLevel) level, posi.add(0, 0, 0.5), posi.add(0, -1, 1.5), 0);
                spawnParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RSItems.getShellById(s.getTypeId()))), (ServerLevel) level, posi.add(0, 0, -0.5), posi.add(0, -1, -1.5), 0);
            }*/
        }
    }

    @Override
    protected void second(Level level, BlockPos blockPos, BlockState blockState, SecondBlockEntity secondBlockEntity) {
        if (level.isClientSide) return;

        if (getCurrentTarget() != null && getCurrentTarget().getType() == EntityType.SHULKER){
            enable = true;
            progress++;
            ((Shulker)getCurrentTarget()).setNoAi(true);
            level.sendBlockUpdated(blockPos,blockState,blockState,Block.UPDATE_CLIENTS);
            ShulkerAuraManager.get(level).insertAura(blockPos, ResourcefulShulkersConfig.ABSORBER_AURA.get() / MAX_PROGRESS, false);
            if (progress == MAX_PROGRESS){
                getCurrentTarget().remove(Entity.RemovalReason.KILLED);
                Block.popResource(level,blockPos.above(),new ItemStack(RSItems.SHULKER_HEAD.get()));
                progress = 0;
                enable = false;
            }

        } else if (getCurrentTarget() != null && getCurrentTarget() instanceof ResourceShulker s && ResourcefulShulkersConfig.ABSORBER_WORKS_AS_TARGET.get()){
            enable = true;
            progress++;
            s.setNoAi(true);
            level.sendBlockUpdated(blockPos,blockState,blockState,Block.UPDATE_CLIENTS);
            if (progress == MAX_PROGRESS){
                getCurrentTarget().remove(Entity.RemovalReason.KILLED);
                Block.popResource(level,blockPos.above(),new ItemStack(RSItems.SHULKER_HEAD.get()));
                //Block.popResource(level,blockPos.above(),new ItemStack(RSItems.getShellById(s.getTypeId()),2));
                progress = 0;
                enable = false;
            }
        } else {
            enable = false;
            progress = 0;
            level.sendBlockUpdated(blockPos,blockState,blockState,Block.UPDATE_CLIENTS);
        }
    }

    public Entity getCurrentTarget(){
        return CommonUtils.getEntityOnBlock((ServerLevel) level,getBlockPos());
    }

    public int getProgress() {
        return progress;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag,provider);
        tag.putBoolean("enable", enable);
        tag.putInt("progress",progress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        enable = tag.getBoolean("enable");
        progress = tag.getInt("progress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("enable", enable);
        tag.putInt("progress",progress);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
