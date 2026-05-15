package game.actions;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Interface for teleportation tube grounds.
 * Exists solely so Location.getGroundAs() can look up a tube
 * without passing a concrete class, which the engine forbids.
 */
public interface Tubeable {
    void addDestination(Location destination, String label);
}