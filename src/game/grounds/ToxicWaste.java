package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

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
 * <p>Any actor standing on a Toxic Waste tile takes 1 point of damage per turn.
 * The tile is passable — all actors can enter — but remaining on it is dangerous.</p>
 *
 * @author REQ2
 */
public class ToxicWaste extends Ground {

    /** Damage dealt to any actor standing on this tile each turn. */
    private static final int DAMAGE_PER_TURN = 1;

    /**
     * Constructor for ToxicWaste.
     * Uses the {@code ≈} character for display.
     */
    public ToxicWaste() {
        super('≈', "Toxic Waste");
    }

    /**
     * Called once per turn. Deals {@value #DAMAGE_PER_TURN} point of damage to
     * any actor currently standing on this tile.
     *
     * @param location the location of this ground tile
     */
    @Override
    public void tick(Location location) {
        Actor actor = location.getActor();
        if (actor != null) {
            actor.hurt(DAMAGE_PER_TURN);
        }
    }

    /**
     * All actors can enter toxic waste tiles.
     * Entering is allowed but stepping on it each turn incurs damage.
     *
     * @param actor the actor attempting to enter
     * @return {@code true} always; toxic waste is passable by any actor
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}