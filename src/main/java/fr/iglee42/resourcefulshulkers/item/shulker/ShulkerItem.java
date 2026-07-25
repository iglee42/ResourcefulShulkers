package fr.iglee42.resourcefulshulkers.item.shulker;

import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public abstract class ShulkerItem extends Item {

    public ShulkerItem(Item.Properties props) {
        super(props);
    }

    public abstract @NotNull EntityType<?> entityType();

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        Level level = context.getLevel();
        Player player = context.getPlayer();
        Direction face = context.getClickedFace();
        BlockPos pos = context.getClickedPos().relative(face);
        Mob shulker = (Mob) entityType().create(level);
        if (shulker != null) {
            shulker.getEntityData().set(CustomShulker.DATA_ATTACH_FACE_ID, face);
            shulker.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(shulker);
            player.getItemInHand(context.getHand()).shrink(1);
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return entityType().getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        if (Screen.hasShiftDown()) {
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.shulker_pickup").withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift").withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(p_41421_, p_41422_, tooltips, p_41424_);
    }

}
