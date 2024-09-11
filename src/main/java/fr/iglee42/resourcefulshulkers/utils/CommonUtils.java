package fr.iglee42.resourcefulshulkers.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static Entity getEntityOnBlock(Level level, BlockPos pos){
        List<Entity> entities = new ArrayList<>();

        for (Entity entity : level.getEntities().getAll()) {
            entities.add(entity);
        }

        return entities.stream().filter(e->e.blockPosition().equals(pos.above())).findAny().orElse(null);
    }
}
