package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An action representing the desperate, mid-combat decision to chug whatever
 * liquid is sloshing around inside a flask.
 * Because nothing cures catastrophic injuries quite like aggressive hydration.
 *
 * @see Action
 */
public class ConsumeAction extends Action {

    private final Consumable consumable;

    /**
     * Constructor for ConsumeAction.
     *
     * @param consumable the object being consumed
     */
    public ConsumeAction(Consumable consumable) {
        this.consumable = consumable;
    }

    /**
     * Executes the consume action, delegating the effect to the Consumable object.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a String describing the outcome of the consume action
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        consumable.consume(actor);
        return actor + " consumes " + consumable;
    }

    /**
     * Descriptor for the menu console,
     * to inform players on what the action entails.
     *
     * @param actor The actor performing the action
     * @return a String confirming the action taken that turn
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " consumes " + consumable;
    }
}
