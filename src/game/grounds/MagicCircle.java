package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.actions.Teleportable;
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
 * <p>Implements {@link Teleportable} so that {@link #findOtherCircles} can
 * discover peer circles via
 * {@link Location#getGroundAs(Class) getGroundAs(Teleportable.class)} —
 * satisfying the engine's requirement that capability types be interfaces or
 * abstract classes (avoids the {@link IllegalArgumentException} thrown when
 * a concrete class is passed to {@code asCapability}).</p>
 */
public class MagicCircle extends Ground implements Teleportable {

    private final Random random = new Random();

    /** The location of this circle, set the first time {@link #allowableActions} is called. */
    private Location cachedLocation = null;

    /** Constructor for MagicCircle. */
    public MagicCircle() {
        super('◎', "Magic Circle");
    }

    /**
     * Returns this circle's location on the map.
     *
     * <p>The location is cached on the first call to {@link #allowableActions}
     * because the ground is not given its location at construction time.</p>
     *
     * @return the {@link Location} of this circle, or {@code null} if not yet placed
     */
    @Override
    public Location getLocation() {
        return cachedLocation;
    }

    /**
     * When the worker is standing directly on this circle (direction is empty),
     * offers a teleport action to a randomly selected other magic circle.
     *
     * @param actor     the actor standing on this tile
     * @param location  this circle's location (used to cache and find peers)
     * @param direction direction from the actor to this ground (empty if standing on it)
     * @return list containing one teleport action, or empty if no other circle exists
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        cachedLocation = location; // cache for getLocation()

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
     * Scans the current map for all other {@link Teleportable} tiles.
     *
     * <p>Uses {@link Location#getGroundAs(Class) getGroundAs(Teleportable.class)}
     * (interface lookup) rather than the concrete {@code MagicCircle} class, which
     * would cause an {@link IllegalArgumentException} from the engine's
     * {@code asCapability} guard.</p>
     *
     * @param thisLocation this circle's location (excluded from results)
     * @return list of locations containing other magic circles
     */
    private List<Location> findOtherCircles(Location thisLocation) {
        List<Location> circles = new ArrayList<>();
        for (Location nearby : thisLocation.getNearbyLocations(Integer.MAX_VALUE)) {
            Teleportable peer = nearby.getGroundAs(Teleportable.class);
            if (peer != null && !nearby.equals(thisLocation)) {
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