package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ConsumeAction;
import game.actions.UnlockAction;
import game.grounds.Door;
import game.items.Flask;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 */
public class ContractedWorker extends  Actor implements Targetable {
    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
    }

    /**
     * The playTurn method checks whether the current actor is unconscious due to environmental hazards.
     * It will generate a pick up action for each item found on the ground so that the player can pick up items
     * from the ground.
     * Next, it will check if the player is carrying an access card. If so, they can open doors.
     * If the flask is available in the inventory, the player will be able to consume its content.
     * Additionally, ut will also handle multi-turn actions by getting the subsequent action returned by the previous action.
     * Finally, it adds all possible actions that the actor can perform in the current turn and show it on the
     * console menu for the player to choose.
     *
     * @see Door
     * @see UnlockAction
     * @see Flask
     * @see ConsumeAction
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return the action that is chosen in the current turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }
}
