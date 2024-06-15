package fr.iglee42.resourcefulshulkers.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DURABILITY = COMPONENTS.register("durability", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
}
