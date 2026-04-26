package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.ConsumeItemAction;
import game.utils.GameAbilities;
import game.utils.ItemStatistics;

/**
 * A packet of 5 cookies. Each cookie eaten permanently decreases the
 * consumer's maximum HP by 1 — unless they carry a SterilisationBox,
 * in which case each cookie heals 1 HP instead.
 * Disappears from inventory only after all 5 cookies are eaten.
 * Weighs 2 units.
 */
public class Cookies extends Item implements Consumable {

    private static final int WEIGHT = 2;
    private static final int MAX_USES = 5;
    private static final int HEAL_AMOUNT = 1;

    private int remainingCookies;

    public Cookies() {
        super("Cookies", '◍');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.remainingCookies = MAX_USES;
    }

    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new ConsumeItemAction(this));
        return actions;
    }

    @Override
    public String consume(Actor actor, GameMap map) {
        remainingCookies--;
        String result;

        if (actor.hasAbility(GameAbilities.HAS_STERILISATION)) {
            actor.heal(HEAL_AMOUNT);
            result = actor + " eats a sterilised cookie and heals " + HEAL_AMOUNT + " HP. ("
                    + remainingCookies + " cookies remaining)";
        } else {
            actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1);
            result = actor + " eats a cookie. Max HP permanently decreased by 1. ("
                    + remainingCookies + " cookies remaining)";
        }

        if (remainingCookies == 0) {
            actor.getInventory().remove(this);
            result += " The packet is empty.";
        }

        return result;
    }

    @Override
    public String toString() {
        return "Cookies (" + remainingCookies + "/" + MAX_USES + ")";
    }
}