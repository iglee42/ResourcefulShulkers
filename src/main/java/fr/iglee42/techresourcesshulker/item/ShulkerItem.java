package fr.iglee42.techresourcesshulker.item;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.entity.BaseEssenceShulker;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static net.minecraft.core.Direction.UP;

public class ShulkerItem extends Item {
    private final EntityType shulkerType;

    public ShulkerItem(Item.Properties props, EntityType shulkerType) {
        super(props);
        this.shulkerType = shulkerType;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Mob shulker = shulkerType != null ? (Mob)shulkerType.create(context.getLevel()) : (Mob)ForgeRegistries.ENTITIES.getValue(this.getRegistryName()).create(context.getLevel());
        shulker.getEntityData().set(CustomShulker.DATA_ATTACH_FACE_ID,context.getClickedFace().getOpposite());
        shulker.setPos(pos.getX(),pos.getY(),pos.getZ());
        if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.SHULKER_INFUSER.get()) && context.getClickedFace() == UP) shulker.setNoAi(true);
        context.getLevel().addFreshEntity(shulker);
        context.getPlayer().getMainHandItem().setCount(context.getItemInHand().getCount() - 1);
        return InteractionResult.PASS;
    }

    @Override
    public String getDescriptionId() {
        return shulkerType == EntityType.SHULKER ? super.getDescriptionId() : super.getDescriptionId().replace("item","entity");
    }
}
