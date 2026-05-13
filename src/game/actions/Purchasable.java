package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Interface for any item that can be purchased from the {@link game.grounds.Supercomputer}.
 *
 * <p>{@link PurchaseAction} calls {@link #getBuyPrice()}, {@link #onBuy(Actor, Location)},
 * and {@link #createInstance()} without knowing the concrete type (DIP, OCP).</p>
 *
 * @author Shivam
 */
public interface Purchasable {

    /**
     * Returns the purchase price in credits.
     *
     * @return credits required to buy
     */
    int getBuyPrice();

    /**
     * Applies immediate side effects of the purchase.
     * Called even if the buyer cannot afford it (some items have
     * consequences for insufficient funds).
     *
     * @param buyer    the worker buying the item
     * @param location the buyer's current location
     */
    void onBuy(Actor buyer, Location location);

    /**
     * Creates and returns a fresh instance of this item for the buyer's inventory.
     *
     * @return a new Item instance
     */
    Item createInstance();
}