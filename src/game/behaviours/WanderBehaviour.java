package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A behaviour that moves a creature to a random adjacent tile.
 * The primary movement behaviour of creatures unless a higher priority
 * behaviour is being executed (namely, ChaseBehaviour).
 */
public class WanderBehaviour implements Behaviour<Actor, Action> {
    private final Random random = new Random();

    /**
     * Operates the wander behaviour, moving the actor
     * to a random valid adjacent tile.
     * Will return null if no valid moves exist.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return MoveActionActor to random adjacent tile, or null if none available.
     */
    @Override
    public Action operate(Actor actor, Location location) {
        List<Action> moveActions = new ArrayList<>();
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            Action moveAction = destination.getMoveAction(actor, exit.getName(), exit.getHotKey());
            if (moveAction != null) {
                moveActions.add(moveAction);
            }
        }
        if (!moveActions.isEmpty()) {
            return moveActions.get(random.nextInt(moveActions.size()));
        }
        return null;
    }
}