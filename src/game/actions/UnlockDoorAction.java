package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Door;

/**
 * The bureaucratic process of asking a piece of the environment for permission to pass.
 */
public class UnlockDoorAction extends Action {
    private final Location doorLocation;

    public UnlockDoorAction(Location doorLocation) {
        this.doorLocation = doorLocation;
    }

    /**
     * When executed, it will search for a nearby door and unlock it.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the description of the result of the action of opening a door
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Door door = doorLocation.getGroundAs(Door.class);
        door.activateLockdown();
            if (door.isOnLockdown) {
                return "The alarm is active! Doors are sealed.";
            }
        Unlockable unlockable = doorLocation.getGroundAs(Unlockable.class);
        if (unlockable != null) {
            unlockable.unlock();
            return actor + " unlocked a door at " + doorLocation;
        }
        return "There is no unlockable door at " + doorLocation;
    }

    /**
     * Descriptor for the menu console,
     * to inform players on what the action entails.
     *
     * @param actor The actor performing the action
     * @return a String confirming the action taken that turn
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks door";
    }
}
