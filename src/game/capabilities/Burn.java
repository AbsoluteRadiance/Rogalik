package game.capabilities;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that burns a {@link Flammable} entity for 1 damage per turn
 * for a fixed duration.
 * Applied when an actor stands on a {@link game.grounds.Fire} ground tile.
 *
 * <p>Modelled after {@code Burning} in
 * {@code edu.monash.fit2099.demo.mars.capabilities}.
 *
 * @author Adrian Kristanto
 */
public class Burn implements Status {

    private static final int BURN_DURATION = 5;
    private int duration;
    private final Flammable flammable;

    /**
     * Constructor.
     *
     * @param flammable the entity to burn each tick
     */
    public Burn(Flammable flammable) {
        this.flammable = flammable;
        this.duration = BURN_DURATION;
    }

    @Override
    public void tickStatus(GameEntity entity, Location location) {
        System.out.println("Burn ticking on " + entity + " | duration remaining: " + duration);
        if (flammable != null) {
            flammable.burn(1);
            System.out.println("Dealt 1 burn damage. Entity: " + entity);
        } else {
            System.out.println("Flammable is null — no damage dealt.");
        }
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}