package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An action that teleports an actor directly to a pre-chosen destination location,
 * then fires an optional {@link TeleportEffect} side effect.
 *
 * <p>All three teleportation devices in the game ({@link game.grounds.TeleportationTube},
 * {@link game.items.AlienCube}, and {@link game.grounds.MagicCircle}) produce
 * instances of this action with different injected effects, avoiding any duplication
 * of movement logic.</p>
 *
 * <p>If the actor is already at the destination (source == destination), the
 * {@code moveActor} call is skipped to avoid an {@link IllegalArgumentException}
 * from the engine's one-actor-per-location rule; the {@link TeleportEffect} is
 * still fired so devices that rely on it (e.g. side-effect tests) behave correctly.</p>
 */
public class TeleportAction extends Action {

    /** The location to teleport the actor to. */
    private final Location destination;

    /** Human-readable label shown in the console menu. */
    private final String label;

    /**
     * Side effect to apply immediately after the actor arrives.
     * May be a no-op lambda if no effect is needed.
     */
    private final TeleportEffect effect;

    /**
     * Constructs a teleport action with a post-arrival side effect.
     *
     * @param destination the target location
     * @param label       the text shown in the menu (e.g. "teleport to Sector 4")
     * @param effect      side effect applied after arrival; use {@code (a,d,s)->{ }}
     *                    for no effect
     */
    public TeleportAction(Location destination, String label, TeleportEffect effect) {
        this.destination = destination;
        this.label = label;
        this.effect = effect;
    }

    /**
     * Moves the actor to the destination on the destination's map,
     * then applies the {@link TeleportEffect}.
     *
     * <p>If the actor is already at the destination, the move is skipped.
     * The engine's {@code moveActor} would throw {@link IllegalArgumentException}
     * in that case because the actor is already occupying the target location.</p>
     *
     * @param actor the actor performing the teleport
     * @param map   the map the actor is currently on (may differ from destination map)
     * @return a string describing what happened
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location source = map.locationOf(actor);

        // Only move if the actor is not already at the destination.
        // Skipping avoids the IllegalArgumentException from the one-actor-per-location rule.
        if (!source.equals(destination)) {
            destination.map().moveActor(actor, destination);
        }

        // Always fire the effect — even for degenerate same-location teleports
        // so that tests and devices that rely on the effect callback work correctly.
        effect.apply(actor, destination, source);

        return actor + " teleports to " + destination + ".";
    }

    /**
     * Returns the menu description for this teleport action.
     *
     * @param actor the actor performing the action
     * @return the menu label
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " " + label;
    }
}