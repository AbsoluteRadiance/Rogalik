package game.actors;

/**
 * Interface representing a credit wallet for a {@link ContractedWorker}.
 *
 * <p>The Company enforces a strict maximum wallet balance of {@value #MAX_CREDITS}.
 * Implemented by {@link ContractedWorker} so the economy system can interact
 * with workers without instanceof checks (DIP).</p>
 *
 * <p>New economic actors can implement Wallet without touching
 * ContractedWorker (OCP).</p>
 *
 * @author Shivam
 */
public interface Wallet {

    /** Maximum credits any worker can hold. */
    int MAX_CREDITS = 1000;

    /**
     * Returns the current credit balance.
     *
     * @return credits held
     */
    int getCredits();

    /**
     * Adds credits to the wallet, capped at {@value #MAX_CREDITS}.
     *
     * @param amount credits to add
     */
    void addCredits(int amount);

    /**
     * Deducts credits from the wallet.
     *
     * @param amount credits to deduct
     * @return true if sufficient funds existed
     */
    boolean deductCredits(int amount);

    /**
     * Returns true if the wallet holds at least {@code amount} credits.
     *
     * @param amount the required credit amount
     * @return true if affordable
     */
    default boolean canAfford(int amount) {
        return getCredits() >= amount;
    }
}