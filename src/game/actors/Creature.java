package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.ChaseBehaviour;
import game.behaviours.WanderBehaviour;
import game.map.AlarmSystem;

import java.util.Map;
import java.util.TreeMap;

/**
 * An abstract parent class representing all non-player creatures in the game.
 * Creatures utilise a priority-based behaviour system.
 * When the alarm system is active, wandering behaviour is replaced by chase behaviour.
 */
public abstract class Creature extends Actor {

    /**
     * The list of behaviours this creature will attempt.
     */
    protected final TreeMap<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Constructor for Creature
     *
     * @param name the name of the creature
     * @param displayChar the character used to represent the creature on the map
     * @param hitPoints the starting health of the creature
     */
    public Creature(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints, new BasicInventory());
    }

    /**
     * Selects and returns an action for the creature to perform this turn.
     * Iterates through behaviours in priority order, returning the first
     * non-null action.
     * Also checks whether alarm is active, swapping wander behaviour
     * with chase behaviour if it is - notably before selection.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction the Action this Actor took last turn.
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return the action to perform this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (AlarmSystem.isActive() && this.asCapability(Hostile.class).isPresent()) {
            swapWanderWithChase();
        }
        Location location = map.locationOf(this);
        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, location);
            if (action != null) {
                return action;
            }
        }
        return new DoNothingAction();
    }

    /**
     * Replaces the standard wandering behaviour of creatures
     * with the chase behaviour.
     * This is only called when the alarm is triggered in the game.
     */
    protected void swapWanderWithChase() {
        for (Map.Entry<Integer, Behaviour<Actor, Action>> entry : behaviours.entrySet()) {
            if (entry.getValue() instanceof WanderBehaviour) {
                behaviours.put(entry.getKey(), new ChaseBehaviour());
                break;
            }
        }
    }
}