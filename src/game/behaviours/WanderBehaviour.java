package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * A behaviour that moves the actor to a random passable adjacent location.
 *
 * <p>Adapted from {@code WanderBehaviour} in
 * {@code edu.monash.fit2099.demo.forest}.
 *
 * @author Riordan D. Alfredo
 */
public class WanderBehaviour implements Behaviour<Actor, Action> {

    private final Random random = new Random();

    /**
     * Returns a {@link edu.monash.fit2099.engine.actions.MoveActorAction}
     * to a random passable exit, or null if no exits are available.
     *
     * @param actor    the actor performing the behaviour
     * @param location the current location of the actor
     * @return a random move action, or null
     */
    @Override
    public Action operate(Actor actor, Location location) {
        ArrayList<Action> actions = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                actions.add(destination.getMoveAction(actor, "around", exit.getHotKey()));
            }
        }
        if (!actions.isEmpty()) {
            return actions.get(random.nextInt(actions.size()));
        }
        return null;
    }
}