package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * The natural weapon of an {@link game.actors.Undead} — a clumsy, rotting fist.
 * Deals 1 damage with a 10% chance to hit.
 */
public class UndeadFist extends IntrinsicWeapon {

    /**
     * Constructor.
     */
    public UndeadFist() {
        super(1, "claws", 10, "undead fist");
    }
}