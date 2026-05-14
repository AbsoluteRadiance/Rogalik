package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.items.Flask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A magic circle ({@code ◎}) found inside the 20-overflow moon.
 *
 * <p>Stepping on a magic circle and activating it randomly teleports the worker
 * to one of the other magic circles on the same map. The magical energy surge
 * upon arrival immediately spawns one {@link Flask} on an empty tile directly
 * adjacent to the destination circle.</p>
 *
 * <p>If the worker is the only circle on the map (i.e., no other destination
 * exists), no action is offered.</p>
 */
public class MagicCircle extends Ground {

    private final Random random = new Random();

    /** Constructor for MagicCircle. */
    public MagicCircle() {
        super('◎', "Magic Circle");
    }

    /**
     * When the worker is standing directly on this circle (direction is empty),
     * offers a teleport action to a randomly selected other magic circle.
     *
     * @param actor     the actor standing on this tile
     * @param location  this circle's location
     * @param direction direction from the actor to this ground (empty if standing on it)
     * @return list containing one teleport action, or empty if no other circle exists
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (!direction.isEmpty()) {
            return actions;
        }

        List<Location> otherCircles = findOtherCircles(location);
        if (otherCircles.isEmpty()) {
            return actions;
        }

        Location dest = otherCircles.get(random.nextInt(otherCircles.size()));
        String label = "activates magic circle → (" + dest.x() + ", " + dest.y() + ")";
        actions.add(new TeleportAction(dest, label, this::spawnFlaskOnArrival));
        return actions;
    }

    /**
     * Scans the current map for all other {@link MagicCircle} ground tiles.
     *
     * @param thisLocation this circle's location (excluded from results)
     * @return list of locations containing other magic circles
     */
    private List<Location> findOtherCircles(Location thisLocation) {
        List<Location> circles = new ArrayList<>();
        for (Location nearby : thisLocation.getNearbyLocations(Integer.MAX_VALUE)) {
            if (nearby.getGroundAs(MagicCircle.class) != null && !nearby.equals(thisLocation)) {
                circles.add(nearby);
            }
        }
        return circles;
    }

    /**
     * Spawns a {@link Flask} on the first empty adjacent tile of the destination.
     * Called as the {@link game.actions.TeleportEffect} after the actor arrives.
     * Silently skips if all adjacent tiles are occupied or impassable.
     *
     * @param actor       the actor who teleported
     * @param destination the circle the actor arrived at
     * @param source      the circle the actor departed from (unused)
     */
    private void spawnFlaskOnArrival(Actor actor, Location destination, Location source) {
        for (Exit exit : destination.getExits()) {
            Location adj = exit.getDestination();
            if (!adj.containsAnActor() && adj.getGround().canActorEnter(actor)) {
                adj.addItem(new Flask());
                System.out.println("Magical energy surge! A Flask appears next to the magic circle!");
                return;
            }
        }
    }
}