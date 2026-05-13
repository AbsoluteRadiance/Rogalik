package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * The bureaucratic process of asking a piece of the environment for permission to pass.
 */
public class UnlockAction extends Action {
    private final Unlockable unlockable;

    public UnlockAction(Unlockable unlockable) {
        this.unlockable = unlockable;
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
        if (unlockable.isOnLockdown()) {
            return "The alarm is active! It has been sealed.";
        }
        unlockable.unlock();
        return actor + " unlocked " + unlockable;
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
