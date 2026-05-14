package game.items;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.Sellable;
import game.actions.TeleportAction;
import game.actors.Undead;
import game.enums.ItemStatistics;
import game.grounds.ToxicWaste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * An alien cube ({@code ◈}) that warps space-time.
 *
 * <p>Found scattered inside the 20-overflow moon. Can be picked up and carried
 * in the worker's inventory. While carried, the worker may activate it to
 * choose from 3 randomly selected locations on the current map to teleport to.</p>
 *
 * <h3>Teleport effect</h3>
 * <p>Using the cube permanently corrupts all tiles immediately adjacent to the
 * <em>source</em> location (where the worker was standing), turning them into
 * {@link ToxicWaste}. The cube itself is <b>not</b> consumed — it can be reused
 * (each use corrupts more terrain).</p>
 *
 * <h3>Selling</h3>
 * <p>Implements {@link Sellable} for 25 credits. As a last-resort defence,
 * selling instantly spawns an {@link Undead} on an empty tile directly next
 * to the worker.</p>
 */
public class AlienCube extends Item implements Sellable {

    /** Number of random destination choices presented to the worker. */
    private static final int DESTINATION_COUNT = 3;

    /** Credits earned when this cube is sold to the Supercomputer. */
    private static final int SELL_PRICE = 25;

    /** Weight of the alien cube. */
    private static final int WEIGHT = 2;

    private final Random random = new Random();

    /** Constructor for AlienCube. */
    public AlienCube() {
        super("Alien Cube", '◈');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * While carried, provides teleport actions to 3 randomly chosen locations
     * on the current map. Each action injects a {@link game.actions.TeleportEffect}
     * that corrupts the source location's adjacent tiles with {@link ToxicWaste}.
     *
     * @param owner the worker carrying this cube
     * @param map   the current game map
     * @return list of up to {@value #DESTINATION_COUNT} teleport actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        List<Location> candidates = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location loc = map.at(x, y);
                if (loc.canActorEnter(owner)) {
                    candidates.add(loc);
                }
            }
        }

        Collections.shuffle(candidates, random);
        int count = Math.min(DESTINATION_COUNT, candidates.size());

        for (int i = 0; i < count; i++) {
            Location dest = candidates.get(i);
            String label = "uses Alien Cube → (" + dest.x() + ", " + dest.y() + ")";
            actions.add(new TeleportAction(dest, label, this::corruptSource));
        }
        return actions;
    }

    /**
     * Corrupts all tiles adjacent to the source location by replacing their
     * ground with {@link ToxicWaste}. Called as the {@link game.actions.TeleportEffect}.
     *
     * @param actor       the actor who used the cube
     * @param destination where the actor arrived (unused here)
     * @param source      the location that will be corrupted
     */
    private void corruptSource(Actor actor, Location destination, Location source) {
        for (Exit exit : source.getExits()) {
            exit.getDestination().setGround(new ToxicWaste());
        }
        System.out.println("Reality tears apart! Adjacent tiles at the source are now Toxic Waste!");
    }

    /**
     * Returns the sell price of this cube.
     *
     * @return {@value #SELL_PRICE} credits
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Spawns an {@link Undead} on an empty adjacent tile when sold.
     * If no empty tile is available, the spawn is silently skipped.
     *
     * @param seller   the worker selling this cube
     * @param location the seller's current location
     */
    @Override
    public void onSell(Actor seller, Location location) {
        for (Exit exit : location.getExits()) {
            Location adj = exit.getDestination();
            if (!adj.containsAnActor() && adj.getGround().canActorEnter(new Undead())) {
                try {
                    adj.addActor(new Undead());
                    System.out.println("An Undead materialises next to " + seller + "!");
                    return;
                } catch (GameEngineException e) {
                    // Tile became occupied between the check and the add — try next exit
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Alien Cube";
    }
}