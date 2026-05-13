package game.actions;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An interface to be utilised by consumable objects.
 * Contains the consume method for implementing classes.
 */
public interface Consumable {

    /**
     * The primary method for consumption, the effects of which
     * are determined by the implementing class.
     *
     * @param actor the actor who is consuming
     */
    void consume(Actor actor);

    /**
     * A boolean used to tell if an item is empty; depleted.
     * Namely for implementing consumables such as the Flask
     * or consumables with numbered uses.
     *
     * @return A boolean; true if the item is empty and unusable, false otherwise.
     */
    boolean isDepleted();
}