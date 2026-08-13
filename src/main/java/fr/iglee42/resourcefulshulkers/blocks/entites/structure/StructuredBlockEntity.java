package fr.iglee42.resourcefulshulkers.blocks.entites.structure;

import fr.iglee42.igleelib.api.blockentities.SecondBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StructuredBlockEntity extends SecondBlockEntity implements MenuProvider {

    public StructuredBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    public abstract void preRemoveSideEffects();
}
