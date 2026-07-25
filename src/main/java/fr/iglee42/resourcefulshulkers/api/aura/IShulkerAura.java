package fr.iglee42.resourcefulshulkers.api.aura;

/**
 * Represents a container that holds a single, non-negative amount of Shulker Aura.
 * <p>
 * Aura is the energy resource used by Resourceful Shulkers. Implementations are
 * expected to keep the stored value within {@code [0, Integer.MAX_VALUE]} and to clamp any operation that would exceed those bounds rather than overflowing
 * or going negative.
 *
 * @see fr.iglee42.resourcefulshulkers.aura.ShulkerAura
 */
public interface IShulkerAura {

    /**
     * Returns the amount of aura currently stored.
     *
     * @return the stored aura, always {@code >= 0}
     */
    int getAura();

    /**
     * Overwrites the stored aura with the given value.
     * <p>
     * This is an unconditional assignment: unlike {@link #insertAura(int, boolean)}
     * and {@link #extractAura(int, boolean)}, it does not clamp against the current
     * contents. Callers are responsible for passing a valid, non-negative amount.
     *
     * @param aura the new aura amount to store
     */
    void setAura(int aura);

    /**
     * Attempts to remove aura from this container.
     * <p>
     * At most the requested amount is removed, and never more than what is currently
     * stored, so the result may be smaller than {@code extract} when the container
     * runs dry.
     *
     * @param extract  the maximum amount of aura to remove; should be non-negative
     * @param simulate if {@code true}, the operation is only evaluated and the stored
     *                 amount is left unchanged; if {@code false}, the aura is actually removed
     * @return the amount of aura that was (or, when simulating, would be) removed
     */
    int extractAura(int extract, boolean simulate);

    /**
     * Attempts to add aura to this container.
     * <p>
     * At most the requested amount is added, and never more than the remaining free
     * capacity, so the result may be smaller than {@code insert} when the container
     * is near full.
     *
     * @param insert   the maximum amount of aura to add; should be non-negative
     * @param simulate if {@code true}, the operation is only evaluated and the stored
     *                 amount is left unchanged; if {@code false}, the aura is actually added
     * @return the amount of aura that was (or, when simulating, would be) added
     */
    int insertAura(int insert, boolean simulate);

}
