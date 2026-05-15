package game.actions;

/**
 * Interface for doors that re-seal under an active alarm.
 *
 * <p>This narrow interface exists so that {@link UnlockDoorAction} can trigger
 * the lockdown check via {@link edu.monash.fit2099.engine.positions.Location#getGroundAs}
 * without downcasting to the concrete {@link game.grounds.doors.AluminiumDoor} class.
 * Only {@link game.grounds.doors.AluminiumDoor} implements this interface,
 * reflecting the fact that only aluminium doors re-seal under alarm. </p>
 */
public interface AlarmLocked {

    /**
     * Activates lockdown on this door if the global alarm is currently active,
     * preventing the door from being opened.
     */
    void activateLockdown();
}