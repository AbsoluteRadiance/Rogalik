package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Sellable;
import game.enums.ItemStatistics;
import game.grounds.Dirt;
import game.grounds.Fire;
import game.status.DamageOverTimeStatus;

/**
 * An unstable lantern that leaks and ignites the ground beneath its carrier.
 *
 * <p><b>REQ1</b>: Implements {@link Sellable}. Sell price = 5 credits per
 * remaining oil unit (max 50). On sale:
 * <ul>
 *   <li>50% chance: burns the seller (2 dmg/turn for 3 turns)</li>
 *   <li>Independent 25% chance: spawns Fire on all surrounding tiles</li>
 * </ul>
 * Both effects can occur at the same time.</p>
 *
 * @author Ben (base), Shivam (REQ1 Sellable)
 */
public class Lantern extends Item implements Sellable {

    private static final int WEIGHT = 7;
    private static final int MAX_OIL = 10;
    private static final double LEAK_CHANCE = 0.05;
    private static final int CREDITS_PER_OIL = 5;
    private static final int SELL_BURN_DAMAGE = 2;
    private static final int SELL_BURN_TURNS = 3;

    private int oilRemaining;

    /**
     * Constructor for Lantern.
     */
    public Lantern() {
        super("Lantern", '&');
        this.oilRemaining = MAX_OIL;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Each turn carried: 5% chance to leak, spawning Fire and consuming oil.
     *
     * @param currentLocation the carrier's location
     * @param actor           the carrier
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (oilRemaining > 0 && Math.random() < LEAK_CHANCE) {
            oilRemaining--;
            currentLocation.setGround(new Fire(currentLocation.getGround()));
            System.out.println("The lantern leaks! Fire spawned! Oil remaining: " + oilRemaining);
        }
    }

    /**
     * Sell price: 5 credits per oil unit (max 50).
     *
     * @return credits earned
     */
    @Override
    public int getSellPrice() {
        return oilRemaining * CREDITS_PER_OIL;
    }

    /**
     * 50% chance burns seller (2 dmg/turn, 3 turns).
     * Independent 25% chance spawns Fire on all surrounding tiles.
     *
     * @param seller   the worker selling
     * @param location the seller's location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        if (Math.random() < 0.5) {
            seller.addStatus(new DamageOverTimeStatus(SELL_BURN_TURNS, SELL_BURN_DAMAGE));
            System.out.println(seller + " is burned selling the lantern! ("
                    + SELL_BURN_DAMAGE + " dmg/turn for " + SELL_BURN_TURNS + " turns)");
        }
        if (Math.random() < 0.25) {
            for (Exit exit : location.getExits()) {
                Location dest = exit.getDestination();
                dest.setGround(new Fire(dest.getGround()));
            }
            System.out.println("The lantern explodes! Fire on all surrounding tiles!");
        }
    }

    /**
     * Returns oil remaining (used by REQ4 infection).
     *
     * @return oil remaining
     */
    public int getOilRemaining() {
        return oilRemaining;
    }

    /**
     * Drains 1 oil unit (used by REQ4 infection).
     */
    public void drainOil() {
        if (oilRemaining > 0) oilRemaining--;
    }

    @Override
    public String toString() {
        return "Lantern (oil: " + oilRemaining + "/" + MAX_OIL + ")";
    }
}