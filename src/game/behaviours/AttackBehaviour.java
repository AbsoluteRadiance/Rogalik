package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.utils.GameAbilities;

/**
 * A behaviour that attacks an adjacent actor with the {@link GameAbilities#IS_WORKER}
 * ability. Creatures with this behaviour will completely ignore other creatures.
 */
public class AttackBehaviour implements Behaviour<Actor, Action> {

    /**
     * Scans all exits for a target with {@link GameAbilities#IS_WORKER}.
     * Returns an {@link AttackAction} against the first valid target found,
     * or null if no valid targets are adjacent.
     *
     * @param actor    the actor performing the behaviour
     * @param location the current location of the actor
     * @return an {@link AttackAction} or null
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Exit exit : location.getExits()) {
            Actor target = exit.getDestination().getActor();
            if (target != null && target.hasAbility(GameAbilities.IS_WORKER)) {
                return new AttackAction(target, exit.getName());
            }
        }
        return null;
    }
}