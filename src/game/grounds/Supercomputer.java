package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.Purchasable;
import game.actions.PurchaseAction;
import game.actions.Sellable;
import game.actions.SellAction;
import game.actors.Wallet;
import game.items.AccessCard;
import game.items.FirstAidKit;
import game.items.SterilisationBox;

import java.util.List;

/**
 * The Supercomputer terminal (represented by {@code ≡}) inside the armoured ship.
 *
 * <p>Workers adjacent to this terminal can sell {@link Sellable} items from
 * their inventory and purchase {@link Purchasable} items from the catalogue.
 * The Supercomputer is stateless — all transaction logic lives in
 * {@link SellAction} and {@link PurchaseAction} (SRP).</p>
 *
 * <p>Uses {@code asCapability(Wallet.class)} instead of instanceof to check
 * if the actor can participate in the economy — no coupling to ContractedWorker.
 * SellAction and PurchaseAction are the sole enforcers of transaction rules (OCP, DIP).</p>
 *
 * <p>The purchase catalogue is fixed at construction. New items can be added
 * without modifying this class (OCP).</p>
 *
 * @author Shivam
 */
public class Supercomputer extends Ground {

    /** Items available for purchase. */
    private final List<Purchasable> catalogue;

    /**
     * Constructs the Supercomputer with a fixed purchase catalogue.
     */
    public Supercomputer() {
        super('\u2261', "Supercomputer");
        this.catalogue = List.of(
                new FirstAidKit(),
                new SterilisationBox(),
                new AccessCard(1),
                new AccessCard(2),
                new AccessCard(3)
        );
    }

    /**
     * When an actor is adjacent, provides sell actions for every Sellable
     * in their inventory and buy actions for every catalogue entry.
     * Uses asCapability() to check Wallet — decoupled from ContractedWorker.
     * SellAction and PurchaseAction enforce who can actually transact.
     *
     * @param actor     the adjacent actor
     * @param location  the Supercomputer's location
     * @param direction direction from actor to terminal
     * @return list of sell and buy actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        // Only offer actions to actors that implement Wallet
        // SellAction and PurchaseAction handle the actual enforcement
        if (actor.asCapability(Wallet.class).isEmpty()) {
            return actions;
        }

        // Sell actions for every Sellable in inventory
        for (Item item : actor.getInventory().getItems()) {
            if (item instanceof Sellable sellable) {
                actions.add(new SellAction(sellable));
            }
        }

        // Buy actions for every catalogue entry
        for (Purchasable purchasable : catalogue) {
            actions.add(new PurchaseAction(purchasable));
        }

        return actions;
    }

    /**
     * No actor can enter the Supercomputer tile.
     *
     * @param actor the actor attempting to enter
     * @return false always
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}