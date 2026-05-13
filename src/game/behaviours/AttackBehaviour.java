package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.actors.Targetable;

/**
 * A behaviour that attacks an adjacent Targetable actor.
 * Used by hostile creatures to attack other entities, namely Workers, within surroundings.
 * Returns null if no targetable actor is found in any adjacent tile.
 */
public class AttackBehaviour implements Behaviour<Actor, Action> {

    /**
     * Operates the attack behaviour, returning an AttackAction targeting
     * the first adjacent Targetable actor found.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an AttackAction if a target is found, null otherwise
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            Targetable target = destination.getActorAs(Targetable.class);
            if (target != null) {
                return new AttackAction((Actor) target);
            }
        }
        return null;
    }
}