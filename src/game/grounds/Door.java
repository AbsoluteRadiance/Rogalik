package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.map.AlarmSystem;
import game.actions.Unlockable;

/**
 * Its primary purpose in the universe is to halt the progress of underpaid
 * {@code ContractedWorker}s until they can produce the correct rectangular
 * piece of plastic.
 */
public class Door extends Ground implements Unlockable {

    /** Initialises the door in a locked state. */
    private boolean isUnlocked = false;
    private boolean isOnLockdown = false;

    /**
     * Constructor for Door.
     */
    public Door() {
        super('=', "Door");
    }

    /**
     * Unlocks the door, allowing actors to enter.
     * Called when a worker is holding an AccessCard.
     */
    @Override
    public void unlock() {
        isUnlocked = true;
    }

    /**
     * Returns whether the Alarm is active and doors are on lockdown.
     *
     * @return the state of the lockdown system, true if active
     */
    public boolean isOnLockdown() {
        return isOnLockdown;
    }

    /**
     * Locks the door when the alarm activates
     * or is currently still active.
     */
    public void activateLockdown() {
        if (AlarmSystem.isActive()) {
            isUnlocked = false;
            isOnLockdown = true;
        }
    }

    /**
     * Called every tick to check if the alarm system is active
     * If it is, activate lockdown for doors.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        if (AlarmSystem.isActive()) {
            activateLockdown();
        }
    }

    /**
     * If the door is unlocked, any actor can step into the door.
     *
     * @param actor the Actor to check
     * @return true if the door is unlocked, false otherwise.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked;
    }
}
