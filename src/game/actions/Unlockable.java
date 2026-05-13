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
}
