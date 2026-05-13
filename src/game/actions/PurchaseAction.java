package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Wallet;

/**
 * Action that purchases a {@link Purchasable} item from the {@link game.grounds.Supercomputer}.
 *
 * <p>Uses {@code asCapability(Wallet.class)} instead of instanceof to check
 * if the buyer can hold credits — reducing coupling and improving extensibility.
 * If the buyer cannot afford the item, {@link Purchasable#onBuy} is still called
 * (e.g. First Aid Kit kills the worker). All item logic stays in the item (SRP, DIP).</p>
 *
 * @author Shivam
 */
public class PurchaseAction extends Action {

    /** The catalogue entry being purchased. */
    private final Purchasable item;

    /**
     * Constructs a purchase action.
     *
     * @param item the purchasable catalogue entry
     */
    public PurchaseAction(Purchasable item) {
        this.item = item;
    }

    /**
     * Executes the purchase. Checks funds via asCapability(), deducts credits,
     * adds item, and triggers side effects.
     *
     * @param actor the worker buying the item
     * @param map   the current game map
     * @return a description of what happened
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Use asCapability() — no instanceof, decoupled from ContractedWorker
        Wallet wallet = actor.asCapability(Wallet.class).orElse(null);

        if (wallet == null) {
            return actor + " cannot hold credits.";
        }

        int price = item.getBuyPrice();

        if (!wallet.canAfford(price)) {
            // Still call onBuy — some items have consequences (e.g. First Aid Kit kills)
            item.onBuy(actor, map.locationOf(actor));
            return actor + " cannot afford " + item
                    + " (costs " + price + ", has " + wallet.getCredits() + " credits).";
        }

        // Deduct credits
        wallet.deductCredits(price);

        // Add new item instance to inventory
        Item newItem = item.createInstance();
        actor.getInventory().add(newItem);

        // Trigger purchase side effects
        item.onBuy(actor, map.locationOf(actor));

        return actor + " purchased " + item + " for " + price + " credits. "
                + "Remaining: " + wallet.getCredits() + " credits.";
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor performing the action
     * @return menu label
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Buy " + item + " for " + item.getBuyPrice() + " credits";
    }
}