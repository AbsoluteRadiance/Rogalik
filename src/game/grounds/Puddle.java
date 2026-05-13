package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.status.DamageOverTimeStatus;
import game.actions.DrinkAction;
import game.actions.Drinkable;
import game.enums.WorkerAbility;

/**
 * A small, stationary body of mysterious liquid on the ground.
 * In a standard video game, this would just be water. On a deprecated moon
 * in the Eclipse Nebula, it could be anything from spilled engine coolant to
 * highly corrosive alien saliva. Step in it at your own risk.
 */
public class Puddle extends Ground implements Drinkable {
    private static final int POISON_TURNS = 3;
    private static final int POISON_DAMAGE = 1;
    private static final int HEAL_AMOUNT = 1;

    /** Constructor for Puddle. */
    public Puddle() {
        super('~', "Puddle");
    }

    /**
     * Applies the effects of drinking from the puddle to the actor.
     * If a worker has the CAN_STERILISE ability,
     * they are healed for 1 health. Otherwise, poison is applied.
     *
     * @param actor the actor who is drinking
     */
    @Override
    public void drink(Actor actor) {
        if (actor.hasAbility(WorkerAbility.CAN_STERILISE)) {
            actor.heal(HEAL_AMOUNT);
        } else {
            actor.addStatus(new DamageOverTimeStatus(POISON_TURNS, POISON_DAMAGE));
        }
    }

    /**
     * Returns a list of allowable actions for an actor at a puddle.
     * Adds a DrinkAction only when the actor is standing directly in a puddle.
     *
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty()) {
            actions.add(new DrinkAction(this));
        }
        return actions;
    }
}
