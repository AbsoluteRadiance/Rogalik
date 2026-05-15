package game.actions;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Marker interface for ground tiles that behave as magic circles.
 *
 * <p>Using an interface here satisfies the engine's {@code asCapability} contract,
 * which requires the capability type to be an interface or abstract class.
 * This avoids passing the concrete {@link game.grounds.MagicCircle} class to
 * {@link edu.monash.fit2099.engine.positions.Location#getGroundAs}, which would
 * throw an {@link IllegalArgumentException}.</p>
 */
public interface Teleportable {

    /**
     * Returns the location of this magic circle on the map.
     * Used by {@link game.grounds.MagicCircle#findOtherCircles} to enumerate peer circles.
     *
     * @return the {@link Location} of this circle
     */
    Location getLocation();
}