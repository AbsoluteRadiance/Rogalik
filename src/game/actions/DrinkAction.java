package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An action that allows the actor to drink from a Drinkable object.
 * The effect of drinking depends entirely on the Drinkable implementation,
 * keeping this action decoupled from any specific drinkable entity.
 */
public class DrinkAction extends Action {
    private final Drinkable drinkable;

    /**
     * Constructor for DrinkAction.
     *
     * @param drinkable the object the actor drinks from
     */
    public DrinkAction(Drinkable drinkable) {
        this.drinkable = drinkable;
    }

    /**
     * Executes the drink action, delegating the effect to the Drinkable object.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a String describing the outcome of the drink action
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        drinkable.drink(actor);
        return actor + " drinks from " + drinkable;
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
        return actor + " drinks from " + drinkable;
    }
}