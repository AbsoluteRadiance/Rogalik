package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.DoorLevel;
import game.enums.WorkerAbility;
import game.grounds.doors.AluminiumDoor;
import game.grounds.doors.IronDoor;
import game.grounds.doors.TitaniumDoor;
import game.items.AccessCard;
import game.map.AlarmSystem;

/**
 * Action that allows a worker carrying a qualifying {@link AccessCard} to unlock
 * a security door.
 *
 * <p>Clearance is checked by comparing the card level stored in the actor's
 * inventory against the door's {@link LockableDoor#getRequiredLevel()}. No
 * {@code instanceof} checks are needed for the clearance decision itself.
 * Door-specific side effects (shock, fire, heal) are triggered by calling
 * back onto typed references obtained via {@link Location#getGroundAs}, which
 * is the engine's own capability pattern: justified because each side effect
 * is intrinsically tied to the concrete door type and cannot be modelled
 * without knowing which door was unlocked.</p>
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
     *   <li>Check the global alarm: aluminium doors re-seal under lockdown.</li>
     *   <li>Retrieve the {@link LockableDoor} from the door's ground.</li>
     *   <li>Determine the highest card level in the actor's inventory.</li>
     *   <li>Verify the card level meets the door's required clearance.</li>
     *   <li>Unlock and apply the door-specific side effect.</li>
     * </ol>
     *
     * @param actor the worker performing the action
     * @param map   the current game map
     * @return a string describing what happened
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Alarm lockdown check for aluminium doors
        AluminiumDoor aluminiumDoor = doorLocation.getGroundAs(AluminiumDoor.class);
        if (aluminiumDoor != null) {
            aluminiumDoor.activateLockdown();
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
        return applyUnlockEffect(actor, lockable);
    }

    /**
     * Applies the side effect specific to the door type that was just unlocked.
     *
     * <p>Using typed ground lookups here is justified: each side effect is
     * intrinsically coupled to the concrete door subtype and cannot be abstracted
     * further into the {@link LockableDoor} interface without violating SRP.</p>
     *
     * @param actor    the actor who unlocked the door
     * @param lockable the lockable ground that was just unlocked
     * @return result message
     */
    private String applyUnlockEffect(Actor actor, LockableDoor lockable) {
        // Aluminium: electrical shock
        AluminiumDoor al = doorLocation.getGroundAs(AluminiumDoor.class);
        if (al != null) {
            actor.hurt(al.getShockDamage());
            return actor + " unlocks the aluminium door to the " + direction
                    + " and receives an electrical shock for " + al.getShockDamage() + " damage!";
        }

        // Iron: overheat: fire on adjacent floor tiles
        IronDoor iron = doorLocation.getGroundAs(IronDoor.class);
        if (iron != null) {
            iron.triggerOverheat(doorLocation);
            return actor + " unlocks the iron door to the " + direction
                    + ". The mechanism overheats: fire spreads to adjacent floor tiles!";
        }

        // Titanium: decontamination: heal worker
        TitaniumDoor titanium = doorLocation.getGroundAs(TitaniumDoor.class);
        if (titanium != null) {
            actor.heal(TitaniumDoor.HEAL_AMOUNT);
            return actor + " unlocks the titanium door to the " + direction
                    + ". Decontamination sequence triggered: +" + TitaniumDoor.HEAL_AMOUNT + " HP!";
        }

        return actor + " unlocks the door to the " + direction + ".";
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
        int requiredInt = requiredLevel.ordinal() + 1; // LEVEL_1=1, LEVEL_2=2, LEVEL_3=3
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