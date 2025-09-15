package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import fr.iglee42.resourcefulshulkers.item.ShulkerItem;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);

    public static final  DeferredHolder<Item,Item> SHULKER_KILLER = ITEMS.register("shulker_killer", () -> new Item(new Item.Properties()){
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
        public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            p_41423_.add(Component.literal("Kill all custom shulker in radius of 8 blocks").withStyle(ChatFormatting.YELLOW));
            p_41423_.add(Component.literal("Creative Only").withStyle(ChatFormatting.RED));
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        }
    });
    public static final DeferredHolder<Item,ShulkerItem> SHULKER_ITEM = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties(), EntityType.SHULKER,null));
    //public static final RegistryObject<Item> OVERWORLD_SHULKER_ITEM = ITEMS.register("overworld_shulker", () -> new ShulkerItem(new Item.Properties(),null,"elemental"));
    //public static final RegistryObject<Item> SKY_SHULKER_ITEM = ITEMS.register("sky_shulker", () -> new ShulkerItem(new Item.Properties(),null,"elemental"));
    //public static final RegistryObject<Item> NETHER_SHULKER_ITEM = ITEMS.register("nether_shulker", () -> new ShulkerItem(new Item.Properties(),null,"elemental"));
    //public static final RegistryObject<Item> END_SHULKER_ITEM = ITEMS.register("end_shulker", () -> new ShulkerItem(new Item.Properties(),null,"elemental" ));
    //public static final RegistryObject<Item> OVERWORLD_ESSENCE = ITEMS.register("overworld_essence", () -> new Item(new Item.Properties()));
    //public static final RegistryObject<Item> SKY_ESSENCE = ITEMS.register("sky_essence", () -> new Item(new Item.Properties()));
    //public static final RegistryObject<Item> NETHER_ESSENCE = ITEMS.register("nether_essence", () -> new Item(new Item.Properties()));
    //public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new Item(new Item.Properties()));
    public static final  DeferredHolder<Item,Item> BASE_ESSENCE = ITEMS.register("base_essence", () -> new Item(new Item.Properties()));

    public static final  DeferredHolder<Item,Item> UPGRADE_BASE = ITEMS.register("upgrade_base", ()-> new Item(new Item.Properties()));
    public static final  DeferredHolder<Item,UpgradeItem> SPEED_UPGRADE = ITEMS.register("speed_upgrade", ()-> new UpgradeItem(Upgrade.SPEED));
    public static final  DeferredHolder<Item,UpgradeItem> DURABILITY_UPGRADE = ITEMS.register("durability_upgrade", ()-> new UpgradeItem(Upgrade.DURABILITY));
    public static final  DeferredHolder<Item,UpgradeItem> QUANTITY_UPGRADE = ITEMS.register("quantity_upgrade", ()-> new UpgradeItem(Upgrade.QUANTITY));
    public static final  DeferredHolder<Item,UpgradeItem> SHELL_UPGRADE = ITEMS.register("shell_upgrade", ()-> new UpgradeItem(Upgrade.SHELL));
    public static final  DeferredHolder<Item,StandingAndWallBlockItem> SHULKER_HEAD = ITEMS.register("shulker_head", ()-> new StandingAndWallBlockItem(ModBlocks.SHULKER_HEAD.get(),ModBlocks.WALL_SHULKER_HEAD.get(), new Item.Properties(),Direction.DOWN){

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean p_41408_) {
            if (!level.isClientSide && entity instanceof Player player)
            {
                if (player.getItemBySlot(EquipmentSlot.HEAD).equals(stack)) {
                    AdvancementHolder adv = player.getServer().getAdvancements().get(ResourceLocation.fromNamespaceAndPath(MODID, "head"));
                    Iterator<String> it = ((ServerPlayer) player).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                    while (it.hasNext()) {
                        String criteria = it.next();
                        ((ServerPlayer) player).getAdvancements().award(adv, criteria);
                    }
                    if (level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, player.blockPosition()).getY() + 10 > player.position().y()) {
                        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 1, false, false));
                    }
                }
            }
            super.inventoryTick(stack, level, entity, slot, p_41408_);
        }


        @Override
        public void appendHoverText(ItemStack p_40572_, @Nullable TooltipContext p_40573_, List<Component> tooltips, TooltipFlag p_40575_) {
            tooltips.add(Component.literal("Try it on your head !").withStyle(ChatFormatting.LIGHT_PURPLE));
            if (ResourcefulShulkersConfig.HEAD_TARGET_ESSENCE.get()) {
                if (Screen.hasShiftDown()) {
                    tooltips.add(Component.translatable("tooltip.resourcefulshulkers.place_on_target").withStyle(ChatFormatting.DARK_PURPLE));
                } else {
                    tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift").withStyle(ChatFormatting.DARK_PURPLE));
                }
            }
            super.appendHoverText(p_40572_, p_40573_, tooltips, p_40575_);
        }
    });

    public static Item getShulkerItemById(ResourceLocation resourceId) {
        Optional<DeferredHolder<Item,? extends Item>> item = ITEMS.getEntries().stream().filter(r-> Objects.equals(r.getId(), ResourceLocation.fromNamespaceAndPath(MODID, resourceId.getPath() + "_shulker"))).findFirst();
        return item.map(i -> BuiltInRegistries.ITEM.get(i.getId())).orElseGet(ModItems.SHULKER_ITEM);
    }

    public static Item getShellById(ResourceLocation resourceId) {
        Optional<DeferredHolder<Item,? extends Item>> item = ITEMS.getEntries().stream().filter(r->r.get() instanceof ShellItem s && s.getId().equals(resourceId)).findFirst();
        return item.isPresent() ? item.get().get() : Items.SHULKER_SHELL;
    }

    public static void createEssence(ResourceLocation id){
        ITEMS.register(id.getPath()+"_essence", () -> new Item(new Item.Properties()));
    }
    public static void createShell(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        ITEMS.register(res.id().getPath()+"_shell", () -> new ShellItem(id));

    }

}
