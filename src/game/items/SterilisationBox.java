package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.utils.GameAbilities;
import game.utils.ItemStatistics;

/**
 * This item weighs 7 units and provides the necessary function of sterilising
 * any consumables, including items and grounds, found on the moon's facility.
 * Because in the Eclipse Nebula, you never know what alien gunk has touched
 * the things you're about to put in your mouth.
 */
public class SterilisationBox extends Item {

    private static final int WEIGHT = 7;

    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.enableAbility(GameAbilities.HAS_STERILISATION);
    }
}