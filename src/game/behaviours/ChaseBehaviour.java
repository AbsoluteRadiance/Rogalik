package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Targetable;

/**
 * A behaviour that moves a creature toward the nearest @Targetable actor.
 * Uses a "greedy" approach, selecting the adjacent tile that minimises
 * distance each turn.
 */
public class ChaseBehaviour implements Behaviour<Actor, Action> {

    /**
     * Operates the chase behaviour, moving the actor one step
     * toward the nearest target.
     * Will return null if no target is found.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return MoveActionActor towards target, or null if no entity found
     */
    @Override
    public Action operate(Actor actor, Location location) {
        // Find nearest targetable actor
        Actor target = findNearestTarget(location);
        if (target == null) {
            return null;
        }

        // Find best exit toward target
        Location targetLocation = location.map().locationOf(target);
        Exit bestExit = null;
        double bestDistance = Double.MAX_VALUE;

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                double distance = getDistance(destination, targetLocation);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestExit = exit;
                }
            }
        }

        if (bestExit != null) {
            return bestExit.getDestination().getMoveAction(actor, bestExit.getName(), bestExit.getHotKey());
        }
        return null;
    }

    /**
     * Finds the nearest Targetable actor.
     * Searches the entire map for an eligible actor.
     *
     * @param location the current location to search from
     * @return the nearest targetable actor as Actor, or null if none found
     */
    private Actor findNearestTarget(Location location) {
        Actor nearest = null;
        double bestDistance = Double.MAX_VALUE;

        for (Location nearby : location.getNearbyLocations(999)) {
            Targetable target = nearby.getActorAs(Targetable.class);
            if (target != null) {
                double distance = getDistance(location, nearby);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    nearest = (Actor) target;
                }
            }
        }
        return nearest;
    }

    /**
     * Calculates the distance between two locations.
     *
     * @param a the first location
     * @param b the second location
     * @return the straight line distance between a and b
     */
    private double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }
}