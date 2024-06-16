package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.core.Direction.UP;

public class ShulkerItem extends Item {
    private final EntityType shulkerType;
    private final String type;

    public ShulkerItem(Item.Properties props, EntityType shulkerType,String type) {
        super(props);
        this.shulkerType = shulkerType;
        this.type = type;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Mob shulker = shulkerType != null ? (Mob)shulkerType.create(context.getLevel()) : (Mob)ForgeRegistries.ENTITY_TYPES.getValue(ForgeRegistries.ITEMS.getKey(this)).create(context.getLevel());
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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        if (type != null && !type.isEmpty()){
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.type", TypesManager.getTierDisplayName(type)).withStyle(ChatFormatting.GRAY));
        }

        if (Screen.hasShiftDown()){
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.shulker_pickup").withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift").withStyle(ChatFormatting.DARK_PURPLE));
        }        super.appendHoverText(p_41421_, p_41422_, tooltips, p_41424_);
    }

    public String getType() {
        return type;
    }
}
