package game.actors;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;

/**
 * A class representing the Undead creature.
 */
public class Undead extends Creature implements Hostile {

    /**
     * Constructor for Undead
     * Adds relevant behaviours for actions the undead can take
     * WanderBehaviour is for movement,
     * AttackBehaviour is for when an Undead is near a player and can attack.
     */
    public Undead() {
        super("Undead", 'Ѫ', 15);
        behaviours.put(1, new AttackBehaviour());
        behaviours.put(2, new WanderBehaviour());
    }
}