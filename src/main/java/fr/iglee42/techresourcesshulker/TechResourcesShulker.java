package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.network.ModMessages;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TechResourcesShulker.MODID)
public class TechResourcesShulker {
    public static final String MODID = "techresourcesshulker";
    public static final CreativeModeTab GROUP = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModContent.SHULKER_ITEM.get());
        }
    };

    private static final Logger LOGGER = LogManager.getLogger();

    public TechResourcesShulker() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientEvents::onTextureStitch);
            bus.addListener(ClientEvents::registerItemColors);
        });
        Types.init();

        ModContent.register(bus);
        ModMessages.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }



}
