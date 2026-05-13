package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

/**
 * Represents a package of expired Cookies found on the moon's facility.
 * Contains 5 individual cookies, each of which can be consumed separately.
 * Without a SterilisationBox, each cookies permanently decreases the consumer's maximum health by 1.
 * With a sterilisation box a cookie healths the consumer for 1 health.
 * The package is removed from the inventory once all cookies have been consumed.
 */
public class Cookies extends Item implements Consumable {
    private static final int WEIGHT = 2;
    private static final int MAX_USES = 5;
    private static final int HEAL_AMOUNT = 1;
    private int usesRemaining;

    /**
     * Constructor for Cookies.
     * Assigns its attributes according to the fields above.
     */
    public Cookies() {
        super("Cookies", '◍');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.usesRemaining = MAX_USES;
    }

    /**
     * Consumes one cookie, applying either a heal or maximum health decrease,
     * dependent on if the consumer is carrying a SterilisationBox.
     * Removes the package from the actor's inventory once all cookies are consumed.
     *
     * @param actor the actor who is consuming
     */
    @Override
    public void consume(Actor actor) {
        if (actor.hasAbility(WorkerAbility.CAN_STERILISE)) {
            actor.heal(HEAL_AMOUNT);
        } else {
            actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1);
        }
        usesRemaining -= 1;
        if (isDepleted()) {
            actor.getInventory().remove(this);
        }
    }

    /**
     * Returns whether all cookies in the package have been eaten.
     *
     * @return a boolean; true if there are no cookies left, otherwise false
     */
    @Override
    public boolean isDepleted() {
        return usesRemaining <= 0;
    }

    /**
     * Returns a list of allowable actions for the carrier.
     * Includes a ConsumeAction only if there are still cookies left in the package.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (!isDepleted()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }
}