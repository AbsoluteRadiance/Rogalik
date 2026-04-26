package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Door;

/**
 * The bureaucratic process of asking a piece of the environment for permission to pass.
 */
public class UnlockDoorAction extends Action {

    /**
     * When executed, it will search for a nearby door and unlock it.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the description of the result of the action of opening a door
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location currentLocation = map.locationOf(actor);
        for (Exit exit : currentLocation.getExits()) {
            Location surroundingLocation = exit.getDestination();
            Ground surroundingGround = surroundingLocation.getGround();
            if (surroundingGround.getDisplayChar() == '=') {
                Door door = (Door) surroundingGround;
                door.unlock();
                return actor + " unlocked " + door + " at " + surroundingLocation;
            }
        }
        return "There is no door to unlock.";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks door";
    }
}
