package game.enums;

/**
 * An enumeration for housing item abilities.
 * This is primarily used for detecting if a Worker
 * is able to perform a specific action
 * without having to iterate through their inventory.
 * Currently only contains 3 abilities,
 * used to tell if a Worker can unlock or sterilise anything.
 */
public enum WorkerAbility {
    CAN_UNLOCK_DOOR,
    CAN_STERILISE,
}
