package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.DoorLevel;
import game.grounds.doors.AluminiumDoor;
import game.items.AccessCard;
import game.map.AlarmSystem;

/**
 * Action that allows a worker carrying a qualifying {@link AccessCard} to unlock
 * a security door.
 *
 * <p>Clearance is checked by comparing the card level against the door's
 * {@link LockableDoor#getRequiredLevel()}. The post-unlock side effect is
 * retrieved via {@link LockableDoor#getUnlockEffect()} — no {@code instanceof}
 * or downcast is needed anywhere in this class (OCP, DIP).</p>
 *
 * <p>The only remaining concrete-type reference is to {@link AluminiumDoor}
 * for the alarm lockdown check, which is an aluminium-specific precondition
 * that cannot be modelled generically without adding alarm awareness to the
 * {@link LockableDoor} interface (which would violate SRP). This is justified
 * in a comment at the call site.</p>
 */
public class UnlockDoorAction extends Action {

    /** Location of the door to unlock. */
    private final Location doorLocation;

    /** Direction label shown in the menu (e.g. "East"). */
    private final String direction;

    /**
     * Constructs an unlock action for the door at the given location.
     *
     * @param doorLocation location of the door
     * @param direction    direction from the actor to the door (for menu display)
     */
    public UnlockDoorAction(Location doorLocation, String direction) {
        this.doorLocation = doorLocation;
        this.direction = direction;
    }

    /**
     * Legacy constructor retained for backward compatibility; direction defaults to empty.
     *
     * @param doorLocation location of the door
     */
    public UnlockDoorAction(Location doorLocation) {
        this(doorLocation, "");
    }

    /**
     * Attempts to unlock the door. Steps:
     * <ol>
     *   <li>Perform the aluminium-door alarm lockdown check (the only door type
     *       that re-seals under alarm — justified downcast via
     *       {@link Location#getGroundAs} using the {@link AluminiumDoor.AlarmLocked}
     *       interface).</li>
     *   <li>Retrieve the {@link LockableDoor} capability from the door's ground.</li>
     *   <li>Determine the highest card level in the actor's inventory.</li>
     *   <li>Verify clearance against {@link LockableDoor#getRequiredLevel()}.</li>
     *   <li>Unlock and call {@link LockableDoor#getUnlockEffect()} — no further
     *       type knowledge required.</li>
     * </ol>
     *
     * @param actor the worker performing the action
     * @param map   the current game map
     * @return a string describing what happened
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        /*
         * Alarm lockdown is specific to AluminiumDoor and cannot be abstracted into
         * LockableDoor without leaking alarm-system knowledge into the door interface
         * (SRP violation). We therefore use AlarmLocked — a narrow interface
         * implemented only by AluminiumDoor — to perform this check without coupling
         * to the concrete class.
         */
        AlarmLocked AlarmLocked = doorLocation.getGroundAs(AlarmLocked.class);
        if (AlarmLocked != null) {
            AlarmLocked.activateLockdown();
            if (AlarmSystem.isActive()) {
                return "The alarm is active! The aluminium door is sealed.";
            }
        }

        LockableDoor lockable = doorLocation.getGroundAs(LockableDoor.class);
        if (lockable == null) {
            return "There is no lockable door at " + doorLocation + ".";
        }

        int cardLevel = highestCardLevel(actor);
        if (cardLevel == 0) {
            return actor + " does not have an access card.";
        }

        if (!hasRequiredClearance(cardLevel, lockable.getRequiredLevel())) {
            return actor + "'s access card (Level " + cardLevel
                    + ") does not have sufficient clearance for this door.";
        }

        lockable.unlock();

        // Delegate the side effect entirely to the door — no type knowledge here.
        String effectResult = lockable.getUnlockEffect().apply(actor, doorLocation);
        if (effectResult == null || effectResult.isEmpty()) {
            return actor + " unlocks the door to the " + direction + ".";
        }
        return effectResult;
    }

    /**
     * Scans the actor's inventory for access cards and returns the highest level found.
     * Returns 0 if no access card is present.
     *
     * @param actor the actor whose inventory is scanned
     * @return the highest access card level, or 0 if none found
     */
    private int highestCardLevel(Actor actor) {
        return actor.getInventory().getItemsAs(AccessCard.class)
                .stream()
                .mapToInt(AccessCard::getLevel)
                .max()
                .orElse(0);
    }

    /**
     * Determines whether a given card level satisfies a door's required clearance.
     * Higher card levels always satisfy lower door requirements.
     *
     * @param cardLevel     the level of the card the actor holds (1, 2, or 3)
     * @param requiredLevel the level the door requires
     * @return {@code true} if the card level is sufficient
     */
    private boolean hasRequiredClearance(int cardLevel, DoorLevel requiredLevel) {
        int requiredInt = requiredLevel.ordinal() + 1;
        return cardLevel >= requiredInt;
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor performing the action
     * @return menu label
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks door to the " + direction;
    }
}