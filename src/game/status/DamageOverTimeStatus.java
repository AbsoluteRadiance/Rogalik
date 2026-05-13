package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents a damage over time status parent effect that can be applied to any entity.
 * Namely this would be the workers/player.
 * Across a set amount of turns, the actor a DOT Status is applied to
 * takes a set amount of damage per turn.
 */
public class DamageOverTimeStatus implements Status {
    private int turnsRemaining;
    private final int damagePerTurn;

    /**
     * Constructor for DamageOverTimeStatus
     *
     * @param turnsRemaining the number of turns this status will remain active
     * @param damagePerTurn the amount of damage taken by the entity per turn
     */
    public DamageOverTimeStatus(int turnsRemaining, int damagePerTurn) {
        this.turnsRemaining = turnsRemaining;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * Called once per turn while this status is active.
     * Deals damage to the entity then decrements the turn counter.
     * The status is automatically removed by the engine when
     * isStatusActive() returns false.
     *
     * @param entity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity entity, Location location) {
        if (entity instanceof Actor) {
            ((Actor) entity).hurt(damagePerTurn);
        }
        turnsRemaining -= 1;
    }

    /**
     * Returns whether the damage over time status is still active.
     *
     * @return a boolean; true if turns remaining is greater than 0, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return turnsRemaining > 0;
    }
}