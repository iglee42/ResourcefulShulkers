package fr.iglee42.resourcefulshulkers.blocks.entites.handlers;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class ShellHandler extends ItemStackHandler {

    private final Set<IShulkerDefinition> supportedShells;

    public ShellHandler(int size, Set<IShulkerDefinition> supportedShells) {
        super(size);
        this.supportedShells = new HashSet<>(supportedShells);
    }

    public ShellHandler(int size) {
        this(size, Set.of());
    }

    public void clearSupportedShells() {
        supportedShells.clear();
    }

    public void addSupportedShell(IShulkerDefinition definition) {
        supportedShells.add(definition);
    }

    public void addSupportedShells(Set<IShulkerDefinition> definitions) {
        supportedShells.addAll(definitions);
    }

    public void removeSupportedShell(IShulkerDefinition definition) {
        supportedShells.remove(definition);
    }

    public int getSlotWithShell(IShulkerDefinition definition){
        for (int slot = 0; slot < stacks.size(); slot++) {
            ItemStack stack = getStackInSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof ShellItem shell)) continue;
            if (shell.getDefinition().equals(definition)) return slot;
        }
        return -1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof ShellItem shellItem)) return false;
        IShulkerDefinition def = shellItem.getDefinition();
        if (!supportedShells.contains(def)) return false;
        return super.isItemValid(slot, stack);
    }

}
