package game;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.grounds.*;
import game.items.*;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the miracle of creation, translating a bunch of periods
 * and hashtags into a sprawling, functional sci-fi facility.
 * The company's armoured ship (Walls, Floors, Door) is where the workers begin.
 * Three unique items are placed in the ship — one of each — forcing the workers
 * to divide responsibilities among themselves.
 */
public class EclipseNebula extends World {

    public EclipseNebula(Display display) {
        super(display);
    }

    /**
     * Initialise maps, actors, items, and grounds of the game world.
     * The three shared items (AccessCard, FirstAidKit, SterilisationBox) are
     * each instantiated exactly once and placed in the armoured ship for the
     * workers to pick up and share responsibilities.
     * @throws Exception in case if anything goes wrong...
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('=', Door::new);
        groundCreator.registerGround('o', Dirt::new);

        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######..........#__________________#___________________#",
                "...#_____#..........=__________________=___________________#",
                "...#_____=...~......#__________________#___________________#",
                "...#_____#..~~~.....########=#####=#####___#############___#",
                "...#######.~~~~.....#______#_#_________#___#___________#___#",
                ".........~~~~.......#______#_#_________#####___________#####",
                "....................#______=_#_________#___________________#",
                "......~.............#______#_#_________#___________________#",
                ".....~~~............#______#_###########___#############___#",
                ".....~..............#______#___________#___#___________#___#",
                "....................=______#___________=___=___________=___#",
                "....................#______#############___#############___#",
                ".........~~~~.......#______#___________#####################",
                "........~~~~~~......#______#___________=___________________#",
                ".........~~~~.......#______#___________#___________________#",
                "....................#______#############___#############___#",
                "....................#______#___________#___#___________#___#",
                "..~.................#______=___________=___=___________=___#",
                "....................########################################"
        );

        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        this.addGameMap(moon99DeprecatedMap);

        // --- Single instances of the three shared items ---
        moon99DeprecatedMap.at(5, 2).addItem(new AccessCard());
        moon99DeprecatedMap.at(6, 2).addItem(new FirstAidKit());
        moon99DeprecatedMap.at(7, 2).addItem(new SterilisationBox());

        // --- Workers: each created with their own WeightLimitedInventory and a Flask ---
        ContractedWorker contractedWorker1 = new ContractedWorker("#1 Bob", 'ඞ', 10);
        ContractedWorker contractedWorker2 = new ContractedWorker("#2 Tom", 'ඞ', 10);
        ContractedWorker contractedWorker3 = new ContractedWorker("#3 Sarah", 'ඞ', 10);
        ContractedWorker contractedWorker4 = new ContractedWorker("#4 Julie", 'ඞ', 10);
        ContractedWorker contractedWorker5 = new ContractedWorker("#5 Rick", 'ඞ', 10);

        // Place workers inside the armoured ship
        this.addPlayer(contractedWorker1, moon99DeprecatedMap.at(4, 2));
        this.addPlayer(contractedWorker2, moon99DeprecatedMap.at(4, 3));
        this.addPlayer(contractedWorker3, moon99DeprecatedMap.at(4, 4));
        this.addPlayer(contractedWorker4, moon99DeprecatedMap.at(5, 3));
        this.addPlayer(contractedWorker5, moon99DeprecatedMap.at(5, 4));

        // --- Items: Placed at o in teh map
        moon99DeprecatedMap.at(37, 1).addItem(new Apple());
        moon99DeprecatedMap.at(21, 11).addItem(new Cookies());
        moon99DeprecatedMap.at(32, 17).addItem(new Lantern());
        moon99DeprecatedMap.at(45, 17).addItem(new FloppyDisk());
        moon99DeprecatedMap.at(21, 11).addItem(new CRTMonitor());
    }
}