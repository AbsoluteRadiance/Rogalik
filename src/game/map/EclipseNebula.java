package game.map;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.WeightLimitedInventory;
import game.actors.ContractedWorker;
import game.grounds.*;
import game.items.*;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the miracle of creation, translating a bunch of periods
 * and hashtags into a sprawling, functional sci-fi facility.
 */
public class EclipseNebula extends World {

    /**
     * Constructor for EclipseNebula.
     *
     * @param display the display object used for rendering the game world
     */
    public EclipseNebula(Display display) {
        super(display);
    }

    /**
     * Initialise maps, actors, items, and grounds of the game world.
     * Additionally creates the armoured ship and moon facility maps,
     * registers all ground types,
     * places items at starting locations,
     * and spawns all ContractedWorkers (players) with individual inventories and items.
     * @throws Exception in case if anything goes wrong...
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('=', Door::new);
        groundCreator.registerGround('o', Hole::new);

        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######..........#__________________#________________o__#",
                "...#_____#..........=__________________=___________________#",
                "...#_____=...~......#__________________#___________________#",
                "...#_____#..~~~.....########=#####=#####___#############___#",
                "...#######.~~~~.....#______#_#_________#___#___________#___#",
                ".........~~~~.......#______#_#_________#####___________#####",
                "....................#______=_#_________#___________________#",
                "......~.............#______#_#_________#___________________#",
                ".....~~~............#______#_###########___#############___#",
                ".....~..............#______#___________#___#___________#___#",
                "....................=______#___________=___=_____o_____=___#",
                "....................#______#############___#############___#",
                ".........~~~~.......#______#___________#####################",
                "........~~~~~~......#______#___________=___________________#",
                ".........~~~~.......#______#___________#___________________#",
                "....................#______#############___#############___#",
                "....................#______#_____o_____#___#___________#_o_#",
                "..~.................#______=___________=___=___________=___#",
                "....................########################################"
        );

        // ship map
        List<String> shipMap = Arrays.asList(
                "#######",
                "#_____#",
                "#_____=",
                "#_____#",
                "#######"
        );

        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        GameMap armouredShipMap = new GameMap("Armoured Ship", groundCreator, shipMap);
        this.addGameMap(moon99DeprecatedMap);
        this.addGameMap(armouredShipMap);

        // placing items
        moon99DeprecatedMap.at(5, 3).addItem(new FloppyDisk());
        moon99DeprecatedMap.at(22, 7).addItem(new CrtMonitor());
        moon99DeprecatedMap.at(23, 11).addItem(new Apple());
        moon99DeprecatedMap.at(24, 15).addItem(new Cookies());
        moon99DeprecatedMap.at(25, 18).addItem(new Lantern());
        moon99DeprecatedMap.at(25, 19).addItem(new AlarmTrigger());

        // BEHOLD, LOCAL MULTIPLAYER!!!
        ContractedWorker contractedWorker1 = new ContractedWorker("#1 Bob", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker contractedWorker2 = new ContractedWorker("#2 Tom", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker contractedWorker3 = new ContractedWorker("#3 Sarah", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker contractedWorker4 = new ContractedWorker("#4 Julie", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker contractedWorker5 = new ContractedWorker("#5 Rick", 'ඞ', 10, new WeightLimitedInventory(50));

        contractedWorker1.getInventory().add(new Flask());
        contractedWorker1.getInventory().add(new AccessCard());
        contractedWorker2.getInventory().add(new Flask());
        contractedWorker3.getInventory().add(new Flask());
        contractedWorker4.getInventory().add(new Flask());
        contractedWorker5.getInventory().add(new Flask());

        this.addPlayer(contractedWorker1, moon99DeprecatedMap.at(2, 1));
        this.addPlayer(contractedWorker2, moon99DeprecatedMap.at(3, 1));
        this.addPlayer(contractedWorker3, moon99DeprecatedMap.at(4, 1));
        this.addPlayer(contractedWorker4, moon99DeprecatedMap.at(2, 2));
        this.addPlayer(contractedWorker5, moon99DeprecatedMap.at(3, 2));
    }
}
