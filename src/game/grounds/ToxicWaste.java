package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.status.DamageOverTimeStatus;

/**
 * Represents a toxic waste tile on the ground (represented by {@code ≈}).
 *
 * <p>Toxic Waste is created in two ways:
 * <ul>
 *   <li>Naturally present in the 20-overflow map as flooded terrain.</li>
 *   <li>Permanently spawned by an Alien Cube when it corrupts adjacent ground tiles
 *       upon use.</li>
 * </ul>
 *
 * <p>Any actor standing on a Toxic Waste tile receives a
 * {@link DamageOverTimeStatus} of 1 turn and 1 damage per turn, applied once
 * per call to {@link #tick}. Using a status effect rather than a direct
 * {@code hurt} call ensures that if {@code tick} is called multiple times in
 * the same game turn (e.g., once directly and once via
 * {@link Location#tick()}), the actor only takes damage once — the DOT is
 * applied on the first call and consumed when
 * {@link edu.monash.fit2099.engine.GameEntity#tickStatuses} runs.</p>
 *
 * <p>The tile is passable — all actors can enter — but remaining on it is
 * dangerous.</p>
 */
public class ToxicWaste extends Ground {

    /** Damage dealt to any actor standing on this tile each turn. */
    private static final int DAMAGE_PER_TURN = 1;

    /** Duration of the damage-over-time effect applied each tick. */
    private static final int DOT_TURNS = 1;

    /**
     * Constructor for ToxicWaste.
     * Uses the {@code ≈} character for display.
     */
    public ToxicWaste() {
        super('≈', "Toxic Waste");
    }

    /**
     * Called once per turn. Applies a {@link DamageOverTimeStatus} (1 turn,
     * {@value #DAMAGE_PER_TURN} damage) to any actor currently standing on this
     * tile.
     *
     * <p>Using a DOT status means the damage is dealt when
     * {@link edu.monash.fit2099.engine.GameEntity#tickStatuses} runs on the
     * actor, not immediately. This prevents double-damage when {@code tick} is
     * invoked more than once before the actor's status tick fires.</p>
     *
     * @param location the location of this ground tile
     */
    @Override
    public void tick(Location location) {
        Actor actor = location.getActor();
        if (actor != null) {
            actor.addStatus(new DamageOverTimeStatus(DOT_TURNS, DAMAGE_PER_TURN));
        }
    }

    /**
     * All actors can enter toxic waste tiles.
     * Entering is allowed, but stepping on it each turn incurs damage.
     *
     * @param actor the actor attempting to enter
     * @return {@code true} always; toxic waste is passable by any actor
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}