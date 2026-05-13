package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;

/**
 * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment. Employees are reminded not to consume
 * all five charges in a panic during a single encounter.
 */
public class Flask extends Item implements Consumable {
    private static final int MAX_USES = 5;
    private static final int HEAL_AMOUNT = 1;
    private static final int WEIGHT = 3;
    private int totalUsable;

    /**
     * Constructor for the Flask.
     * Assigns its attributes according to the fields above,
     * and initialises the uses at max - 5.
     */
    public Flask() {
        super("Flask", 'u');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.totalUsable = MAX_USES;
    }

    /**
     * Consumes one use of the flask, decrementing the use counter by 1.
     * Heals the actor for 1 health.
     *
     * @param actor the actor who is consuming
     */
    @Override
    public void consume(Actor actor) {
        totalUsable -= 1;
        actor.heal(HEAL_AMOUNT);
    }

    /**
     * Returns whether the flask has been fully used.
     *
     * @return a boolean; true if the flask's current uses are less than or equal to 0, false otherwise
     */
    @Override
    public boolean isDepleted() {
        return totalUsable <= 0;
    }

    /**
     * Returns a list of allowable actions for the carrier.
     * Includes a ConsumeAction only if the flask still has uses.
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

    /**
     * Returns a drop action for this flask only if it is not depleted.
     * Depleted flasks cannot be dropped by workers; they remain deadweight in their inventory.
     *
     * @param actor the actor attempting to drop the flask
     * @return a DropAction if the flask is not fully depleted, null otherwise
     */
    @Override
    public DropAction getDropAction(Actor actor) {
        if (isDepleted()) {
            return null;
        }
        return super.getDropAction(actor);
    }
}
