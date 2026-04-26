package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.Burn;
import game.capabilities.Flammable;

/**
 * A fire tile spawned by a leaking {@link game.items.Lantern}.
 * Burns any {@link Flammable} actor standing on it for 1 damage per turn
 * for 5 turns via the {@link Burn} status effect.
 * The fire itself lasts {@value FIRE_DURATION} turns before extinguishing
 * and being replaced by {@link Dirt}.
 */
public class Fire extends Ground {

    /**
     * Number of turns the fire tile remains on the ground.
     */
    private static final int FIRE_DURATION = 5;

    /**
     * Remaining turns before this fire tile extinguishes.
     */
    private int turnsRemaining;

    /**
     * Constructor.
     */
    public Fire() {
        super('^', "Fire");
        this.turnsRemaining = FIRE_DURATION;
    }

    /**
     * Called once per turn by the engine.
     * If a {@link Flammable} actor is present and not already burning,
     * applies a {@link Burn} status to them.
     * Decrements the fire's lifespan and replaces itself with {@link Dirt}
     * when expired.
     *
     * @param location the location of this fire tile
     */
    @Override
    public void tick(Location location) {
        System.out.println("Fire ticking at " + location + " | turns remaining: " + turnsRemaining);

        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            System.out.println("Actor on fire tile: " + actor);

            Flammable flammable = location.getActorAs(Flammable.class);
            System.out.println("Flammable cast result: " + flammable);

            boolean alreadyBurning = actor.hasStatus(Burn.class);
            System.out.println("Already has Burn status: " + alreadyBurning);

            if (flammable != null && !alreadyBurning) {
                actor.addStatus(new Burn(flammable));
                System.out.println("Burn status added to " + actor);
            }

            // print all current statuses
            System.out.println("Current statuses on " + actor + ": " + actor.statuses());

        } else {
            System.out.println("No actor on fire tile.");
        }

        turnsRemaining--;
        if (turnsRemaining <= 0) {
            location.setGround(new Dirt());
            System.out.println("Fire extinguished.");
        }
    }
}