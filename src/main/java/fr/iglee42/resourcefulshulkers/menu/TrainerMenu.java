package fr.iglee42.resourcefulshulkers.menu;

import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.trainer.TrainerBlockEntity;
import fr.iglee42.resourcefulshulkers.menu.slot.UpgradeItemHandlerSlot;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static fr.iglee42.resourcefulshulkers.menu.EndCityMenu.SHULKER_SLOTS;
import static fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu.EMPTY_SHELL_SLOT;
import static fr.iglee42.resourcefulshulkers.menu.GeneratingBoxMenu.EMPTY_UPGRADE_SLOT;

public class TrainerMenu extends AbstractContainerMenu {


    private final TrainerBlockEntity blockEntity;
    private final Level level;

    public TrainerMenu(int id, Inventory inv, FriendlyByteBuf extraData){
        this(id,inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public TrainerMenu(int id, Inventory playerInv, BlockEntity entity) {
        super(RSBlockEntities.TRAINER_MENU.get(),id);
        this.blockEntity = (TrainerBlockEntity) entity;
        this.level = playerInv.player.level();
        IItemHandler upgrades = blockEntity.getUpgrades();
        for (int u = 0; u < upgrades.getSlots(); ++u){
            this.addSlot(new UpgradeItemHandlerSlot(upgrades,u,8+u*18,108).setBackground(InventoryMenu.BLOCK_ATLAS,EMPTY_UPGRADE_SLOT));
        }

        IItemHandler shulkers = blockEntity.getShulkers();
        for(int l = 0; l < shulkers.getSlots(); ++l) {
            int[] pos = SHULKER_SLOTS[l];
            this.addSlot(new SlotItemHandler(shulkers, l, pos[0], pos[1]));
        }

        IItemHandler items = blockEntity.getOutputInventory();
        for(int l = 0; l < blockEntity.getOutputInventory().getSlots(); ++l) {
            this.addSlot(new SlotItemHandler(items, l, 116 + (l % 3) * 18, 18 + (l / 3) * 18));
        }



        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInv, k1 + i1 * 9 + 9, 8 + k1 * 18, 138 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInv, j1, 8 + j1 * 18, 196));
        }
    }


    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 0;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;


    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        int TE_INSERTABLE_INVENTORY_SLOT_COUNT = blockEntity.getUpgrades().getSlots() + blockEntity.getShulkers().getSlots();
        int TE_EXTRACTABLE_INVENTORY_SLOT_COUNT = TE_INSERTABLE_INVENTORY_SLOT_COUNT + blockEntity.getOutputInventory().getSlots();
        int VANILLA_FIRST_SLOT_INDEX = TE_INVENTORY_FIRST_SLOT_INDEX + TE_EXTRACTABLE_INVENTORY_SLOT_COUNT;
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        //MOVE FROM BE
        if (index >= TE_INVENTORY_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX){
            if (!moveItemStackTo(sourceStack,VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,false))
                return ItemStack.EMPTY;
        }
        //MOVE TO BE
        else if (index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack,TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INSERTABLE_INVENTORY_SLOT_COUNT,false))
                return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }


        // If stack size == 0 (the entire stack was moved) set aura contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return !getBlockEntity().isRemoved() && player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < 64;
    }


    public TrainerBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
