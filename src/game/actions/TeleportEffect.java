package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A functional interface representing a side effect that fires immediately
 * after a teleportation completes.
 *
 * <p>Each teleportation device injects its own implementation when constructing
 * a {@link TeleportAction}, keeping all device-specific logic out of the
 * action itself (Strategy pattern, Open/Closed Principle).</p>
 *
 * <p>Examples of effects: spawning fire around the destination, corrupting
 * the source location with toxic waste, or spawning a flask nearby.</p>
 */
@FunctionalInterface
public interface TeleportEffect {

    /**
     * Applies the post-teleport side effect.
     *
     * @param actor       the actor who teleported
     * @param destination the location the actor arrived at
     * @param source      the location the actor departed from
     */
    void apply(Actor actor, Location destination, Location source);
}