package game.actions;

import game.enums.DoorLevel;

/**
 * Interface for lockable ground tiles (doors) in the tiered security system.
 *
 * <p>Replaces the old {@link Unlockable} single-method interface with a richer
 * contract that exposes the door's required clearance level, allowing
 * {@link UnlockDoorAction} to validate access without any {@code instanceof}
 * checks.</p>
 */
public interface LockableDoor {

    /**
     * Returns the minimum clearance level required to unlock this door.
     *
     * @return the {@link DoorLevel} required
     */
    DoorLevel getRequiredLevel();

    /**
     * Unlocks the door so that actors may pass through.
     */
    void unlock();

    /**
     * Returns whether the door is currently unlocked.
     *
     * @return {@code true} if unlocked
     */
    boolean isUnlocked();
}