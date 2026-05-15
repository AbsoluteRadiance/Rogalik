package game.grounds.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AlarmLocked;
import game.actions.LockableDoor;
import game.actions.UnlockEffect;
import game.enums.DoorLevel;
import game.map.AlarmSystem;

/**
 * An aluminium security door ({@code =}) requiring Access Card Level 1 or higher.
 *
 * <p>When unlocked, faulty wiring delivers an electrical shock to the worker,
 * dealing exactly {@value #SHOCK_DAMAGE} points of damage. The door re-seals
 * if the alarm is active.</p>
 *
 * <p>The side effect is exposed through {@link #getUnlockEffect()} so that
 * {@link game.actions.UnlockDoorAction} never needs to downcast to this
 * concrete type (OCP, DIP).</p>
 */
public class AluminiumDoor extends Ground implements LockableDoor, AlarmLocked {

    /** Damage dealt to the worker from the electrical short-circuit on unlock. */
    private static final int SHOCK_DAMAGE = 2;

    /** Whether this door is currently unlocked. */
    private boolean unlocked = false;

    /** Whether this door is currently on lockdown due to the alarm. */
    private boolean onLockdown = false;

    /** Constructor for AluminiumDoor. */
    public AluminiumDoor() {
        super('=', "Aluminium Door");
    }

    /**
     * Returns the clearance level required to open this door.
     *
     * @return {@link DoorLevel#LEVEL_1}
     */
    @Override
    public DoorLevel getRequiredLevel() {
        return DoorLevel.LEVEL_1;
    }

    /**
     * Unlocks this door, allowing actors to pass through.
     * Called by {@link game.actions.UnlockDoorAction} after clearance is verified.
     */
    @Override
    public void unlock() {
        unlocked = true;
    }

    /**
     * Returns whether this door is currently unlocked and not on lockdown.
     *
     * @return {@code true} if unlocked and not on lockdown
     */
    @Override
    public boolean isUnlocked() {
        return unlocked && !onLockdown;
    }

    /**
     * Activates lockdown if the global alarm is active, re-sealing the door.
     * Implements {@link game.actions.AlarmLocked} so callers need not reference this class directly.
     */
    public void activateLockdown() {
        if (AlarmSystem.isActive()) {
            unlocked = false;
            onLockdown = true;
        }
    }

    /**
     * Actors may only enter when the door is unlocked and not on lockdown.
     *
     * @param actor the actor attempting to enter
     * @return {@code true} if the door is open
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked();
    }

    /**
     * Returns the electrical-shock {@link UnlockEffect} for this door type.
     *
     * <p>Also checks for alarm lockdown before the effect runs. This keeps
     * all aluminium-door logic inside this class — no instanceof in the caller.</p>
     *
     * @return an {@link UnlockEffect} that shocks the actor and handles lockdown
     */
    @Override
    public UnlockEffect getUnlockEffect() {
        return (actor, doorLocation) -> {
            // Re-check lockdown: activateLockdown has already been called by execute()
            if (!isUnlocked()) {
                return "The alarm is active — the aluminium door is sealed.";
            }
            actor.hurt(SHOCK_DAMAGE);
            return actor + " unlocks the aluminium door and receives an electrical shock for "
                    + SHOCK_DAMAGE + " damage!";
        };
    }
}