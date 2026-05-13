package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.status.DamageOverTimeStatus;

/**
 * Represents a spoiled apple found on the Moon's facility.
 * When consumed with a SterilisationBox, the apple poisons the consumer.
 * This deals damage over time.
 * With a sterilisation box, the apple heals the consumer instead.
 */
public class Apple extends Item implements Consumable {
    private static final int WEIGHT = 1;
    private static final int POISON_TURNS = 5;
    private static final int POISON_DAMAGE = 1;
    private static final int HEAL_AMOUNT = 3;

    /**
     * Constructor for Apple.
     * Assigns its attributes according to the fields above.
     */
    public Apple() {
        super("Apple", 'ó');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Consumes the apple, applying either a poison or heal effect
     * depending on whether the consumer has a sterilisation box.
     * Removes the apple from the worker's inventory after consumption.
     *
     * @param actor
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
     * Returns false as the apple is always consumable.
     * @return false, always
     */
    @Override
    public boolean isDepleted() {
        return false;
    }

    /**
     * Returns the list of allowable actions for the actor.
     * Always includes a ConsumeAction for this apple
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new ConsumeAction(this));
        return actions;
    }
}