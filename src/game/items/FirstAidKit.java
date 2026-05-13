package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.actions.Purchasable;
import game.actors.Wallet;
import game.enums.ItemStatistics;

/**
 * A first aid kit purchasable from the {@link game.grounds.Supercomputer}.
 *
 * <p>Costs 1000 credits. If the buyer cannot afford it, the Supercomputer
 * is deeply upset and kills the worker immediately. Uses {@code asCapability()}
 * to check wallet — no tight coupling to ContractedWorker.</p>
 *
 * @author Shivam
 */
public class FirstAidKit extends Item implements Purchasable, Consumable {

    private static final int BUY_PRICE = 1000;
    private static final int WEIGHT = 1;
    private static final int HEAL_AMOUNT = 10;
    private boolean used = false;

    /**
     * Constructor for FirstAidKit.
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Purchase price: 1000 credits.
     *
     * @return 1000
     */
    @Override
    public int getBuyPrice() {
        return BUY_PRICE;
    }

    /**
     * If the buyer cannot afford it, kills them.
     * Uses asCapability() to check wallet — decoupled from ContractedWorker.
     *
     * @param buyer    the worker buying
     * @param location the buyer's location
     */
    @Override
    public void onBuy(Actor buyer, Location location) {
        boolean canAfford = buyer.asCapability(Wallet.class)
                .map(w -> w.canAfford(BUY_PRICE))
                .orElse(false);

        if (!canAfford) {
            int currentHp = buyer.getStatistic(ActorStatistics.HEALTH);
            buyer.hurt(currentHp);
            System.out.println("The Supercomputer is DEEPLY UPSET! "
                    + buyer + " is killed for insufficient funds!");
        }
    }

    /**
     * Creates a new FirstAidKit instance.
     *
     * @return new FirstAidKit
     */
    @Override
    public Item createInstance() {
        return new FirstAidKit();
    }

    /**
     * Heals the carrier 10 HP when used.
     *
     * @param actor the actor consuming
     */
    @Override
    public void consume(Actor actor) {
        used = true;
        actor.heal(HEAL_AMOUNT);
        actor.getInventory().remove(this);
    }

    /**
     * Returns true after the kit is used once.
     *
     * @return true if used
     */
    @Override
    public boolean isDepleted() {
        return used;
    }

    /**
     * Provides a consume action when carried.
     *
     * @param owner the owner
     * @param map   the map
     * @return list of actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (!isDepleted()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    @Override
    public String toString() {
        return "First Aid Kit";
    }
}