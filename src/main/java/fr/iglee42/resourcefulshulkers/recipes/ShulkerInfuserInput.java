package fr.iglee42.resourcefulshulkers.recipes;

import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class ShulkerInfuserInput implements RecipeInput {

    private final Entity target;
    private final List<PedestalEntry> pedestals;
    private final Holder<Biome> biome;
    private final BlockPos blockPos;

    public ShulkerInfuserInput(ShulkerInfuserBlockEntity be) {
        this.target = be.getCurrentTarget();
        this.pedestals = List.copyOf(be.getPedestals());
        this.biome = be.getLevel().getBiome(be.getBlockPos());
        this.blockPos = be.getBlockPos();
    }

    @Override
    public ItemStack getItem(int i) {
        return pedestals.get(i) == null ? ItemStack.EMPTY : pedestals.get(i).stack();
    }

    @Override
    public int size() {
        return pedestals.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Entity getTarget() {
        return target;
    }

    public List<PedestalEntry> getPedestals() {
        return pedestals;
    }

    public Holder<Biome> getBiome() {
        return biome;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public record PedestalEntry(BlockPos pos, ItemStack stack) {
        public void deleteItem(Level level){
            if (level.isClientSide) return;
            if (!(level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity be)) return;
            be.setStack(ItemStack.EMPTY);
        }
    }
}
