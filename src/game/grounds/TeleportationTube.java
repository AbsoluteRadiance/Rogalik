package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.actions.TeleportEffect;
import game.actions.Tubeable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A teleportation tube ({@code Φ}) installed inside armoured ships.
 *
 * <p>Workers can use a tube to jump to one of its pre-configured destination
 * locations, which may be on the same map or on a different map entirely.</p>
 *
 * <h3>Behaviour</h3>
 * <ul>
 *   <li>Each destination appears as a separate menu option.</li>
 *   <li>On use, there is a <b>50% chance of malfunction</b>: the worker is
 *       teleported to a random passable location within the destination map
 *       instead of the intended spot.</li>
 *   <li>Regardless of malfunction, all tiles <em>adjacent to the actual arrival
 *       location</em> are set on fire for 2 turns.</li>
 * </ul>
 *
 * <p>Destinations are registered at construction time via
 * {@link #addDestination(Location, String)} and are stored as a list,
 * satisfying the requirement that all teleportation devices support multiple
 * destination locations.</p>
 */
public class TeleportationTube extends Ground implements Tubeable {

    /** Duration (in turns) of the fire spawned at the arrival surroundings. */
    private static final int ARRIVAL_FIRE_TURNS = 2;

    /** Probability of a malfunction (teleport to random location instead). */
    private static final double MALFUNCTION_CHANCE = 0.5;

    /** Pre-configured destinations: each pair holds (location, label). */
    private final List<Location> destinations = new ArrayList<>();

    /** Human-readable labels corresponding to each destination. */
    private final List<String> destinationLabels = new ArrayList<>();

    private final Random random = new Random();

    /** Constructor for TeleportationTube. */
    public TeleportationTube() {
        super('Φ', "Teleportation Tube");
    }

    /**
     * Registers a destination location and its menu label.
     *
     * @param destination the target location (may be on a different map)
     * @param label       description shown in the menu
     */
    public void addDestination(Location destination, String label) {
        destinations.add(destination);
        destinationLabels.add(label);
    }

    /**
     * Returns one {@link TeleportAction} per registered destination.
     * The action's {@link TeleportEffect} handles malfunction and fire spawning.
     *
     * @param actor     the adjacent actor
     * @param location  this tube's location (used as the source)
     * @param direction direction from the actor to the tube
     * @return list of teleport actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty()) {
            // Actor is standing on the tube
            for (int i = 0; i < destinations.size(); i++) {
                Location dest = destinations.get(i);
                String label = "uses teleportation tube → " + destinationLabels.get(i);
                TeleportEffect effect = buildEffect(dest);
                actions.add(new TeleportAction(dest, label, effect));
            }
        }
        return actions;
    }

    /**
     * Builds the {@link TeleportEffect} for a given destination.
     *
     * <p>The effect: (1) 50% chance to reroute arrival to a random passable tile
     * in the destination map; (2) set all tiles adjacent to the actual arrival
     * location on fire for 2 turns.</p>
     *
     * @param intendedDest the originally intended destination
     * @return a composed {@link TeleportEffect}
     */
    private TeleportEffect buildEffect(Location intendedDest) {
        return (actor, destination, source) -> {
            // Determine actual arrival location (may differ due to malfunction)
            Location actualArrival = destination;
            if (random.nextDouble() < MALFUNCTION_CHANCE) {
                Location randomDest = randomPassableLocation(intendedDest.map(), actor);
                if (randomDest != null && randomDest != destination) {
                    // Re-move actor to the random location
                    intendedDest.map().moveActor(actor, randomDest);
                    actualArrival = randomDest;
                    System.out.println("TUBE MALFUNCTION! " + actor
                            + " was rerouted to a random location!");
                }
            }
            // Spawn fire on all adjacent tiles of actual arrival
            spawnArrivalFire(actualArrival);
        };
    }

    /**
     * Sets all adjacent tiles of the arrival location on fire for 2 turns.
     *
     * @param arrival the location where the actor actually arrived
     */
    private void spawnArrivalFire(Location arrival) {
        for (Exit exit : arrival.getExits()) {
            Location adj = exit.getDestination();
            adj.setGround(new Fire(adj.getGround(), ARRIVAL_FIRE_TURNS));
        }
    }

    /**
     * Finds a random passable location within the given map.
     * Returns {@code null} if the entire map is occupied.
     *
     * @param map   the map to search
     * @param actor the actor that needs to enter the tile
     * @return a passable {@link Location}, or {@code null}
     */
    private Location randomPassableLocation(GameMap map, Actor actor) {
        List<Location> passable = new ArrayList<>();
        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location loc = map.at(x, y);
                if (loc.canActorEnter(actor)) {
                    passable.add(loc);
                }
            }
        }
        if (passable.isEmpty()) {
            return null;
        }
        return passable.get(random.nextInt(passable.size()));
    }
}