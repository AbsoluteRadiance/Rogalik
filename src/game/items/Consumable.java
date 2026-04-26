package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for items that can be consumed by an actor.
 * The consume logic (healing, poisoning, stat changes) is delegated
 * to the implementing class rather than the action, keeping each item
 * responsible for its own effect (Single Responsibility Principle).
 */
public interface Consumable {

    /**
     * Consume this item, applying its effect to the actor.
     *
     * @param actor the actor consuming the item
     * @param map   the current game map
     * @return a description of what happened
     */
    String consume(Actor actor, GameMap map);
}