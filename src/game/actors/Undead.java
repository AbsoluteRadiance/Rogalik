package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.inventories.BasicInventory;
import game.weapons.UndeadFist;

import java.util.Map;
import java.util.TreeMap;

/**
 * A reanimated corpse of a former worker whose contract was
 * unceremoniously terminated by being unalive.
 *
 * <p>Hostile to workers only — ignores other creatures.
 * Attacks with an {@link UndeadFist} (1 damage, 10% hit chance).
 * Wanders when no worker is in range.
 */
public class Undead extends Actor {

    /**
     * Priority-ordered map of behaviours.
     * Uses {@link TreeMap} to guarantee execution order.
     */
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Constructor.
     */
    public Undead() {
        super("Undead", 'Ѫ', 15, new BasicInventory());
        this.setIntrinsicWeapon(new UndeadFist());
        this.behaviours.put(0, new AttackBehaviour());
        this.behaviours.put(1, new WanderBehaviour());
    }

    /**
     * Each turn, attempts behaviours in priority order.
     * Returns {@link DoNothingAction} if no behaviour produces an action.
     *
     * @param actions    collection of possible actions
     * @param lastAction the action taken last turn
     * @param map        the current game map
     * @param display    the display object
     * @return the action to perform this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, map.locationOf(this));
            if (action != null) {
                return action;
            }
        }
        return new DoNothingAction();
    }
}