package fr.iglee42.resourcefulshulkers.blocks.entites;

import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PurpurTargetBlockEntity extends BlockEntity {

    private @Nullable UUID ownerUUID;

    public PurpurTargetBlockEntity(BlockPos pos, BlockState state) {
        super(RSBlockEntities.PURPUR_TARGET.get(), pos, state);
    }

    public @Nullable UUID ownerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        if (this.ownerUUID != ownerUUID) {
            this.ownerUUID = ownerUUID;
            if (level != null && !level.isClientSide)
                level.sendBlockUpdated(getBlockPos(),getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag updateTag = super.getUpdateTag(registries);
        if (ownerUUID != null) {
            updateTag.putUUID("ownerUUID", ownerUUID);
        }
        return updateTag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ownerUUID = tag.hasUUID("ownerUUID") ? tag.getUUID("ownerUUID") : null;
    }
}
