package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.Fire;
import game.utils.ItemStatistics;

import java.util.Random;

/**
 * An unstable light source with 10 units of oil fuel.
 * While carried, has a 5% chance each turn to leak and ignite the ground
 * beneath the actor, spawning a Fire tile and consuming 1 unit of fuel.
 * Weighs 7 units.
 */
public class Lantern extends Item {

    private static final int WEIGHT = 7;
    private static final int MAX_FUEL = 10;
    private static final double LEAK_CHANCE = 0.05;

    private int fuel;
    private final Random random;

    public Lantern() {
        super("Lantern", '&');
        makePortable();
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.fuel = MAX_FUEL;
        this.random = new Random();
    }

    /**
     * Each turn while carried, 5% chance to leak and ignite the ground below.
     * Reduces fuel by 1 and spawns a Fire ground at the actor's location.
     *
     * @param currentLocation the location of the carrying actor
     * @param actor           the actor carrying this lantern
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (fuel > 0 && random.nextDouble() <= LEAK_CHANCE) {
            fuel--;
            currentLocation.setGround(new Fire());
            System.out.println(actor + "'s lantern leaks! Fire ignites beneath them. (Fuel: "
                    + fuel + "/" + MAX_FUEL + ")");
        }
    }

    @Override
    public String toString() {
        return "Lantern (fuel: " + fuel + "/" + MAX_FUEL + ")";
    }
}