package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.client.items.GeneratingBoxItemExtension;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import fr.iglee42.resourcefulshulkers.registries.RSNBT;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class GeneratingBoxItem extends BlockItem {
    private final IShulkerDefinition shulker;

    public GeneratingBoxItem(Block p_40565_, Properties p_40566_, IShulkerDefinition shulker) {
        super(p_40565_, p_40566_);
        this.shulker = shulker;
    }

    public int getDurability(ItemStack stack) {
        return RSNBT.getOrDefaultSet(stack, RSNBT.DURABILITY, RSServerConfig.getMaxDurability());
    }

    public int getItemIndex(ItemStack stack) {
        int index = RSNBT.getOrDefault(stack, RSNBT.ITEM_INDEX, 0);
        int safeIndex = validateIndex(index);
        if (safeIndex != index)
            stack.getOrCreateTag().putInt(RSNBT.ITEM_INDEX, safeIndex);
        return safeIndex;
    }

    public int validateIndex(int index){
        return validateIndex(definition(), index);
    }

    public static int validateIndex(IShulkerDefinition definition, int index){
        return Mth.clamp(index, 0, definition.item().getIngredient().getItems().length - 1);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getDurability(stack) < RSServerConfig.getMaxDurability();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float maxDura = RSServerConfig.getMaxDurability();
        float f = Math.max(0.0F, getDurability(stack) / maxDura);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getDurability(stack) * 13.0F / (float) RSServerConfig.getMaxDurability());
    }

    public IShulkerDefinition definition() {
        return shulker;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        int color = getBarColor(stack);
        RSTooltipHandler.tooltip(stack, tooltips)
                .type(shulker.type())
                .header(Component.translatable("gui.resourcefulshulkers.durability", Component.literal(getDurability(stack) + "").withStyle(style -> style.withColor(color)), RSServerConfig.getMaxDurability()))
                .header(Component.translatable("gui.resourcefulshulkers.generating", Component.translatable((definition().hasItem()?definition().item().getIngredient().getItems()[getItemIndex(stack)] : Items.AIR.getDefaultInstance()).getDescriptionId())))
                .apply();
        super.appendHoverText(stack, p_41422_, tooltips, p_41424_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new GeneratingBoxItemExtension());
    }
}
