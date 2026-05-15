package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.LockableDoor;
import game.actions.Purchasable;
import game.actions.UnlockDoorAction;
import game.items.ClearanceLevel;
import game.actors.Wallet;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;

/**
 * A security access card granting entry through doors.
 *
 * <p><b>REQ1</b>: Implements {@link Purchasable}. Three tiers:
 * <ul>
 *   <li>Level 1 (50 credits, weight 1, '▤'): no side effects on buy</li>
 *   <li>Level 2 (100 credits, weight 2, 'α'): blood extraction deals 5 damage on buy</li>
 *   <li>Level 3 (200 credits, weight 3, '◐'): 50% hidden fee of 50 extra credits on buy</li>
 * </ul>
 * Uses {@code asCapability()} to access wallet without tight coupling.</p>
 *
 * @author Ben (base), Shivam (REQ1 tiered Purchasable), Ilya
 */
public class AccessCard extends Item implements ClearanceLevel, Purchasable {

    /** The clearance level of this card. */
    private final int level;

    /**
     * Constructs an access card of the given level.
     *
     * @param level 1, 2, or 3
     */
    public AccessCard(int level) {
        super("Access Card (Level " + level + ")", levelToChar(level));
        this.level = level;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(level));
        this.makePortable();
        this.enableAbility(WorkerAbility.CAN_UNLOCK_DOOR);
    }

    /**
     * Default constructor — Level 1 (backward compatible with A1).
     */
    public AccessCard() {
        this(1);
    }

    /** Maps level to display character. */
    private static char levelToChar(int level) {
        return switch (level) {
            case 1 -> '▤';
            case 2 -> 'α';
            case 3 -> '◐';
            default -> '?';
        };
    }

    /**
     * Returns this card's clearance level.
     *
     * @return 1, 2, or 3
     */
    public int getLevel() {
        return level;
    }

    /**
     * Provides {@link UnlockDoorAction}s for every adjacent lockable door.
     * Uses the {@link LockableDoor} capability check via {@link Location#getGroundAs}
     * to avoid coupling to concrete door types.
     *
     * @param owner the actor carrying this card
     * @param map   the current game map
     * @return list of unlock actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location location = map.locationOf(owner);
        for (Exit exit : location.getExits()) {
            LockableDoor lockable = exit.getDestination().getGroundAs(LockableDoor.class);
            if (lockable != null) {
                actions.add(new UnlockDoorAction(exit.getDestination()));
            }
        }
        return actions;
    }

    /**
     * Purchase price: 50 / 100 / 200 for levels 1 / 2 / 3.
     *
     * @return purchase price
     */
    @Override
    public int getBuyPrice() {
        return switch (level) {
            case 1 -> 50;
            case 2 -> 100;
            case 3 -> 200;
            default -> 0;
        };
    }

    /**
     * Level 2: blood extraction deals 5 damage immediately.
     * Level 3: 50% chance of hidden fee via asCapability() — no Wallet coupling.
     * Level 1: no side effects.
     *
     * @param buyer    the worker buying
     * @param location the buyer's location
     */
    @Override
    public void onBuy(Actor buyer, Location location) {
        if (level == 2) {
            buyer.hurt(5);
            System.out.println("Blood sample extracted! " + buyer + " takes 5 damage!");
        } else if (level == 3) {
            if (Math.random() < 0.5) {
                buyer.asCapability(Wallet.class).ifPresent(wallet -> {
                    wallet.deductCredits(50);
                    System.out.println("HIDDEN FEE! " + buyer + " charged an extra 50 credits!");
                });
            }
        }
    }

    /**
     * Creates a new AccessCard of the same level.
     *
     * @return new AccessCard instance
     */
    @Override
    public Item createInstance() {
        return new AccessCard(level);
    }

    @Override
    public String toString() {
        return "Access Card (Level " + level + ")";
    }
}