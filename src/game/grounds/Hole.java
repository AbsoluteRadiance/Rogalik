package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Creature;
import game.actors.Slime;
import game.actors.Undead;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Represents a hole in the Moon facility's floor.
 * Every 20 turns, the hole has an equal chance to
 * spawn either an Undead or a Slime.
 * Holes are impassable and cannot be entered by any actor.
 */
public class Hole extends Ground {
    private static final int SPAWN_INTERVAL = 20;
    private int turnCounter;
    private final Random random = new Random();
    private final List<Supplier<Creature>> spawnables  = new ArrayList<>();

    /**
     * Constructor for Hole.
     * Initialises the hole with a turn counter of 0,
     * So it must wait the full 20 turns before spawning any creature.
     */
    public Hole() {
        super('o', "Hole");
        this.turnCounter = 0;
        spawnables.add(Undead::new);
        spawnables.add(Slime::new);
    }

    /**
     * Called once per turn, increments the turn counter and spawns a creature.
     * Has an equal chance of spawning either an Undead or a Slime.
     * Contains a failsafe to not spawn anything if an entity somehow occupies the hole.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        turnCounter++;
        if (turnCounter >= SPAWN_INTERVAL) {
            turnCounter = 0;
            if (!location.containsAnActor()) {
                try {
                    Creature creature = spawnables.get(random.nextInt(spawnables.size())).get();
                    location.addActor(creature);
                } catch (GameEngineException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Holes are impassable and cannot be entered by any actors.
     *
     * @param actor the Actor to check
     * @return false always; actors can never enter
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}