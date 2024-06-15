package fr.iglee42.resourcefulshulkers.utils;

import net.minecraft.core.Direction;

public class Utils {
    public static boolean isSideDirection(Direction direction){
        return direction == Direction.NORTH || direction == Direction.SOUTH || direction == Direction.EAST || direction == Direction.WEST;
    }

}
