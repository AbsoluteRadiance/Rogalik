package game.capabilities;

/**
 * Interface for entities that can receive burn damage.
 * Implementing this interface allows the entity to be targeted by
 * the {@link Burn} status effect.
 *
 * <p>Modelled after {@code Flammable} in
 * {@code edu.monash.fit2099.demo.mars.capabilities}.
 *
 * @author Adrian Kristanto
 */
public interface Flammable {

    /**
     * Called each turn by {@link Burn} to deal burn damage to this entity.
     *
     * @param damage the amount of damage to apply
     */
    void burn(int damage);
}