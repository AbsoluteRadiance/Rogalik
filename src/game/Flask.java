package game;

import edu.monash.fit2099.engine.items.Item;

/**
 * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment. Employees are reminded not to consume
 * all five charges in a panic during a single encounter.
 */
public class Flask extends Item {
    int totalUsable = 5;
    public Flask() {
        super("Flask", 'u');
    }
}
