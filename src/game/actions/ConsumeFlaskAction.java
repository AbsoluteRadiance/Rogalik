package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Flask;

/**
 * An action representing the desperate, mid-combat decision to chug whatever
 * liquid is sloshing around inside a flask.
 * Each use heals 1 point of health. Once depleted, the flask remains in the
 * worker's inventory as a bitter reminder of poor resource management.
 *
 * @see Action
 */
public class ConsumeFlaskAction extends Action {

    private final Flask flask;

    /**
     * Constructor.
     *
     * @param flask the flask to consume
     */
    public ConsumeFlaskAction(Flask flask) {
        this.flask = flask;
    }

    /**
     * Consumes one use of the flask, healing the actor.
     * If the flask is depleted, nothing happens.
     *
     * @param actor The actor consuming the flask.
     * @param map   The map the actor is on.
     * @return the description of the result of the action
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int healAmount = flask.consume();
        if (healAmount > 0) {
            actor.heal(healAmount);
            if (flask.isDepleted()) {
                return actor + " drinks from the flask, healing " + healAmount + " HP. The flask is now empty!";
            }
            return actor + " drinks from the flask, healing " + healAmount + " HP. ("
                    + flask.getRemainingUses() + " uses remaining)";
        }
        return actor + " tries to drink from the flask, but it's empty.";
    }

    @Override
    public String menuDescription(Actor actor) {
        if (flask.isDepleted()) {
            return actor + " tries to drink from the empty flask (depleted)";
        }
        return actor + " drinks from the flask (" + flask.getRemainingUses() + " uses remaining)";
    }
}