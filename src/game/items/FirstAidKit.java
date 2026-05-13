package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import game.actions.Consumable;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;

/**
 * Represents the first aid kit carried by workers in the moon's facility.
 * When consumed, permanently increases the carrier's maximum health by 1,
 * and restores their health to full.
 * Has a 20-turn cooldown between uses. Cooldown pauses if the kit is dropped on the ground.
 */
public class FirstAidKit extends Item implements Consumable {
    private static final int WEIGHT = 25;
    private static final int COOLDOWN = 20;
    private int cooldownRemaining;

    /**
     * Constructor for First Aid Kit.
     * Assigns its attributes according to the fields above,
     * and initialises the cooldown at 0 - it hasn't been used yet.
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.cooldownRemaining = 0;
    }

    /**
     * Consumed the First Aid Kit, permanently increasing the worker's maximum health by 1
     * and restoring health to full. Starts the 20-turn cooldown.
     *
     * @param actor the actor who is consuming
     */
    @Override
    public void consume(Actor actor) {
        actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);
        actor.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.UPDATE, actor.getMaximumStatistic(ActorStatistics.HEALTH));
        cooldownRemaining = COOLDOWN;
    }

    /**
     * This method is instead used to return whether the cooldown is still active,
     * as this item has infinite uses.
     *
     * @return a boolean; true if the remaining cooldown is greater than 0, false otherwise
     */
    @Override
    public boolean isDepleted() {
        return cooldownRemaining > 0;
    }

    /**
     * Called once per turn while carried.
     * Checks if the cooldown is active,
     * and if it is, decrements the cooldown by 1 per turn.
     *
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (cooldownRemaining > 0) {
            cooldownRemaining -= 1;
        }
    }

    /**
     * Returns a list of allowable actions for the carrier.
     * Includes a ConsumeAction only if the first aid kit is off cooldown.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (!isDepleted()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }
}