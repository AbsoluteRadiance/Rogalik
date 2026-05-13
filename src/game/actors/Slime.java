package game.actors;

import game.behaviours.ConsumeBehaviour;
import game.behaviours.WanderBehaviour;

/**
 * A class representing the Slime creature.
 */
public class Slime extends Creature {

    /**
     * Constructor for Slime
     * Adds relevant behaviours for actions the slime can take
     * WanderBehaviour is for movement,
     * ConsumeBehaviour is for when the slime finds an item on the ground.
     */
    public Slime() {
        super("Slime", '⍾', 25);
        behaviours.add(new WanderBehaviour());
        behaviours.add(new ConsumeBehaviour());
    }
}