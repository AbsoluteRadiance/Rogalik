package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

/**
 * Represents the sterilisation box carried by workers in the Moon's facility.
 * While carried, grants the CAN_STERILISE ability, enabling workers
 * to consume foods and drink objects safely.
 * Weighs 7 units and can be picked up and dropped.
 */
public class SterilisationBox extends Item {
    private static final int WEIGHT = 7;

    /**
     * Constructor for the Sterilisation Box.
     * Assigns its attributes according to the fields above,
     * and grants the ability CAN_STERILISE
     */
    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.enableAbility(WorkerAbility.CAN_STERILISE);
    }
}