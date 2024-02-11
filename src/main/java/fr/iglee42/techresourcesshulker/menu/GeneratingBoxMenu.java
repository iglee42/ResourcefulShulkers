package fr.iglee42.techresourcesshulker.menu;

import fr.iglee42.techresourcesshulker.init.ModBlockEntities;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.init.ModEntities;
import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.menu.slot.BoxShellSlot;
import fr.iglee42.techresourcesshulker.menu.slot.BoxUpgradeSlot;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GeneratingBoxMenu extends AbstractContainerMenu {

    private final GeneratingBoxBlockEntity blockEntity;
    private final Level level;

    public GeneratingBoxMenu(int id, Inventory inv, FriendlyByteBuf extraData){
        this(id,inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public GeneratingBoxMenu(int id, Inventory playerInv, BlockEntity entity) {
        super(ModBlockEntities.GENERATING_BOX_MENU.get(),id);
        this.blockEntity = (GeneratingBoxBlockEntity) entity;
        this.level = playerInv.player.level;
        blockEntity.startOpen(playerInv.player);
        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).ifPresent(h->{
            for (int u = 0; u < 4; ++u){
                this.addSlot(new BoxUpgradeSlot(h,u,8+u*18,18*3));
            }
        });
        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
            this.addSlot(new BoxShellSlot(h,0,8+8*18,18*3,blockEntity.getResourceGenerated().id()));
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new SlotItemHandler(h, l + 1, 8 + l * 18, 18));
            }
        });

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInv, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInv, j1, 8 + j1 * 18, 142));
        }
    }


    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 0;

    private static final int TE_INSERTABLE_INVENTORY_SLOT_COUNT = 5;
    private static final int TE_EXTRACTABLE_INVENTORY_SLOT_COUNT = 14;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = TE_INVENTORY_FIRST_SLOT_INDEX + TE_EXTRACTABLE_INVENTORY_SLOT_COUNT;


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_EXTRACTABLE_INVENTORY_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,false))
                return ItemStack.EMPTY;
        } else if (index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack,TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INSERTABLE_INVENTORY_SLOT_COUNT,false))
                return ItemStack.EMPTY;
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }


        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(Player player) {
        for (Block block : ModBlocks.getAllBox()) {
            if (stillValid(ContainerLevelAccess.create(level,blockEntity.getBlockPos()),player,block)) return true;
        }
        return false;
    }

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        blockEntity.stopOpen(p_38940_);
    }

    public GeneratingBoxBlockEntity getTile() {
        return blockEntity;
    }
}
