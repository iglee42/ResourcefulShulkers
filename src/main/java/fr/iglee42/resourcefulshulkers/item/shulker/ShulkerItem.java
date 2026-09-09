package fr.iglee42.resourcefulshulkers.item.shulker;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventBusSubscriber(modid = RSIds.MODID)
public abstract class ShulkerItem extends Item {

    public ShulkerItem(Item.Properties props) {
        super(props);
    }

    public abstract @NotNull EntityType<? extends Shulker> entityType();

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        Level level = context.getLevel();
        Player player = context.getPlayer();
        Direction face = context.getClickedFace();
        BlockPos pos = context.getClickedPos().relative(face);
        ItemStack handStack = player.getItemInHand(context.getHand());
        Mob shulker = (Mob) entityType().create(level);
        if (shulker != null) {
            shulker.getEntityData().set(Shulker.DATA_ATTACH_FACE_ID, face);
            shulker.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(shulker);
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return entityType().getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        RSTooltipHandler.tooltip(stack, tooltips).shift(Screen.hasShiftDown()).append("shulker_pickup").apply();
        super.appendHoverText(stack, p_41422_, tooltips, p_41424_);
    }


    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget().getType() == EntityType.SHULKER && event.getEntity().isCrouching()){
            event.getEntity().addItem(new ItemStack(RSItems.SHULKER.get()));
            event.getTarget().remove(Entity.RemovalReason.KILLED);
        }
    }
}
