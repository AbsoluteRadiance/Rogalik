package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Sellable;
import game.actors.Wallet;
import game.enums.ItemStatistics;

/**
 * An ancient floppy disk — lightweight scrap worth collecting.
 *
 * <p><b>REQ1</b>: Implements {@link Sellable}. Sells for 1 credit.
 * 50% chance the Supercomputer glitches and deducts 50 credits from
 * the seller after giving the 1 credit. Uses {@code asCapability()}
 * to access the wallet without tight coupling.</p>
 *
 * @author Ben (base), Shivam (REQ1 Sellable)
 */
public class FloppyDisk extends Item implements Sellable {

    private static final int WEIGHT = 1;
    private static final int SELL_PRICE = 1;
    private static final int GLITCH_PENALTY = 50;

    /**
     * Constructor for FloppyDisk.
     */
    public FloppyDisk() {
        super("Floppy Disk", '⋟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sell price: 1 credit.
     *
     * @return 1
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * 50% chance Supercomputer glitches and deducts 50 credits.
     * Uses asCapability() to access wallet — no direct Wallet coupling.
     *
     * @param seller   the worker selling
     * @param location the seller's location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        if (Math.random() < 0.5) {
            seller.asCapability(Wallet.class).ifPresent(wallet -> {
                wallet.deductCredits(GLITCH_PENALTY);
                System.out.println("SUPERCOMPUTER GLITCH! "
                        + seller + " loses " + GLITCH_PENALTY + " credits!");
            });
        }
    }
}