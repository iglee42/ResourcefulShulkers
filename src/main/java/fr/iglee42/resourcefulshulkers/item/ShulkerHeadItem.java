package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShulkerHeadItem extends StandingAndWallBlockItem {
    public ShulkerHeadItem() {
        super(RSBlocks.SHULKER_HEAD.get(), RSBlocks.WALL_SHULKER_HEAD.get(), new Properties(), Direction.DOWN);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean p_41408_) {
        if (!level.isClientSide && entity instanceof Player player)
        {
            if (player.getItemBySlot(EquipmentSlot.HEAD).equals(stack)) {
                RSAdvancements.HEAD.awardTo(player);
                if (level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, player.blockPosition()).getY() + 10 > player.position().y()) {
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 1, false, false));
                }
            }
        }
        super.inventoryTick(stack, level, entity, slot, p_41408_);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext p_40573_, List<Component> tooltips, TooltipFlag p_40575_) {
        RSTooltipHandler.tooltip(stack, tooltips)
                .header(Component.translatable("tooltip.resourcefulshulkers.head"))
                .apply();
        super.appendHoverText(stack, p_40573_, tooltips, p_40575_);
    }
}
