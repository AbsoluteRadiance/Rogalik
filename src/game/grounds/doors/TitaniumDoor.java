package game.grounds.doors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.LockableDoor;
import game.actions.UnlockDoorAction;
import game.enums.DoorLevel;

/**
 * A titanium security door ({@code M}) requiring Access Card Level 3.
 *
 * <p>When unlocked, a localised decontamination sequence is triggered,
 * immediately healing the worker for 5 health points.</p>
 */
public class TitaniumDoor extends Ground implements LockableDoor {

    /** Health points restored to the worker by the decontamination sequence. */
    public static final int HEAL_AMOUNT = 5;

    /** Whether this door is currently unlocked. */
    private boolean unlocked = false;

    /** Constructor for TitaniumDoor. */
    public TitaniumDoor() {
        super('M', "Titanium Door");
    }

    /**
     * Returns the clearance level required to open this door.
     *
     * @return {@link DoorLevel#LEVEL_3}
     */
    @Override
    public DoorLevel getRequiredLevel() {
        return DoorLevel.LEVEL_3;
    }

    /**
     * Unlocks this door so that actors may pass through.
     */
    @Override
    public void unlock() {
        unlocked = true;
    }

    /**
     * Returns whether this door is currently unlocked.
     *
     * @return {@code true} if unlocked
     */
    @Override
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Actors may only enter when the door is unlocked.
     *
     * @param actor the actor attempting to enter
     * @return {@code true} if the door is open
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return unlocked;
    }
}