package fr.iglee42.resourcefulshulkers.init;

import fr.iglee42.igleelib.common.init.ModCreativeTab;
import fr.iglee42.resourcefulshulkers.item.GeneratingBoxItem;
import fr.iglee42.resourcefulshulkers.item.ShellItem;
import fr.iglee42.resourcefulshulkers.item.ShulkerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers"))
            .icon(()->new ItemStack(ModItems.OVERWORLD_ESSENCE.get()))
            .withTabsBefore(ModCreativeTab.TAB.getId())
            .build());
    public static final RegistryObject<CreativeModeTab> SHULKERS = CREATIVE_TABS.register("shulkers",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resourcefulshulkers.shulkers"))
            .icon(()->new ItemStack(ModItems.SHULKER_ITEM.get()))
            .withTabsBefore(MAIN.getId())
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        List<RegistryObject<Item>> holders = new ArrayList<>(ModItems.ITEMS.getEntries());
        if (event.getTabKey() == SHULKERS.getKey()){
        holders.stream().filter(h->h.get() instanceof ShellItem || (h.get() instanceof ShulkerItem si && (si.getType() != null && !Objects.equals(si.getType(), "elemental"))) || h.get() instanceof GeneratingBoxItem).forEach(holder->{
                event.accept(holder.get());
            });
        } else if (event.getTabKey() == MAIN.getKey()){
            holders.stream().filter(h->!(h.get() instanceof ShellItem ||  (h.get() instanceof ShulkerItem si && (si.getType() != null && !Objects.equals(si.getType(), "elemental"))) || h.get() instanceof GeneratingBoxItem)).forEach(h->event.accept(h.get()));
        }
    }
}