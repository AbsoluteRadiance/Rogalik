package game.grounds.doors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.LockableDoor;
import game.actions.UnlockDoorAction;
import game.enums.DoorLevel;
import game.map.AlarmSystem;

/**
 * An aluminium security door ({@code =}) requiring Access Card Level 1 or higher.
 *
 * <p>When unlocked, faulty wiring delivers an electrical shock to the worker,
 * dealing exactly 2 points of damage. The door seals again if the alarm is active.</p>
 */
public class AluminiumDoor extends Ground implements LockableDoor {

    /** Damage dealt to the worker from the electrical short-circuit on unlock. */
    private static final int SHOCK_DAMAGE = 2;

    /** Whether this door is currently unlocked. */
    private boolean unlocked = false;

    /** Whether this door is currently on lockdown due to the alarm. */
    private boolean onLockdown = false;

    /** Constructor for AluminiumDoor. */
    public AluminiumDoor() {
        super('=', "Aluminium Door");
    }

    /**
     * Returns the clearance level required to open this door.
     *
     * @return {@link DoorLevel#LEVEL_1}
     */
    @Override
    public DoorLevel getRequiredLevel() {
        return DoorLevel.LEVEL_1;
    }

    /**
     * Unlocks this door, allowing actors to pass through.
     * Called by {@link UnlockDoorAction} after clearance is verified.
     */
    @Override
    public void unlock() {
        unlocked = true;
    }

    /**
     * Returns whether this door is currently unlocked.
     *
     * @return {@code true} if unlocked and not on lockdown
     */
    @Override
    public boolean isUnlocked() {
        return unlocked && !onLockdown;
    }

    /**
     * Activates lockdown if the global alarm is active, re-sealing the door.
     */
    public void activateLockdown() {
        if (AlarmSystem.isActive()) {
            unlocked = false;
            onLockdown = true;
        }
    }

    /**
     * Actors may only enter when the door is unlocked and not on lockdown.
     *
     * @param actor the actor attempting to enter
     * @return {@code true} if the door is open
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked();
    }

    /**
     * Returns the shock damage constant so {@link UnlockDoorAction} can apply it.
     *
     * @return damage dealt on unlock
     */
    public int getShockDamage() {
        return SHOCK_DAMAGE;
    }
}