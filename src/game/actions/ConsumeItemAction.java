package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Consumable;

/**
 * A generic action for consuming an item from the actor's inventory.
 * Delegates the consume logic to the item itself via the Consumable interface.
 */
public class ConsumeItemAction extends Action {

    private final Consumable consumable;

    /**
     * Constructor.
     *
     * @param consumable the item to consume
     */
    public ConsumeItemAction(Consumable consumable) {
        this.consumable = consumable;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        return consumable.consume(actor, map);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " consumes " + consumable;
    }
}