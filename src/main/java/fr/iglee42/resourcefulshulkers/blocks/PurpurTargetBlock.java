package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.resourcefulshulkers.blocks.entites.PurpurTargetBlockEntity;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PurpurTargetBlock extends TargetBlock implements EntityBlock {
    public PurpurTargetBlock() {
        super(Properties.of().strength(1.5F,6.0F).requiresCorrectToolForDrops());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext p_49817_, List<Component> tooltips, TooltipFlag p_49819_) {
        RSTooltipHandler.addTooltip(stack, tooltips, Screen.hasShiftDown());
        super.appendHoverText(stack, p_49817_, tooltips, p_49819_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PurpurTargetBlockEntity(pos,state);
    }
}
