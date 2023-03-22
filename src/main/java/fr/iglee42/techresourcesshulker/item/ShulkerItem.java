package fr.iglee42.techresourcesshulker.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ShulkerItem extends Item {
    public ShulkerItem(Item.Properties props) {
        super(props);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Shulker shulker = EntityType.SHULKER.create(context.getLevel());
        shulker.getEntityData().set(Shulker.DATA_ATTACH_FACE_ID,context.getClickedFace().getOpposite());
        shulker.setPos(pos.getX(),pos.getY(),pos.getZ());
        context.getLevel().addFreshEntity(shulker);
        context.getPlayer().getMainHandItem().setCount(context.getItemInHand().getCount() - 1);
        return InteractionResult.PASS;
    }
}
