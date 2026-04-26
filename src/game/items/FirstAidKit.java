package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.utils.ItemStatistics;
import game.actions.UseFirstAidKitAction;

/**
 * A super useful medical item weighing 25 units.
 * When used, it permanently increases the worker's maximum health by 1 point
 * and fully restores their health.
 * Has a 20-turn cooldown that only progresses while the kit is carried by a worker.
 * If left on the floor, the cooldown timer is paused.
 */
public class FirstAidKit extends Item {

    private static final int WEIGHT = 25;
    private static final int COOLDOWN_TURNS = 20;

    private int cooldownRemaining;

    public FirstAidKit() {
        super("First Aid Kit", '+');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.cooldownRemaining = 0; // ready to use at the start
    }

    /**
     * Ticks the cooldown only while the kit is being carried by an actor.
     * This is called by the engine each turn when the item is in an inventory.
     *
     * @param currentLocation The location of the actor carrying this item.
     * @param actor           The actor carrying this item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    /**
     * Does NOT tick cooldown when on the ground — the timer is paused.
     *
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        // Intentionally left empty: cooldown does not progress on the floor
    }

    /**
     * Returns the UseFirstAidKitAction if the kit is not on cooldown.
     *
     * @param owner the actor carrying this item
     * @param map   the current game map
     * @return action list containing the use action if available
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (isReady()) {
            actions.add(new UseFirstAidKitAction(this));
        }
        return actions;
    }

    /**
     * Uses the kit: increases max health by 1 and restores health to full.
     * Starts the cooldown.
     *
     * @param actor the actor using the kit
     * @return description of what happened
     */
    public String use(Actor actor) {
        // Increase max health by 1 and restore to full
        actor.modifyStatisticMaximum(
                edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH,
                StatisticOperations.INCREASE,
                1
        );
        actor.heal(actor.getMaximumStatistic(edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH));
        cooldownRemaining = COOLDOWN_TURNS;
        return actor + " uses the First Aid Kit! Max health increased by 1 and health fully restored. "
                + "(Cooldown: " + COOLDOWN_TURNS + " turns)";
    }

    /**
     * @return true if the kit is ready to be used (cooldown has expired)
     */
    public boolean isReady() {
        return cooldownRemaining == 0;
    }

    /**
     * @return remaining cooldown turns
     */
    public int getCooldownRemaining() {
        return cooldownRemaining;
    }

    @Override
    public String toString() {
        if (isReady()) {
            return "First Aid Kit (ready)";
        }
        return "First Aid Kit (cooldown: " + cooldownRemaining + " turns)";
    }
}