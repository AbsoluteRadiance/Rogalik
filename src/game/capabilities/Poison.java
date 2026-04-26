package game.capabilities;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that deals 1 damage per turn for a set number of turns.
 * Cannot be removed early — the consumer must wait it out.
 * Adapted from the Burning status in edu.monash.fit2099.demo.mars.capabilities.
 */
public class Poison implements Status {

    private int duration;

    /**
     * Constructor.
     *
     * @param duration number of turns the poison lasts
     */
    public Poison(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity entity, Location location) {
        if (entity instanceof Actor actor) {
            actor.hurt(1);
        }
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}