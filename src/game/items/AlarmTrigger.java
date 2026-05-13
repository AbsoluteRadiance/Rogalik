package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.map.AlarmSystem;
import game.enums.ItemStatistics;

/**
 * Represents an item that looks like a suspicious piece of circuitry.
 * Upon being picked up - on the following tick - the Moon facility's alarm activates.
 */
public class AlarmTrigger extends Item {
    private static final int WEIGHT = 1;

    /**
     * Constructor for AlarmTrigger
     * Assigns its attributes according to the field above.
     */
    public AlarmTrigger() {
        super("Oddly Suspicious Looking Circuitry", '!');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Called once per turn while carrying the AlarmTrigger.
     * Activates the alarm every turn while carried.
     *
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        AlarmSystem.activate();
    }
}