package fr.iglee42.techresourcesshulker.recipes;

import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableRecipe {

    default void start(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be){}

    default void tick(Level level, BlockPos pos, BlockState state,int progress, ShulkerInfuserBlockEntity be){}

    default void second(Level level, BlockPos pos, BlockState state,int progress, ShulkerInfuserBlockEntity be){}

    default void finish(Level level, BlockPos pos, BlockState state, ShulkerInfuserBlockEntity be){}

    boolean canContinue(Level level, BlockPos pos, BlockState state,int progress, ShulkerInfuserBlockEntity be);

}
