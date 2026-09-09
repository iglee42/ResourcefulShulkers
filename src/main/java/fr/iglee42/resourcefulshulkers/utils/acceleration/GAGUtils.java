package fr.iglee42.resourcefulshulkers.utils.acceleration;

import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.TemporalPouchItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class GAGUtils {

    public static boolean isPouched(Level level, BlockPos pos){
        Optional<TimeAcceleratorEntity> o = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst();
        return o.isPresent();
    }

    public static boolean isAccelerationEntity(Entity entity){
        return entity instanceof TimeAcceleratorEntity;
    }
}
