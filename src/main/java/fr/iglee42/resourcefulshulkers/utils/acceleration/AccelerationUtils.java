package fr.iglee42.resourcefulshulkers.utils.acceleration;

import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

public final class AccelerationUtils {

    public static final String TIAB = "tiab";
    public static final String GAG = "gag";

    private AccelerationUtils() {}

    public static boolean isAccelerated(Level level, BlockPos blockPos){
        if (!RSServerConfig.ACCELERATION_PROTECTION.get()) return false;

        if (ModList.get().isLoaded(TIAB))
            if (TIABUtils.checkTIAB(level, blockPos))
                return true;

        if (ModList.get().isLoaded(GAG))
            if (GAGUtils.isPouched(level, blockPos))
                return true;

        return false;
    }

    public static boolean isAccelerationEntity(Entity entity){
        if (ModList.get().isLoaded(TIAB))
            if (TIABUtils.isAccelerationEntity(entity))
                return true;

        if (ModList.get().isLoaded(GAG))
            if (GAGUtils.isAccelerationEntity(entity))
                return true;

        return false;
    }
}
