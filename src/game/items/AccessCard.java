package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.UnlockAction;
import game.actions.Unlockable;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

/**
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot,
 *
 * @author Adrian Kristanto
 */
public class AccessCard extends Item {

    /**
     * Constructor for AccessCard.
     * Initialises with 1 weight and makes the card portable.
     * Enables the ability to unlock doors.
     */
    public AccessCard() {
        super("Access Card", '▤');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
        this.enableAbility(WorkerAbility.CAN_UNLOCK_DOOR);
    }

    /**
     * Returns a list of allowable actions for the carrier.
     * Adds an UnlockAction for each adjacent unlockable object.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return the list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location location = map.locationOf(owner);
        for (Exit exit : location.getExits()) {
            Unlockable unlockable = exit.getDestination().getGroundAs(Unlockable.class);
            if (unlockable != null && !unlockable.isOnLockdown()) {
                actions.add(new UnlockAction(unlockable));
            }
        }
        return actions;
    }
}
