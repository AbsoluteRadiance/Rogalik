package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * An action that allows an actor to attack another actor.
 * If no weapon is provided, falls back to the attacker's intrinsic weapon.
 *
 * <p>Adapted from {@code AttackAction} in
 * {@code edu.monash.fit2099.demo.forest}.
 *
 * @author Adrian Kristanto
 */
public class AttackAction extends Action {

    /**
     * The target of the attack.
     */
    private final Actor target;

    /**
     * The direction of the target from the attacker.
     */
    private final String direction;

    /**
     * The weapon used for the attack. If null, the actor's intrinsic weapon is used.
     */
    private Weapon weapon;

    /**
     * Constructor with an explicit weapon.
     *
     * @param target    the actor to attack
     * @param direction the direction of the target
     * @param weapon    the weapon to use
     */
    public AttackAction(Actor target, String direction, Weapon weapon) {
        this.target = target;
        this.direction = direction;
        this.weapon = weapon;
    }

    /**
     * Constructor that uses the attacker's intrinsic weapon.
     *
     * @param target    the actor to attack
     * @param direction the direction of the target
     */
    public AttackAction(Actor target, String direction) {
        this.target = target;
        this.direction = direction;
    }

    /**
     * Executes the attack. If the target is rendered unconscious, calls
     * {@link Actor#unconscious(Actor, GameMap)}.
     *
     * @param actor the actor performing the attack
     * @param map   the map the actor is on
     * @return description of the attack result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (weapon == null) {
            weapon = actor.getIntrinsicWeapon();
        }
        String result = weapon.attack(actor, target, map);
        if (!target.isConscious()) {
            result += "\n" + target.unconscious(actor, map);
        }
        return result;
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " attacks " + target + " at " + direction
                + " with " + (weapon != null ? weapon : "intrinsic weapon");
    }
}