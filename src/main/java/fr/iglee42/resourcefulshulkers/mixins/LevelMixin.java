package fr.iglee42.resourcefulshulkers.mixins;

import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.core.Direction.UP;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "loadedAndEntityCanStandOnFace",at = @At("HEAD"), cancellable = true)
    private void loadedAndEntityCanStandOnFace(BlockPos pos, Entity entity, Direction direction, CallbackInfoReturnable<Boolean> cir){
        Object obj = this;
        ChunkAccess chunkaccess = ((Level)obj).getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        if ((chunkaccess.getBlockState(pos).is(ModBlocks.SHULKER_INFUSER.get()) || chunkaccess.getBlockState(pos).is(ModBlocks.SHULKER_ABSORBER.get())) && direction == UP) cir.setReturnValue(true);
    }

}
