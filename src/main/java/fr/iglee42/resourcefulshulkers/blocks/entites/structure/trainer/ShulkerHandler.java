package fr.iglee42.resourcefulshulkers.blocks.entites.structure.trainer;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.item.shulker.ResourceShulkerItem;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkerDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class ShulkerHandler extends ItemStackHandler {

    public ShulkerHandler(int size) {
        super(size);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof ResourceShulkerItem;
    }

    public Set<IShulkerDefinition> getShulkersInInventory(){
        Set<IShulkerDefinition> shulkers = new HashSet<>(stacks.size());
        for (ItemStack stack : stacks) {
            if (stack.isEmpty() || !(stack.getItem() instanceof ResourceShulkerItem it)) continue;
            shulkers.add(it.definition());
        }
        return Set.copyOf(shulkers);
    }

    public IShulkerDefinition getShulkerDefinitionForSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof ResourceShulkerItem it)) return ShulkerDefinition.EMPTY;
        return it.definition();
    }
}
