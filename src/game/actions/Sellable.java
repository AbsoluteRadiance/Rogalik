package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Interface for any item that can be sold to the {@link game.grounds.Supercomputer}.
 *
 * <p>{@link SellAction} calls {@link #getSellPrice()} and {@link #onSell(Actor, Location)}
 * without knowing the concrete item type (DIP, OCP). New sellable items only
 * need to implement this interface — no changes to SellAction needed.</p>
 *
 * <p>Note: First Aid Kit, Flask, and SterilisationBox are NOT sellable per spec.</p>
 *
 * @author Shivam
 */
public interface Sellable {

    /**
     * Returns the sell price for this item in credits.
     *
     * @return credits the worker receives
     */
    int getSellPrice();

    /**
     * Applies the side effects of selling this item to the seller.
     * Called immediately at the point of transaction.
     *
     * @param seller   the worker selling the item
     * @param location the seller's current location
     */
    void onSell(Actor seller, Location location);
}