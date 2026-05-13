package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.actions.Sellable;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

/**
 * A pack of expired cookies with 5 individual servings.
 *
 * <p><b>REQ1</b>: Implements {@link Sellable}. Dynamic price: 1 credit per
 * remaining cookie. Selling deducts 1 HP per cookie sold ("organic processing fee").</p>
 *
 * @author Ben (base), Shivam (REQ1 Sellable)
 */
public class Cookies extends Item implements Consumable, Sellable {

    private static final int WEIGHT = 2;
    private static final int MAX_USES = 5;
    private int cookiesRemaining;

    /**
     * Constructor for Cookies.
     */
    public Cookies() {
        super("Cookies", '◍');
        this.cookiesRemaining = MAX_USES;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Eats one cookie. Without SterilisationBox: permanently -1 max HP.
     * With one: heals 1 HP.
     *
     * @param actor the actor consuming
     */
    @Override
    public void consume(Actor actor) {
        cookiesRemaining--;
        if (actor.hasAbility(WorkerAbility.CAN_STERILISE)) {
            actor.heal(1);
        } else {
            actor.modifyStatisticMaximum(
                    edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH,
                    edu.monash.fit2099.engine.statistics.StatisticOperations.DECREASE, 1);
        }
        if (isDepleted()) {
            actor.getInventory().remove(this);
        }
    }

    /**
     * Returns true when all cookies are eaten.
     *
     * @return true if cookiesRemaining == 0
     */
    @Override
    public boolean isDepleted() {
        return cookiesRemaining == 0;
    }

    /**
     * Returns allowable actions.
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

    /**
     * Dynamic sell price: 1 credit per remaining cookie.
     *
     * @return cookiesRemaining
     */
    @Override
    public int getSellPrice() {
        return cookiesRemaining;
    }

    /**
     * Organic processing fee: seller loses 1 HP per cookie sold.
     *
     * @param seller   the worker selling
     * @param location the seller's location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        int cookiesSold = cookiesRemaining;
        seller.hurt(cookiesSold);
        System.out.println(seller + " pays the organic processing fee: -" + cookiesSold + " HP!");
    }

    @Override
    public String toString() {
        return "Cookies (" + cookiesRemaining + "/" + MAX_USES + ")";
    }
}