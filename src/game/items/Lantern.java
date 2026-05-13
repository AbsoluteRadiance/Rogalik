package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.Fire;
import game.enums.ItemStatistics;

import java.util.Random;

/**
 * Represents a (rather unstable) Lantern carried by workers.
 * While carried, the lantern has a small chance to leak oil and start a fire.
 * Each leak consumes one unit of oil.
 * When all oil is consumed, the lantern is dropped by the worker.
 */
public class Lantern extends Item {
    private static final int WEIGHT = 7;
    private static final int MAX_OIL = 10;
    private static final int LEAK_CHANCE = 5;
    private int oilRemaining;
    private final Random random = new Random();

    /**
     * Constructor for Lantern.
     * Sets the oil to its max value,
     * and assigns its attributes according to the fields above.
     */
    public Lantern() {
        super("Lantern", '&');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.oilRemaining = MAX_OIL;
    }

    /**
     * Called once per turn while the lantern is being carried.
     * Has a 5% chance to leak oil, which starts a fire
     * and decrements the oil supply by 1.
     * Remove from the worker's inventory when oil is fully depleted.
     *
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (oilRemaining > 0) {
            if (random.nextInt(100) < LEAK_CHANCE) {
                Ground originalGround = currentLocation.getGround();
                currentLocation.setGround(new Fire(originalGround));
                oilRemaining--;
                if (oilRemaining <= 0) {
                    actor.getInventory().remove(this);
                }
            }
        }
    }
}