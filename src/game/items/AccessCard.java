package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.utils.ItemStatistics;

/**
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot.
 * Weighs 1 unit.
 *
 * @author Adrian Kristanto
 */
public class AccessCard extends Item {

    private static final int WEIGHT = 1;

    public AccessCard() {
        super("Access Card", '▤');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
    }
}