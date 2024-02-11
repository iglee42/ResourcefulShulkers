package fr.iglee42.techresourcesshulker.aura;

public class ShulkerAura {

    public static final int MAX_AURA = 262144;
    private int aura;

    public ShulkerAura() {
        this.aura = 0;
    }
    public ShulkerAura(int aura) {
        this.aura = aura;
    }

    public int getAura() {
        return aura;
    }

    public void setAura(int aura) {
        this.aura = aura;
    }
}
