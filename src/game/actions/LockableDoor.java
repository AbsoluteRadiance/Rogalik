package game.actions;

import game.enums.DoorLevel;

/**
 * Interface for lockable ground tiles (doors) in the tiered security system.
 *
 * <p>Exposes the door's required clearance level and its post-unlock side effect
 * via {@link #getUnlockEffect()}, allowing {@link UnlockDoorAction} to validate
 * access and apply effects without any {@code instanceof} checks or downcasting.</p>
 *
 * <p>All door-specific logic (shock, fire, heal) lives inside the concrete door
 * class, injected as an {@link UnlockEffect} lambda. This satisfies SRP, OCP,
 * and DIP — new door types only need to implement this interface.</p>
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

    /**
     * Returns the side effect to apply immediately after this door is unlocked.
     *
     * <p>Defaults to no effect so existing door implementations without a
     * custom effect continue to compile and run correctly (OCP).</p>
     *
     * @return an {@link UnlockEffect} describing what happens on unlock
     */
    default UnlockEffect getUnlockEffect() {
        return (actor, doorLocation) -> "";
    }
}