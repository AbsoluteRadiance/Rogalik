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
 * <p>The action uses {@link GameMap#moveActor} to relocate the actor; the
 * destination map is retrieved directly from the {@link Location} object so
 * cross-map teleportation is supported transparently.</p>
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
     * @param actor the actor performing the teleport
     * @param map   the map the actor is currently on (may differ from destination map)
     * @return a string describing what happened
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location source = map.locationOf(actor);
        GameMap destMap = destination.map();

        // Move actor — works for both same-map and cross-map teleportation
        map.moveActor(actor, destination);

        // Apply post-arrival side effect (may involve source location for corruption etc.)
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