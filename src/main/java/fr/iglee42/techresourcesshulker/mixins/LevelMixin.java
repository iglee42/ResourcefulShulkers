package fr.iglee42.techresourcesshulker.mixins;

import fr.iglee42.techresourcesshulker.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.core.Direction.UP;

@Mixin(Level.class)
public class LevelMixin {

    @Shadow
    public ChunkAccess getChunk(int p_46502_, int p_46503_, ChunkStatus p_46504_, boolean p_46505_) {
        return null;
    }

    @Inject(method = "loadedAndEntityCanStandOnFace",at = @At("HEAD"), cancellable = true)
    private void loadedAndEntityCanStandOnFace(BlockPos pos, Entity entity, Direction direction, CallbackInfoReturnable<Boolean> cir){
        ChunkAccess chunkaccess = this.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        if (chunkaccess.getBlockState(pos).is(ModContent.SHULKER_INFUSER.get()) && direction == UP) cir.setReturnValue(true);
    }

}
