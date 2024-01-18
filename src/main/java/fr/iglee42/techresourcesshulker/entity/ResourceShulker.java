package fr.iglee42.techresourcesshulker.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ResourceShulker extends CustomShulker{

    public ResourceShulker(EntityType<? extends CustomShulker> p_33404_, Level p_33405_, int typeId) {
        super(p_33404_, p_33405_, typeId);
    }

    @Override
    public void hitByShulkerBullet() {}
}
