package fr.iglee42.resourcefulshulkers.aura;

import com.mojang.serialization.Codec;
import fr.iglee42.resourcefulshulkers.api.aura.IShulkerAura;
import fr.iglee42.resourcefulshulkers.config.RSServerConfig;
import net.minecraft.network.FriendlyByteBuf;

public class ShulkerAura implements IShulkerAura {

    public static final Codec<ShulkerAura> CODEC = Codec.intRange(0, RSServerConfig.getMaxAura())
            .xmap(ShulkerAura::new, ShulkerAura::getAura);

    private int aura;

    public ShulkerAura() {
        this(0);
    }

    public ShulkerAura(int aura) {
        this.aura = aura;
    }

    @Override
    public int getAura() {
        return aura;
    }

    @Override
    public void setAura(int aura) {
        this.aura = aura;
    }

    @Override
    public int extractAura(int amount, boolean simulate) {
        if (aura <= 0) return 0;
        if (amount <= 0) return 0;
        int extracted = Math.min(aura, amount);
        if (!simulate) setAura(aura - extracted);
        return extracted;
    }

    @Override
    public int insertAura(int amount, boolean simulate) {
        if (aura >= RSServerConfig.getMaxAura()) return 0;
        if (amount <= 0) return 0;
        int added = Math.min(RSServerConfig.getMaxAura() - aura, amount);
        if (!simulate) setAura(aura + added);
        return added;
    }
}
