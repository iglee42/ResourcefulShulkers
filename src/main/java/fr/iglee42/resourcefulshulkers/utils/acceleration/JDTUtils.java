package fr.iglee42.resourcefulshulkers.utils.acceleration;

import com.direwolf20.justdirethings.common.entities.TimeWandEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class JDTUtils {

    public static boolean isTimeWand(Level level, BlockPos pos){
        Optional<TimeWandEntity> o = level.getEntitiesOfClass(TimeWandEntity.class, new AABB(pos)).stream().findFirst();
        return o.isPresent();
    }

    public static boolean isAccelerationEntity(Entity entity){
        return entity instanceof TimeWandEntity;
    }
}
