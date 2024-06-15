package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.igleelib.common.init.ModCreativeTab;
import fr.iglee42.igleelib.common.init.ModItem;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import fr.iglee42.resourcefulshulkers.item.ShulkerItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,MODID);

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> MAIN = CREATIVE_TABS.register("main",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers"))
            .icon(ModItems.OVERWORLD_ESSENCE::toStack)
            .withTabsBefore(ModCreativeTab.TAB.getId())
            .build());
    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> SHULKERS = CREATIVE_TABS.register("shulkers",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers.shulkers"))
            .icon(ModItems.SHULKER_ITEM::toStack)
            .withTabsBefore(MAIN.getId())
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        List<DeferredHolder<Item,? extends Item>> holders = new ArrayList<>(ModItems.ITEMS.getEntries());
        if (event.getTabKey() == SHULKERS.getKey()){
            holders.stream().filter(h->h.value() instanceof ShellItem || h.value() instanceof ShulkerItem || h.value() instanceof GeneratingBoxItem).forEach(holder->{
                event.accept(holder.get());
            });
        } else if (event.getTabKey() == MAIN.getKey()){
            holders.stream().filter(h->!(h.value() instanceof ShellItem || h.value() instanceof ShulkerItem || h.value() instanceof GeneratingBoxItem)).forEach(h->event.accept(h.get()));
        }
    }
}
