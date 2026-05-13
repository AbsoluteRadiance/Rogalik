package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.ItemStatistics;

/**
 * Represents a CRT monitor found on the Moon facility.
 * Is even more useless than the floppy disk somehow.
 * Not only does this item do absolutely nothing but it also takes up 30 weight.
 * Can be picked up and dropped by workers.
 */
public class CrtMonitor extends Item {
    private static final int WEIGHT = 30;

    /**
     * Constructor for the CrtMonitor.
     * Assigns its attributes according to the fields above.
     */
    public CrtMonitor() {
        super("CRT Monitor", '◙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }
}