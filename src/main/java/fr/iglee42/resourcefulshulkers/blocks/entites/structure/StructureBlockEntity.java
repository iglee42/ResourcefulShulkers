package fr.iglee42.resourcefulshulkers.blocks.entites.structure;

import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class StructureBlockEntity extends BlockEntity {

    @NotNull
    private WeakReference<StructuredBlockEntity> main = new WeakReference<>(null);
    private BlockPos mainPossiblePos;

    public StructureBlockEntity(BlockPos pos, BlockState state) {
        super(RSBlockEntities.STRUCTURE.get(), pos, state);
    }

    public Optional<StructuredBlockEntity> getMain() {
        if (mainPossiblePos != null && level != null) {
            BlockEntity blockEntity = level.getBlockEntity(mainPossiblePos);
            if (blockEntity instanceof StructuredBlockEntity main){
                this.main = new WeakReference<>(main);
                this.mainPossiblePos = null;
            } else {
                level.destroyBlock(getBlockPos(), false, null);
            }
        }
        return Optional.ofNullable(main.get());
    }

    public void setMain(@NotNull StructuredBlockEntity main) {
        if (this.main.get() != null)
            throw new IllegalStateException("Main already set and can't be changed");
        this.main = new WeakReference<>(main);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (mainPossiblePos != null) {
            tag.put("main_pos", NbtUtils.writeBlockPos(mainPossiblePos));
        } else {
            StructuredBlockEntity mainEntity = main.get();
            if (mainEntity != null) {
                tag.put("main_pos", NbtUtils.writeBlockPos(mainEntity.getBlockPos()));
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        mainPossiblePos = NbtUtils.readBlockPos(tag, "main_pos").orElse(null);
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level == null || level.isClientSide) return;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (mainPossiblePos != null) {
            tag.put("main_pos", NbtUtils.writeBlockPos(mainPossiblePos));
        } else {
            StructuredBlockEntity mainEntity = main.get();
            if (mainEntity != null) {
                tag.put("main_pos", NbtUtils.writeBlockPos(mainEntity.getBlockPos()));
            }
        }
        return tag;
    }
}
