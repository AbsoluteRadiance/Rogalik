package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.FirstAidKit;

/**
 * An action to use the First Aid Kit, permanently increasing the worker's
 * maximum health by 1 and restoring health to full.
 */
public class UseFirstAidKitAction extends Action {

    private final FirstAidKit firstAidKit;

    /**
     * Constructor.
     *
     * @param firstAidKit the kit to use
     */
    public UseFirstAidKitAction(FirstAidKit firstAidKit) {
        this.firstAidKit = firstAidKit;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        return firstAidKit.use(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " uses the First Aid Kit (increases max HP by 1, restores full health)";
    }
}