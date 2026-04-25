package game;

import edu.monash.fit2099.engine.items.Item;

/**
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot,
 *
 * @author Adrian Kristanto
 */
public class AccessCard extends Item {
    public AccessCard() {
        super("Access Card", '▤');
    }
}
