package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.ConsumeItemAction;
import game.capabilities.Poison;
import game.utils.GameAbilities;
import game.utils.ItemStatistics;

/**
 * A spoiled fruit that is toxic when consumed without sterilisation.
 * Without SterilisationBox: poisons consumer (1 damage/turn, 5 turns).
 * With SterilisationBox: heals consumer for 3 HP.
 * Disappears from inventory after consumption.
 * Weighs 1 unit.
 */
public class Apple extends Item implements Consumable {

    private static final int WEIGHT = 1;
    private static final int POISON_DURATION = 5;
    private static final int HEAL_AMOUNT = 3;

    public Apple() {
        super("Apple", 'ó');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
    }

    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new ConsumeItemAction(this));
        return actions;
    }

    @Override
    public String consume(Actor actor, GameMap map) {
        if (actor.hasAbility(GameAbilities.HAS_STERILISATION)) {
            actor.heal(HEAL_AMOUNT);
            actor.getInventory().remove(this);
            return actor + " sterilises and eats the apple, healing " + HEAL_AMOUNT + " HP.";
        }
        actor.addStatus(new Poison(POISON_DURATION));
        actor.getInventory().remove(this);
        return actor + " eats the spoiled apple and is poisoned for " + POISON_DURATION + " turns!";
    }
}