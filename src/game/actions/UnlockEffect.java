package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A functional interface representing the side effect that fires immediately
 * after a door is unlocked.
 *
 * <p>Each door type injects its own implementation when the door is queried,
 * keeping all door-specific logic inside the door class itself (SRP, OCP).
 * {@link UnlockDoorAction} calls this without knowing the concrete door type,
 * eliminating all {@code instanceof} and downcast usage.</p>
 *
 * <p>Examples of effects:
 * <ul>
 *   <li>Aluminium door: electrical shock to the actor</li>
 *   <li>Iron door: fire spawned on adjacent tiles</li>
 *   <li>Titanium door: decontamination heal</li>
 * </ul>
 * </p>
 */
@FunctionalInterface
public interface UnlockEffect {

    /**
     * Applies the post-unlock side effect.
     *
     * @param actor        the actor who unlocked the door
     * @param doorLocation the location of the door that was unlocked
     * @return a human-readable description of the effect
     */
    String apply(Actor actor, Location doorLocation);
}