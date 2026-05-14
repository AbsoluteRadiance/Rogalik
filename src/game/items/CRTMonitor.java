package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Sellable;
import game.enums.ItemStatistics;
import game.grounds.Fire;
import game.status.DamageOverTimeStatus;

/**
 * A massive, incredibly heavy CRT monitor.
 *
 * <p><b>REQ1</b>: Implements {@link Sellable}. Sells for 25 credits.
 * Always heals seller 5 HP on sale (relief of dropping 30 weight).
 * 20% chance: short circuit deals 2 damage AND spawns Fire on all adjacent tiles.</p>
 *
 * @author Ben (base), Shivam (REQ1 Sellable)
 */
public class CrtMonitor extends Item implements Sellable {

    private static final int WEIGHT = 30;
    private static final int SELL_PRICE = 25;
    private static final int SELL_HEAL = 5;
    private static final int SHORT_DAMAGE = 2;

    /**
     * Constructor for CrtMonitor.
     */
    public CrtMonitor() {
        super("CRT Monitor", '⛙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sell price: 25 credits.
     *
     * @return 25
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Always heals seller 5 HP. 20% chance: 2 damage + Fire on all surroundings.
     *
     * @param seller   the worker selling
     * @param location the seller's location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        seller.heal(SELL_HEAL);
        System.out.println(seller + " feels relief dropping the CRT monitor! +5 HP!");

        if (Math.random() < 0.20) {
            seller.hurt(SHORT_DAMAGE);
            System.out.println("SHORT CIRCUIT! " + seller + " takes " + SHORT_DAMAGE + " damage!");
            for (Exit exit : location.getExits()) {
                Location dest = exit.getDestination();
                dest.setGround(new Fire(dest.getGround(), 5));
            }
            System.out.println("Fire spawns on all surrounding tiles!");
        }
    }
}