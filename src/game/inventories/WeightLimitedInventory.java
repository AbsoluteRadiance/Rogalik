package game.inventories;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.items.Item;
import game.utils.ItemStatistics;
import game.items.Flask;

/**
 * At its core, this is just an oversized {@code ArrayList}.
 * It prevents the player from simply carrying the entire {@code EclipseNebula}
 * with them by enforcing an arbitrary capacity limit.
 * Additionally, it enforces that a Flask, once added, can never be removed —
 * even when empty. The corporation insists you return it for "recycling."
 *
 * @author Adrian Kristanto
 */
public class WeightLimitedInventory extends Inventory {

    private static final int WEIGHT_LIMIT = 50;
    private int currentWeight;

    public WeightLimitedInventory() {
        this.currentWeight = 0;
    }

    /**
     * Adds the item if it has a weight statistic and the weight limit is not exceeded.
     * Flask is always added at construction time via the ContractedWorker.
     *
     * @param item The Item to add.
     * @return true if the item is successfully added, false otherwise.
     */
    @Override
    public boolean add(Item item) {
        Display display = new Display();
        if (!item.hasStatistic(ItemStatistics.WEIGHT)) {
            throw new IllegalArgumentException(item + " does not have a weight statistic");
        }

        int itemWeight = item.getStatistic(ItemStatistics.WEIGHT);
        if (currentWeight + itemWeight <= WEIGHT_LIMIT) {
            items.add(item);
            currentWeight += itemWeight;
            display.println(String.format("%s added. Inventory weight: (%d/%d)",
                    item, currentWeight, WEIGHT_LIMIT));
            return true;
        } else {
            display.println(String.format("Cannot add %s (weight: %d). Would exceed weight limit (%d/%d)",
                    item, itemWeight, currentWeight + itemWeight, WEIGHT_LIMIT));
            return false;
        }
    }

    /**
     * Removes the item from the inventory unless it is a Flask, which must remain
     * permanently (even when empty).
     *
     * @param item The Item to remove.
     * @return true if removed, false if item is a Flask (cannot be removed).
     */
    @Override
    public boolean remove(Item item) {
        if (item instanceof Flask) {
            new Display().println("The flask cannot be removed from the inventory.");
            return false;
        }

        if (!item.hasStatistic(ItemStatistics.WEIGHT)) {
            throw new IllegalArgumentException(item + " does not have a weight statistic");
        }

        if (items.remove(item)) {
            currentWeight -= item.getStatistic(ItemStatistics.WEIGHT);
            return true;
        }
        return false;
    }

    /**
     * @return current total weight of inventory contents
     */
    public int getCurrentWeight() {
        return currentWeight;
    }

    /**
     * @return the fixed weight limit of this inventory
     */
    public int getWeightLimit() {
        return WEIGHT_LIMIT;
    }
}