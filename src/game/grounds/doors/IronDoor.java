package game.grounds.doors;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.LockableDoor;
import game.actions.UnlockDoorAction;
import game.enums.DoorLevel;
import game.grounds.Fire;
import game.grounds.Floor;

/**
 * A heavy iron security door ({@code N}) requiring Access Card Level 2 or higher.
 *
 * <p>When unlocked, the rusted mechanism overheats, instantly setting all
 * adjacent floor tiles on fire. This fire lasts for 2 turns and applies
 * the standard burn effect to any actor walking through it.</p>
 */
public class IronDoor extends Ground implements LockableDoor {

    /** Duration (in turns) of the fire spawned when this door is unlocked. */
    public static final int FIRE_TURNS = 2;

    /** Whether this door is currently unlocked. */
    private boolean unlocked = false;

    /** Constructor for IronDoor. */
    public IronDoor() {
        super('N', "Iron Door");
    }

    /**
     * Returns the clearance level required to open this door.
     *
     * @return {@link DoorLevel#LEVEL_2}
     */
    @Override
    public DoorLevel getRequiredLevel() {
        return DoorLevel.LEVEL_2;
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

    /**
     * Spawns fire on all adjacent floor tiles when unlocked.
     * Called by {@link UnlockDoorAction} immediately after the door is opened.
     *
     * @param doorLocation the location of this door on the map
     */
    public void triggerOverheat(Location doorLocation) {
        for (Exit exit : doorLocation.getExits()) {
            Location dest = exit.getDestination();
            dest.setGround(new Fire(dest.getGround(), FIRE_TURNS));
        }
    }
}