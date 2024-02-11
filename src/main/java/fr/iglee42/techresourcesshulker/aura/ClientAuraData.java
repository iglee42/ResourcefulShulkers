package fr.iglee42.techresourcesshulker.aura;

public class ClientAuraData {

    private static int chunkMana;

    public static void set(int chunkMana) {
        ClientAuraData.chunkMana = chunkMana;
    }
    public static int getChunkMana() {
        return chunkMana;
    }
}