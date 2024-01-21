package fr.iglee42.techresourcesshulker.blocks;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.entity.CustomShulkerBullet;
import fr.iglee42.techresourcesshulker.utils.Resource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

public class PurpurTargetBlock extends TargetBlock {
    public PurpurTargetBlock() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).strength(1.5F,6.0F).requiresCorrectToolForDrops());
    }

    @Override
    public void onProjectileHit(Level p_57381_, BlockState p_57382_, BlockHitResult p_57383_, Projectile projectile) {
        super.onProjectileHit(p_57381_, p_57382_, p_57383_, projectile);
        if (projectile instanceof CustomShulkerBullet bullet){
            Block.popResource(p_57381_,p_57383_.getBlockPos().offset(0,1,0), new ItemStack(ModContent.getShellById(bullet.getTypeId())));
        }
    }
}
