package fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.registries.RSNBT;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkerDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class GeneratingBoxHandler extends ItemStackHandler {

    public GeneratingBoxHandler(int size) {
        super(size);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof GeneratingBoxItem;
    }

    public Set<IShulkerDefinition> getShulkersInInventory(){
        Set<IShulkerDefinition> shulkers = new HashSet<>(stacks.size());
        for (ItemStack stack : stacks) {
            if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) continue;
            shulkers.add(box.definition());
        }
        return Set.copyOf(shulkers);
    }

    public int getDurabilityOfSlot(int slot){
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) return 0;
        return box.getDurability(stack);
    }

    public boolean useDurabilityForSlot(int slot, boolean simulate){
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) return false;
        int durability = box.getDurability(stack);
        if (durability <= 0) return false;
        if (!simulate) {
            ItemStack copy = stack.copy();
            RSNBT.set(copy, RSNBT.DURABILITY, durability - 1);
            setStackInSlot(slot, copy);
            onContentsChanged(slot);
        }
        return true;
    }

    public int getIndexOfSlot(int slot){
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) return 0;
        return box.getItemIndex(stack);
    }

    public void setItemIndex(int slot, int index){
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) return;
        int oldIndex = box.getItemIndex(stack);
        int newIndex = box.validateIndex(index);
        if (oldIndex != newIndex){
            ItemStack copy = stack.copy();
            RSNBT.set(copy, RSNBT.ITEM_INDEX, index);
            setStackInSlot(slot, copy);
            onContentsChanged(slot);
        }
    }

    public IShulkerDefinition getShulkerDefinitionForSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof GeneratingBoxItem box)) return ShulkerDefinition.EMPTY;
        return box.definition();
    }
}
