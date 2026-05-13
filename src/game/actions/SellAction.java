package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Wallet;

/**
 * Action that sells a {@link Sellable} item to the {@link game.grounds.Supercomputer}.
 *
 * <p>Transfers credits to the seller's {@link Wallet}, removes the item from
 * inventory, and immediately triggers the item's sell side effect via
 * {@link Sellable#onSell}. Uses {@code asCapability()} instead of instanceof
 * to reduce coupling and improve extensibility (OCP, DIP).</p>
 *
 * @author Shivam
 */
public class SellAction extends Action {

    /** The item being sold. */
    private final Sellable item;

    /**
     * Constructs a sell action for the given sellable item.
     *
     * @param item the item to sell (must also extend {@link Item})
     */
    public SellAction(Sellable item) {
        this.item = item;
    }

    /**
     * Executes the sale: credits the seller, removes the item,
     * and triggers sell side effects immediately.
     *
     * @param actor the worker selling the item
     * @param map   the current game map
     * @return a string describing the transaction result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int price = item.getSellPrice();

        // Use asCapability() — no instanceof, no coupling to ContractedWorker
        actor.asCapability(Wallet.class).ifPresent(wallet -> wallet.addCredits(price));

        // Remove item from inventory
        actor.getInventory().remove((Item) item);

        // Trigger item-specific sell side effects immediately
        item.onSell(actor, map.locationOf(actor));

        return actor + " sold " + item + " for " + price + " credits.";
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor performing the action
     * @return menu label
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Sell " + item + " for " + item.getSellPrice() + " credits";
    }
}