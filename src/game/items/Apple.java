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
import game.status.DamageOverTimeStatus;

/**
 * Represents a spoiled apple found on the Moon's facility.
 *
 * <p><b>REQ1</b>: Implements {@link Sellable}. Sells for 1 credit.
 * Selling immediately poisons the seller for 2 turns (2 dmg/turn)
 * unless they carry a SterilisationBox.</p>
 *
 * @author Ben (base), Shivam (REQ1 Sellable)
 */
public class Apple extends Item implements Consumable, Sellable {

    private static final int WEIGHT = 1;
    private static final int POISON_TURNS = 5;
    private static final int POISON_DAMAGE = 1;
    private static final int HEAL_AMOUNT = 3;
    private static final int SELL_PRICE = 1;
    private static final int SELL_POISON_TURNS = 2;
    private static final int SELL_POISON_DAMAGE = 2;

    /**
     * Constructor for Apple.
     */
    public Apple() {
        super("Apple", 'ó');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Consumes the apple. Poisons without SterilisationBox, heals with one.
     * Removes the apple from inventory after consumption.
     *
     * @param actor the actor consuming the apple
     */
    @Override
    public void consume(Actor actor) {
        if (actor.hasAbility(WorkerAbility.CAN_STERILISE)) {
            actor.heal(HEAL_AMOUNT);
        } else {
            actor.addStatus(new DamageOverTimeStatus(POISON_TURNS, POISON_DAMAGE));
        }
        actor.getInventory().remove(this);
    }

    /**
     * Apple is always consumable (never depleted before use).
     *
     * @return false always
     */
    @Override
    public boolean isDepleted() {
        return false;
    }

    /**
     * Returns allowable actions including a ConsumeAction.
     *
     * @param owner the actor that owns the item
     * @param map   the map
     * @return list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new ConsumeAction(this));
        return actions;
    }

    /**
     * Sell price: 1 credit.
     *
     * @return 1
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Selling poisons the seller for 2 turns unless they have a SterilisationBox.
     *
     * @param seller   the worker selling
     * @param location the seller's location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        if (!seller.hasAbility(WorkerAbility.CAN_STERILISE)) {
            seller.addStatus(new DamageOverTimeStatus(SELL_POISON_TURNS, SELL_POISON_DAMAGE));
            System.out.println(seller + " is poisoned from selling the apple! ("
                    + SELL_POISON_DAMAGE + " dmg/turn for " + SELL_POISON_TURNS + " turns)");
        } else {
            System.out.println(seller + "'s SterilisationBox protects them from the apple's toxins.");
        }
    }
}