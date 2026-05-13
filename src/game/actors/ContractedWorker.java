package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 *
 * <p><b>REQ1</b>: Now implements {@link Wallet} to participate in the economy
 * at the {@link game.grounds.Supercomputer}. Credits are tracked internally
 * and capped at {@link Wallet#MAX_CREDITS}.</p>
 *
 * @author Ben (base), Shivam (REQ1 Wallet)
 */
public class ContractedWorker extends Actor implements Targetable, Wallet {

    /** Current credit balance. */
    private int credits = 0;

    /**
     * Constructs a contracted worker with zero credits.
     *
     * @param name        the worker's name
     * @param displayChar character on the map
     * @param hitPoints   starting hit points
     * @param inventory   this worker's personal inventory
     */
    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
    }

    /**
     * Selects and returns the action to perform this turn.
     *
     * @param actions    all allowable actions this turn
     * @param lastAction the action taken last turn
     * @param map        the current game map
     * @param display    the output display
     * @return the chosen action
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

    // ==================== Wallet ====================

    /**
     * Returns the current credit balance.
     *
     * @return credits held
     */
    @Override
    public int getCredits() {
        return credits;
    }

    /**
     * Adds credits, capped at {@link Wallet#MAX_CREDITS}.
     *
     * @param amount credits to add
     */
    @Override
    public void addCredits(int amount) {
        credits = Math.min(credits + amount, MAX_CREDITS);
    }

    /**
     * Deducts credits. Returns false if insufficient funds.
     *
     * @param amount credits to deduct
     * @return true if deducted successfully
     */
    @Override
    public boolean deductCredits(int amount) {
        if (credits >= amount) {
            credits -= amount;
            return true;
        }
        return false;
    }
}