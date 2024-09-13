package fr.iglee42.resourcefulshulkers.utils;

import com.haoict.tiab.common.entities.TimeAcceleratorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class TIABUtils {


    public static boolean checkTIAB(Level level, BlockPos pos) {
        Optional<TimeAcceleratorEntity> o = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst();
        return o.isPresent();
    }
}
