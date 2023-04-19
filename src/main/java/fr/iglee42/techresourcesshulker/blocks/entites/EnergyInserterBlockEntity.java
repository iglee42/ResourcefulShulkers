package fr.iglee42.techresourcesshulker.blocks.entites;

import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import fr.iglee42.techresourcesshulker.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyInserterBlockEntity extends BlockEntity {

    private EnergyStorage energyStorage = new EnergyStorage(1000000,10000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };
    private LazyOptional<EnergyStorage> lazyEnergy = LazyOptional.empty();

    public EnergyInserterBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.ENERGY_INSERTER_BLOCK_ENTITY.get(), pos,state);
    }

    public static void tick(Level level,BlockPos pos,BlockState state,EnergyInserterBlockEntity entity){
        if (level.getBlockEntity(pos.above()) instanceof ShulkerInfuserBlockEntity be){
            be.getEnergyStorage().receiveEnergy(entity.energyStorage.extractEnergy(1000,false),false);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return side == Direction.DOWN && cap == CapabilityEnergy.ENERGY ? lazyEnergy.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergy.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergy = LazyOptional.of(()->energyStorage);
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("energy"));
    }
}
