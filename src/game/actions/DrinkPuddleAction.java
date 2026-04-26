package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.Poison;
import game.utils.GameAbilities;

/**
 * An action allowing a worker to drink from a puddle.
 * Without a SterilisationBox: poisons the worker (1 damage/turn, 3 turns).
 * With a SterilisationBox: heals the worker for 1 HP.
 */
public class DrinkPuddleAction extends Action {

    private static final int POISON_DURATION = 3;
    private static final int HEAL_AMOUNT = 1;

    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor.hasAbility(GameAbilities.HAS_STERILISATION)) {
            actor.heal(HEAL_AMOUNT);
            return actor + " drinks the purified water and heals " + HEAL_AMOUNT + " HP.";
        }
        actor.addStatus(new Poison(POISON_DURATION));
        return actor + " drinks the toxic water and is poisoned for " + POISON_DURATION + " turns!";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " drinks from the puddle";
    }
}