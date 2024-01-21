package fr.iglee42.techresourcesshulker.init;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.entity.CustomShulker;
import fr.iglee42.techresourcesshulker.item.ShulkerItem;
import fr.iglee42.techresourcesshulker.item.UpgradeItem;
import fr.iglee42.techresourcesshulker.utils.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechResourcesShulker.MODID);

    public static final RegistryObject<Item> SHULKER_KILLER = ITEMS.register("shulker_killer", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)){
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            List<CustomShulker> target = level.getEntitiesOfClass(CustomShulker.class, player.getBoundingBox().inflate(8), (entity) -> true);
            for (CustomShulker s : target){
                s.remove(Entity.RemovalReason.KILLED);
            }
            if (!player.isCreative())player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        @Override
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            p_41423_.add(new TextComponent("Kill all custom shulker in radius of 8 blocks").withStyle(ChatFormatting.YELLOW));
            p_41423_.add(new TextComponent("Creative Only").withStyle(ChatFormatting.RED));
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        }
    });
    public static final RegistryObject<Item> SHULKER_ITEM = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP), EntityType.SHULKER));
    public static final RegistryObject<Item> OVERWORLD_SHULKER_ITEM = ITEMS.register("overworld_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> SKY_SHULKER_ITEM = ITEMS.register("sky_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> NETHER_SHULKER_ITEM = ITEMS.register("nether_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> END_SHULKER_ITEM = ITEMS.register("end_shulker", () -> new ShulkerItem(new Item.Properties().tab(TechResourcesShulker.GROUP),null));
    public static final RegistryObject<Item> OVERWORLD_ESSENCE = ITEMS.register("overworld_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> SKY_ESSENCE = ITEMS.register("sky_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> NETHER_ESSENCE = ITEMS.register("nether_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));

    public static final RegistryObject<Item> UPGRADE_BASE = ITEMS.register("upgrade_base", ()-> new Item(new Item.Properties().tab(TechResourcesShulker.GROUP)));
    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", ()-> new UpgradeItem(Upgrade.SPEED));
    public static final RegistryObject<Item> DURABILITY_UPGRADE = ITEMS.register("durability_upgrade", ()-> new UpgradeItem(Upgrade.DURABILITY));

}
