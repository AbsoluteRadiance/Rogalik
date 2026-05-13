package game.actions;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An interface to be utilised by drinkable objects and/or locations.
 * Contains the drink method for implementing classes.
 */
public interface Drinkable {

    /**
     * The primary method for drinking, the effects of which are to be determined
     * by implementing classes.
     *
     * @param actor the actor who is drinking
     */
    void drink(Actor actor);
}