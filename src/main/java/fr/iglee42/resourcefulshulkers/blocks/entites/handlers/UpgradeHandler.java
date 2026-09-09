package fr.iglee42.resourcefulshulkers.blocks.entites.handlers;

import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Set;

public class UpgradeHandler extends ItemStackHandler {

    private final Set<Upgrade> supportedUpgrades;
    private final boolean supportMultipleTimes;

    public UpgradeHandler(int size, Set<Upgrade> supportedUpgrades, boolean supportMultipleTimes) {
        super(size);
        this.supportedUpgrades = supportedUpgrades;
        this.supportMultipleTimes = supportMultipleTimes;
    }

    public UpgradeHandler(int size, Set<Upgrade> supportedUpgrades) {
        this(size, supportedUpgrades, false);
    }

    public UpgradeHandler(int size) {
        this(size, Set.of(Upgrade.values()));
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof UpgradeItem upgradeItem)) return 0;
        Upgrade upgrade = upgradeItem.getUpgrade();
        if (!supportedUpgrades.contains(upgrade)) return 0;
        return upgrade.maxAmount();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof UpgradeItem upgradeItem)) return false;
        Upgrade upgrade = upgradeItem.getUpgrade();
        if (!supportedUpgrades.contains(upgrade)) return false;
        int existingSlot = firstSlotWith(upgrade);
        if (existingSlot == -1) return true;
        return existingSlot == slot || supportMultipleTimes;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!(stack.getItem() instanceof UpgradeItem upgradeItem)) return stack;
        Upgrade upgrade = upgradeItem.getUpgrade();
        if (!supportedUpgrades.contains(upgrade)) return stack;
        int existingSlot = firstSlotWith(upgrade);
        if (existingSlot == -1) return super.insertItem(slot, stack, simulate);
        if (existingSlot != slot && !supportMultipleTimes) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    public int getUpgradeCount(Upgrade upgrade) {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof UpgradeItem upgradeItem && upgradeItem.getUpgrade() == upgrade) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public int getUpgradeCount(UpgradeItem item){
        return getUpgradeCount(item.getUpgrade());
    }

    public int firstSlotWith(Upgrade upgrade){
        for(int i = 0; i < getSlots(); ++i) {
            ItemStack currentStack = getStackInSlot(i);
            if (currentStack.getItem() instanceof UpgradeItem u && u.getUpgrade() == upgrade) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasUpgrade(Upgrade upgrade){
        return firstSlotWith(upgrade) != -1;
    }
}
