package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.Iterator;
import java.util.List;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ShulkerHeadItem extends StandingAndWallBlockItem {
    public ShulkerHeadItem() {
        super(ModBlocks.SHULKER_HEAD.get(), ModBlocks.WALL_SHULKER_HEAD.get(), new Properties(), Direction.DOWN);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean p_41408_) {
        if (!level.isClientSide && entity instanceof Player player)
        {
            if (player.getItemBySlot(EquipmentSlot.HEAD).equals(stack)) {
                AdvancementHolder adv = player.getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(MODID, "head"));
                Iterator<String> it = ((ServerPlayer) player).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                while (it.hasNext()) {
                    String criteria = it.next();
                    ((ServerPlayer) player).getAdvancements().award(adv, criteria);
                }
                if (level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, player.blockPosition()).getY() + 10 > player.position().y()) {
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 1, false, false));
                }
            }
        }
        super.inventoryTick(stack, level, entity, slot, p_41408_);
    }


    @Override
    public void appendHoverText(ItemStack p_40572_, @Nullable TooltipContext p_40573_, List<Component> tooltips, TooltipFlag p_40575_) {
        tooltips.add(Component.literal("Try it on your head !").withStyle(ChatFormatting.LIGHT_PURPLE));
        if (ResourcefulShulkersConfig.HEAD_TARGET_ESSENCE.get()) {
            if (Screen.hasShiftDown()) {
                tooltips.add(Component.translatable("tooltip.resourcefulshulkers.place_on_target").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
        super.appendHoverText(p_40572_, p_40573_, tooltips, p_40575_);
    }
}
