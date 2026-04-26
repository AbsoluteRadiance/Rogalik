package game.utils;

/**
 * Enum representing abilities that game entities can possess.
 * Modelled after {@code DemoAbilities} in
 * {@code edu.monash.fit2099.demo.mars}.
 */
public enum GameAbilities {
    /** Granted by carrying a {@link game.items.SterilisationBox}. */
    HAS_STERILISATION,
    /** Granted to all {@link game.actors.ContractedWorker} instances.
     *  Used by {@link game.behaviours.AttackBehaviour} to identify valid targets. */
    IS_WORKER
}