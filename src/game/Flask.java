package game;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

/**
 * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment. Employees are reminded not to consume
 * all five charges in a panic during a single encounter.
 * Once depleted, the empty flask remains in the worker's inventory permanently.
 */
public class Flask extends Item {

    private static final int MAX_USES = 5;
    private static final int WEIGHT = 3;
    private static final int HEAL_AMOUNT = 1;

    private int remainingUses;

    public Flask() {
        super("Flask", 'u');
        this.remainingUses = MAX_USES;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        // Flask starts portable so it can be placed in inventory, but cannot be dropped
        // (it stays in inventory permanently once picked up)
    }

    /**
     * Attempts to consume one use of the flask.
     *
     * @return the amount to heal, or 0 if the flask is depleted
     */
    public int consume() {
        if (remainingUses > 0) {
            remainingUses--;
            return HEAL_AMOUNT;
        }
        return 0;
    }

    /**
     * @return true if the flask still has uses remaining
     */
    public boolean isDepleted() {
        return remainingUses <= 0;
    }

    /**
     * @return the number of remaining uses
     */
    public int getRemainingUses() {
        return remainingUses;
    }

    @Override
    public String toString() {
        return "Flask (" + remainingUses + "/" + MAX_USES + " uses)";
    }
}