package game;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.items.PickUpAction;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 * Each worker starts with a Flask already in their weight-limited inventory (50 units).
 */
public class ContractedWorker extends Actor {

    public ContractedWorker(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints, new WeightLimitedInventory());
        // Every worker starts with a Flask — the corporation's idea of a generous benefit package
        Flask flask = new Flask();
        this.getInventory().add(flask);
    }

    /**
     * On each turn:
     * - Removes the worker if unconscious
     * - Adds pickup actions for items on the ground
     * - Adds door-unlocking action if carrying an AccessCard and a door is adjacent
     * - Adds flask consumption action if carrying a Flask with remaining uses
     * - Handles multi-turn actions
     * - Displays the menu
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the action chosen this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        addPickUpActions(actions, map);
        addDoorUnlockAction(actions, map);
        addFlaskAction(actions);

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null) {
            return lastAction.getNextAction();
        }

        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Adds pick-up actions for all items at the worker's current location.
     */
    private void addPickUpActions(ActionList actions, GameMap map) {
        for (Item item : map.locationOf(this).getItems()) {
            actions.add(new PickUpAction(item));
        }
    }

    /**
     * If the worker carries an AccessCard, adds an UnlockDoorAction when a door is adjacent.
     */
    private void addDoorUnlockAction(ActionList actions, GameMap map) {
        boolean hasAccessCard = false;
        for (Item item : this.getInventory().getItems()) {
            if (item instanceof AccessCard) {
                hasAccessCard = true;
                break;
            }
        }

        if (hasAccessCard) {
            Location location = map.locationOf(this);
            for (Exit exit : location.getExits()) {
                Ground ground = exit.getDestination().getGround();
                if (ground.getDisplayChar() == '=') {
                    actions.add(new UnlockDoorAction());
                    break; // Only one unlock action needed per turn
                }
            }
        }
    }

    /**
     * Adds a ConsumeFlaskAction if the worker's Flask is not depleted.
     * The Flask always stays in inventory, but we only show the action when it has uses left.
     */
    private void addFlaskAction(ActionList actions) {
        for (Item item : this.getInventory().getItems()) {
            if (item instanceof Flask flask) {
                if (!flask.isDepleted()) {
                    actions.add(new ConsumeFlaskAction(flask));
                }
                break; // There is only one Flask per worker
            }
        }
    }
}