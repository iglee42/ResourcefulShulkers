package fr.iglee42.resourcefulshulkers.menu.slot;

import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class UpgradeItemHandlerSlot extends SlotItemHandler {
    public UpgradeItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (!(stack.getItem() instanceof UpgradeItem upgradeItem)) return super.getMaxStackSize(stack);
        return upgradeItem.getUpgrade().maxAmount();
    }
}
