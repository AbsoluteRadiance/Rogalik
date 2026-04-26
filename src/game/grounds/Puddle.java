package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.DrinkPuddleAction;

/**
 * A mysterious puddle of liquid found on the moon's surface.
 * Workers can attempt to drink directly from it — outcome depends
 * on whether they carry a SterilisationBox.
 */
public class Puddle extends Ground {

    public Puddle() {
        super('~', "Puddle");
    }

    /**
     * Allows a worker standing on this puddle to drink from it.
     * The empty string direction indicates the actor is on this tile, not adjacent.
     *
     * @param actor     the actor acting
     * @param location  the current location
     * @param direction empty string when actor is standing on this ground
     * @return action list containing DrinkPuddleAction
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty()) {
            actions.add(new DrinkPuddleAction());
        }
        return actions;
    }
}