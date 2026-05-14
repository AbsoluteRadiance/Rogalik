package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.status.DamageOverTimeStatus;

/**
 * Represents a fire tile on the ground, spawned by a Lantern leaking oil.
 * Fire persists for a set number of turns.
 * Any actor standing on the fire receives damage for a set number of turns.
 * Once expired, the fire will restore the original ground that was set aflame.
 */
public class Fire extends Ground {

    /** The amount of damage an actor per turn, once aflame */
    private static final int DAMAGE_PER_TURN = 1;

    /** How long the actor for takes damage once aflame */
    private static final int BURNING_TURNS = 5;

    private int turnsRemaining;
    private final Ground originalGround;

    /**
     * Constructor for Fire.
     * Stores the original ground so it can be restored when the fire expires.
     * @param originalGround
     */
    public Fire(Ground originalGround, int burnTime) {
        super('^', "Fire");
        this.originalGround = originalGround;
        this.turnsRemaining = burnTime;
    }

    /**
     * Called once per turn. If an actor is standing on this tile, apply a
     * DamageOverTime effect. At the same time,
     * decrement the fire's lifespan by one.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        Actor actor = location.getActor();
        if (actor != null) {
            actor.addStatus(new DamageOverTimeStatus(BURNING_TURNS, DAMAGE_PER_TURN));
        }
        turnsRemaining -= 1;
        if (turnsRemaining <= 0) {
            location.setGround(originalGround);
        }
    }
}
