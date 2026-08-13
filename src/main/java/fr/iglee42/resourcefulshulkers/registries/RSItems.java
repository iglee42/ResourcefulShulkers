package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import fr.iglee42.resourcefulshulkers.item.ShulkerHeadItem;
import fr.iglee42.resourcefulshulkers.item.shulker.ResourceShulkerItem;
import fr.iglee42.resourcefulshulkers.item.shulker.ShulkerItem;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.item.shulker.TypeShulkerItem;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

public class RSItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);

    public static final DeferredHolder<Item,ShulkerItem> SHULKER = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties()) {
        @Override
        public @NotNull EntityType<? extends Shulker> entityType() {
            return EntityType.SHULKER;
        }
    });
    public static final  DeferredHolder<Item,Item> BASE_ESSENCE = ITEMS.register("base_essence", () -> new Item(new Item.Properties()));

    public static final  DeferredHolder<Item,Item> UPGRADE_BASE = ITEMS.register("upgrade_base", ()-> new Item(new Item.Properties()));
    public static final  DeferredHolder<Item,UpgradeItem> SPEED_UPGRADE = ITEMS.register("speed_upgrade", ()-> new UpgradeItem(Upgrade.SPEED));
    public static final  DeferredHolder<Item,UpgradeItem> DURABILITY_UPGRADE = ITEMS.register("durability_upgrade", ()-> new UpgradeItem(Upgrade.DURABILITY));
    public static final  DeferredHolder<Item,UpgradeItem> QUANTITY_UPGRADE = ITEMS.register("quantity_upgrade", ()-> new UpgradeItem(Upgrade.QUANTITY));
    public static final  DeferredHolder<Item,UpgradeItem> SHELL_UPGRADE = ITEMS.register("shell_upgrade", ()-> new UpgradeItem(Upgrade.SHELL));

    public static final  DeferredHolder<Item, ShulkerHeadItem> SHULKER_HEAD = ITEMS.register("shulker_head", ShulkerHeadItem::new);

    // Types Relative Methods
    public static DeferredHolder<Item,Item> createEssence(ITypeDefinition type){
        return ITEMS.register(type.id().getPath() +"_essence", () -> new Item(new Item.Properties()));
    }

    public static DeferredHolder<Item, Item> createTypeShulker(ITypeDefinition type){
        return ITEMS.register(type.id().getPath() +"_shulker", () -> new TypeShulkerItem(new Item.Properties(), type));
    }

    //Shulkers Relative Methods
    public static DeferredHolder<Item,Item> createShell(IShulkerDefinition shulker){
        return ITEMS.register(shulker.id().getPath()+"_shell", () -> new ShellItem(shulker));
    }

    public static DeferredHolder<Item, Item> createResourceShulker(IShulkerDefinition shulker){
        return ITEMS.register(shulker.id().getPath() +"_shulker", () -> new ResourceShulkerItem(new Item.Properties(), shulker));
    }

}
