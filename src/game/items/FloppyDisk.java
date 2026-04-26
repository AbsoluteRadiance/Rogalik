package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.utils.ItemStatistics;

/**
 * A piece of ancient technology. Extremely lightweight.
 * Weighs 1 unit. Has no special interaction.
 */
public class FloppyDisk extends Item {

    private static final int WEIGHT = 1;

    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
    }
}