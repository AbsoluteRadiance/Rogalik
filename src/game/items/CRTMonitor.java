package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.utils.ItemStatistics;

/**
 * A massive, incredibly heavy piece of archaic junk.
 * Weighs 30 units. Has no special interaction.
 */
public class CRTMonitor extends Item {

    private static final int WEIGHT = 30;

    public CRTMonitor() {
        super("CRT Monitor", '◙');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
    }
}