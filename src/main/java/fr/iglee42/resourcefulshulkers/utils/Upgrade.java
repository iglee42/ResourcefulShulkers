package fr.iglee42.resourcefulshulkers.utils;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;

import java.util.Arrays;
import java.util.function.Supplier;

public enum Upgrade {
    SPEED(4),
    DURABILITY(4, ()-> (int) (RSServerConfig.DURABILITY_REDUCTION.get() * 100)),
    QUANTITY(4),
    SHELL(4),

    ;
    private final int maxAmount;
    private final Supplier<Object>[] args;

    Upgrade(int maxAmount, Supplier<Object>... args) {
        this.maxAmount = maxAmount;
        this.args = args;
    }

    public int maxAmount() {
        return maxAmount;
    }

    public Object[] getArgs(){
        return Arrays.stream(args).map(Supplier::get).toArray();
    }

    public String getName(){
        return ModsUtils.getUpperName(name(),"_");
    }

}
