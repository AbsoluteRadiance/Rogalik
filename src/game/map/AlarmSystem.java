package game.map;

/**
 * A static utility class that manages the global alarm state of the Moon's facility.
 * Once activated, it currently cannot be deactivated and affects all creatures
 * and doors in the facility. Creatures are instructed to switch from wandering to chasing,
 * and doors cannot be unlocked while the alarm is active.
 */
public abstract class AlarmSystem {

    /** A boolean returning whether the alarm system has been activated or not. */
    private static boolean alarmActive = false;

    /**
     * Activates the facility alarm. Once activated, cannot be deactivated.
     */
    public static void activate() {
        alarmActive = true;
    }

    /**
     * A getter for whether the facility alarm is currently active.
     *
     * @return A boolean; true if the alarm is active, false if not
     */
    public static boolean isActive() {
        return alarmActive;
    }
}