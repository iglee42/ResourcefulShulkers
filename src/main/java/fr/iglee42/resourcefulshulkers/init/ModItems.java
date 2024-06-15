package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import fr.iglee42.resourcefulshulkers.item.ShulkerItem;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> SHULKER_KILLER = ITEMS.register("shulker_killer", () -> new Item(new Item.Properties()){
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
            p_41423_.add(Component.literal("Kill all custom shulker in radius of 8 blocks").withStyle(ChatFormatting.YELLOW));
            p_41423_.add(Component.literal("Creative Only").withStyle(ChatFormatting.RED));
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        }
    });
    public static final RegistryObject<Item> SHULKER_ITEM = ITEMS.register("shulker_item", () -> new ShulkerItem(new Item.Properties(), EntityType.SHULKER));
    public static final RegistryObject<Item> OVERWORLD_SHULKER_ITEM = ITEMS.register("overworld_shulker", () -> new ShulkerItem(new Item.Properties(),null));
    public static final RegistryObject<Item> SKY_SHULKER_ITEM = ITEMS.register("sky_shulker", () -> new ShulkerItem(new Item.Properties(),null));
    public static final RegistryObject<Item> NETHER_SHULKER_ITEM = ITEMS.register("nether_shulker", () -> new ShulkerItem(new Item.Properties(),null));
    public static final RegistryObject<Item> END_SHULKER_ITEM = ITEMS.register("end_shulker", () -> new ShulkerItem(new Item.Properties(),null));
    public static final RegistryObject<Item> OVERWORLD_ESSENCE = ITEMS.register("overworld_essence", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SKY_ESSENCE = ITEMS.register("sky_essence", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_ESSENCE = ITEMS.register("nether_essence", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BASE_ESSENCE = ITEMS.register("base_essence", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UPGRADE_BASE = ITEMS.register("upgrade_base", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", ()-> new UpgradeItem(Upgrade.SPEED));
    public static final RegistryObject<Item> DURABILITY_UPGRADE = ITEMS.register("durability_upgrade", ()-> new UpgradeItem(Upgrade.DURABILITY));
    public static final RegistryObject<Item> SHULKER_HEAD = ITEMS.register("shulker_head", ()-> new StandingAndWallBlockItem(ModBlocks.SHULKER_HEAD.get(),ModBlocks.WALL_SHULKER_HEAD.get(), new Item.Properties(),Direction.DOWN){
        @Override
        public void onArmorTick(ItemStack stack, Level level, Player player) {
            if (!level.isClientSide)
            {
                Advancement adv = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(MODID,"head"));
                Iterator<String> it = ((ServerPlayer)player).getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                while (it.hasNext()){
                    String criteria = it.next();
                    ((ServerPlayer)player).getAdvancements().award(adv,criteria);
                }
                if (level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE,player.blockPosition()).getY() + 10 > player.position().y())
                {
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION,20,1,false,false));
                } else if ((level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE,player.blockPosition()).getY() + 10) == (int) player.position().y()){
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION,20,255,false,false));
                }
            }
            super.onArmorTick(stack, level, player);
        }
    });

    public static Item getShulkerItemById(ResourceLocation resourceId) {
        Optional<RegistryObject<Item>> item = ITEMS.getEntries().stream().filter(r-> Objects.equals(r.getId(), new ResourceLocation(MODID, resourceId.getPath() + "_shulker"))).findFirst();
        return item.map(RegistryObject::get).orElse(ModItems.SHULKER_ITEM.get());
    }

    public static Item getShellById(ResourceLocation resourceId) {
        Optional<RegistryObject<Item>> item = ITEMS.getEntries().stream().filter(r->r.get() instanceof ShellItem s && s.getId() == resourceId).findFirst();
        return item.map(RegistryObject::get).orElse(Items.SHULKER_SHELL);
    }
    public static void createShell(ResourceLocation id){
        ShulkerType res = ShulkerType.getById(id);
        ITEMS.register(res.id().getPath()+"_shell", () -> new ShellItem(id));

    }

}
