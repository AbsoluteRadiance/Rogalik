package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Purchasable;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

import java.util.List;
import java.util.Random;

/**
 * A sterilisation box that purifies consumables when carried.
 *
 * <p>Grants {@link WorkerAbility#CAN_STERILISE} to the carrier via the
 * engine's ability propagation system.</p>
 *
 * <p><b>REQ1</b>: Now also implements {@link Purchasable}. Costs 750 credits.
 * The intense radiation permanently erases one random item from the buyer's
 * inventory immediately on purchase.</p>
 *
 * @author Ben (base), Shivam (REQ1 Purchasable)
 */
public class SterilisationBox extends Item implements Purchasable {

    private static final int WEIGHT = 5;
    private static final int BUY_PRICE = 750;

    /**
     * Constructor for SterilisationBox.
     */
    public SterilisationBox() {
        super("Sterilisation Box", 'S');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.enableAbility(WorkerAbility.CAN_STERILISE);
    }

    /**
     * Purchase price: 750 credits.
     *
     * @return 750
     */
    @Override
    public int getBuyPrice() {
        return BUY_PRICE;
    }

    /**
     * Radiation side effect: permanently erases one random item
     * from the buyer's inventory on purchase.
     *
     * @param buyer    the worker buying
     * @param location the buyer's location
     */
    @Override
    public void onBuy(Actor buyer, Location location) {
        List<Item> items = buyer.getInventory().getItems();
        if (!items.isEmpty()) {
            Item erased = items.get(new Random().nextInt(items.size()));
            buyer.getInventory().remove(erased);
            System.out.println("RADIATION SURGE! " + erased
                    + " is permanently erased from " + buyer + "'s inventory!");
        }
    }

    /**
     * Creates a new SterilisationBox instance.
     *
     * @return new SterilisationBox
     */
    @Override
    public Item createInstance() {
        return new SterilisationBox();
    }

    @Override
    public String toString() {
        return "Sterilisation Box";
    }
}