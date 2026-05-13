package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.ItemStatistics;

/**
 * Represents a floppy disk found on the Moon facility.
 * Is basically deadweight - no data can be decrypted off the disk itself.
 * Weighs 1 unit and can be picked up and dropped by workers.
 */
public class FloppyDisk extends Item {
    private static final int WEIGHT = 1;

    /**
     * Constructor for the Floppy Disk.
     * Assigns its attributes according to the fields above.
     */
    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }
}