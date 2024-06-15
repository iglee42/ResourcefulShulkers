package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.resourcefulshulkers.entity.CustomShulkerBullet;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PurpurTargetBlock extends TargetBlock {
    public PurpurTargetBlock() {
        super(Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(1.5F,6.0F).requiresCorrectToolForDrops());
    }

    @Override
    public void onProjectileHit(Level p_57381_, BlockState p_57382_, BlockHitResult p_57383_, Projectile projectile) {
        super.onProjectileHit(p_57381_, p_57382_, p_57383_, projectile);
        if (projectile instanceof CustomShulkerBullet bullet){
            Block.popResource(p_57381_,p_57383_.getBlockPos().offset(0,1,0), new ItemStack(ModItems.getShellById(bullet.getTypeId())));
        }
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable Item.TooltipContext p_49817_, List<Component> tooltips, TooltipFlag p_49819_) {

        if (Screen.hasShiftDown()){
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.purpur_target").withStyle(ChatFormatting.DARK_PURPLE));
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.purpur_target1").withStyle(ChatFormatting.DARK_PURPLE));
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.purpur_target2").withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift").withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(p_49816_, p_49817_, tooltips, p_49819_);

    }
}
