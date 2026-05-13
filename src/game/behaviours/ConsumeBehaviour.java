package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.Consumable;
import game.actions.ConsumeAction;

import java.util.List;

/**
 * A behaviour that consumes the first Consumable item found at the
 * actor's current location. Used by Slimes to consume items directly
 * off the ground (without adding them to an inventory first).
 */
public class ConsumeBehaviour implements Behaviour<Actor, Action> {

    /**
     * Operates the consume behaviour, returning a ConsumeAction for the
     * first consumable item at the specified actor's current location.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return a ConsumeAction if a consumable is found, null if not
     */
    @Override
    public Action operate(Actor actor, Location location) {
        List<Consumable> consumables = location.getItemsAs(Consumable.class);
        if (!consumables.isEmpty()) {
            return new ConsumeAction(consumables.get(0));
        }
        return null;
    }
}