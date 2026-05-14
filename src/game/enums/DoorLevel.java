package game.enums;

/**
 * Enumerates the security clearance tiers used by the tiered door system.
 *
 * <p>An access card grants clearance at its own level and all levels below it.
 * For example, {@link #LEVEL_3} can open {@link #LEVEL_1}, {@link #LEVEL_2},
 * and {@link #LEVEL_3} doors.</p>
 */
public enum DoorLevel {

    /** Aluminium door clearance — requires Access Card Level 1 or higher. */
    LEVEL_1,

    /** Iron door clearance — requires Access Card Level 2 or higher. */
    LEVEL_2,

    /** Titanium door clearance — requires Access Card Level 3 only. */
    LEVEL_3
}