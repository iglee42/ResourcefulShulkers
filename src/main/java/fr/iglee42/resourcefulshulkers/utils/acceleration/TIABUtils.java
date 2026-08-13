package fr.iglee42.resourcefulshulkers.utils.acceleration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;

import java.util.Optional;

public class TIABUtils {

    public static boolean checkTIAB(Level level, BlockPos pos) {
        Optional<TimeAcceleratorEntity> o = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst();
        return o.isPresent();
    }

    public static boolean isAccelerationEntity(Entity entity){
        return entity instanceof TimeAcceleratorEntity;
    }

}
