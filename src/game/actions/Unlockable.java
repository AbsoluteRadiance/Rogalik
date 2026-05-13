package game.actions;

/**
 * An interface to be utilised by unlockable objects and/or locations.
 * Contains the unlock and lock method for implementing classes.
 */
public interface Unlockable {

    /**
     * The primary method for unlocking, the effects of which are to be determined
     * by implementing classes.
     */
    void unlock();

    /**
     * A boolean method to be implemented by unlockable objects,
     * typically returning whether the Alarm system is active
     *
     * @return the state of the lockdown system
     */
    boolean isOnLockdown();

    /**
     * A method to activate the lockdown, called when the alarm
     * system activates
     */
    void activateLockdown();
}
