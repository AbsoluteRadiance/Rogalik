package game.map;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.ContractedWorker;
import game.actors.WeightLimitedInventory;
import game.grounds.doors.AluminiumDoor;
import game.grounds.Dirt;
import game.grounds.Floor;
import game.grounds.Hole;
import game.grounds.doors.IronDoor;
import game.grounds.MagicCircle;
import game.grounds.Puddle;
import game.grounds.Supercomputer;
import game.grounds.TeleportationTube;
import game.grounds.doors.TitaniumDoor;
import game.grounds.ToxicWaste;
import game.grounds.Wall;
import game.items.AccessCard;
import game.items.AlarmTrigger;
import game.items.AlienCube;
import game.items.Apple;
import game.items.Cookies;
import game.items.CrtMonitor;
import game.items.Flask;
import game.items.FloppyDisk;
import game.items.Lantern;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the entire Eclipse Nebula game world.
 *
 * <p>Constructs and wires together all maps, actors, items, and grounds.
 * Contains three maps:</p>
 * <ul>
 *   <li><b>99-Deprecated</b>: the original moon facility with aluminium doors.</li>
 *   <li><b>Armoured Ship</b>: safe zone with {@link Supercomputer} ({@code ≡})
 *       and a {@link TeleportationTube} ({@code Φ}).</li>
 *   <li><b>20-overflow</b>: a flooded factory complex with {@link ToxicWaste},
 *       tiered doors ({@link IronDoor}, {@link TitaniumDoor}), {@link AlienCube}
 *       items, and {@link MagicCircle} grounds.</li>
 * </ul>
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
     * Initialises all maps, registers ground types, places items, and spawns workers.
     *
     * @throws Exception if any ground registration or actor placement fails
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);
        // Tiered doors
        groundCreator.registerGround('=', AluminiumDoor::new);
        groundCreator.registerGround('N', IronDoor::new);
        groundCreator.registerGround('M', TitaniumDoor::new);
        groundCreator.registerGround('o', Hole::new);
        groundCreator.registerGround('≡', Supercomputer::new);
        groundCreator.registerGround('≈', ToxicWaste::new);
        groundCreator.registerGround('Φ', TeleportationTube::new);
        groundCreator.registerGround('◎', MagicCircle::new);
        // ◈ AlienCube: item placed via addItem; use Floor as the underlying ground
        groundCreator.registerGround('◈', Floor::new);

        //  99-Deprecated 
        // Φ placed at col 4, row 3 inside the small ship alcove (top-left structure)
        List<String> moon99Deprecated = Arrays.asList(
                "....................########################################",
                "...#######..........#__________________#________________o__#",
                "...#_____#..........=__________________=___________________#",
                "...#__Φ__=...~......#__________________#___________________#",
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

        //  Armoured Ship 
        // Φ at col 2, row 2 inside the ship
        List<String> shipMap = Arrays.asList(
                "#######",
                "#≡____#",
                "#_Φ___=",
                "#_____#",
                "#######"
        );

        //  20-overflow 
        // ◎ = MagicCircle  ◈ = AlienCube drop position (Floor underneath)
        // N = IronDoor  M = TitaniumDoor  = = AluminiumDoor
        List<String> overflow20 = Arrays.asList(
                "......................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈............≈≈≈≈≈≈≈≈≈≈≈≈≈≈##################≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈............≈≈≈≈≈≈≈≈≈≈≈≈≈≈#________________#≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈............≈≈≈≈≈≈≈≈#######_______◈________#≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈............≈≈≈≈≈≈≈≈#_____N________________#≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_◎___###########N######≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_____#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#########N#####≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#_____________#≈≈≈≈≈≈≈≈≈#___◎__#≈≈≈≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈#______o______#≈≈≈≈≈≈≈≈≈#______#≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈######M########≈≈≈≈≈≈≈≈≈####M###≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈≈≈≈≈",
                "...≈≈≈≈≈≈≈≈≈.≈≈≈≈≈≈≈≈≈≈≈≈≈#_#≈≈≈≈≈###############_#######≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈#_____________________________#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#_______M__________◈__≈≈≈≈____#≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈#___◎___#_____________≈≈≈≈≈≈__≈≈≈≈",
                "....≈≈≈≈≈≈...≈≈≈≈≈≈≈≈≈≈≈≈≈######################≈≈≈≈≈≈≈≈≈≈≈≈",
                ".............≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                "......................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈",
                "......................≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈"
        );

        //  Create maps 
        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        GameMap armouredShipMap = new GameMap("Armoured Ship", groundCreator, shipMap);
        GameMap overflow20Map = new GameMap("20-overflow", groundCreator, overflow20);

        this.addGameMap(moon99DeprecatedMap);
        this.addGameMap(armouredShipMap);
        this.addGameMap(overflow20Map);

        //  Wire teleportation tubes 
        // Tube in 99-Deprecated at (4, 3)
        wireTube(moon99DeprecatedMap, 4, 3, armouredShipMap, "Armoured Ship", overflow20Map, "20-overflow");
        // Tube in Armoured Ship at (2, 2)
        wireTube(armouredShipMap, 2, 2, moon99DeprecatedMap, "99-Deprecated", overflow20Map, "20-overflow");

        //  Place Alien Cubes (items) at ◈ positions in 20-overflow 
        // Row 3 ◈ at approximately col 20
        overflow20Map.at(20, 3).addItem(new AlienCube());
        // Row 14 ◈ at approximately col 21
        overflow20Map.at(21, 14).addItem(new AlienCube());

        //  Place items on 99-Deprecated 
        moon99DeprecatedMap.at(5, 3).addItem(new FloppyDisk());
        moon99DeprecatedMap.at(22, 7).addItem(new CrtMonitor());
        moon99DeprecatedMap.at(23, 11).addItem(new Apple());
        moon99DeprecatedMap.at(24, 15).addItem(new Cookies());
        moon99DeprecatedMap.at(25, 18).addItem(new Lantern());
        moon99DeprecatedMap.at(25, 19).addItem(new AlarmTrigger());

        //  Spawn contracted workers 
        ContractedWorker w1 = new ContractedWorker("#1 Bob", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker w2 = new ContractedWorker("#2 Tom", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker w3 = new ContractedWorker("#3 Sarah", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker w4 = new ContractedWorker("#4 Julie", 'ඞ', 10, new WeightLimitedInventory(50));
        ContractedWorker w5 = new ContractedWorker("#5 Rick", 'ඞ', 10, new WeightLimitedInventory(50));

        w1.getInventory().add(new Flask());
        w1.getInventory().add(new AccessCard(1));
        w2.getInventory().add(new Flask());
        w3.getInventory().add(new Flask());
        w4.getInventory().add(new Flask());
        w5.getInventory().add(new Flask());

        this.addPlayer(w1, moon99DeprecatedMap.at(2, 1));
        this.addPlayer(w2, moon99DeprecatedMap.at(3, 1));
        this.addPlayer(w3, moon99DeprecatedMap.at(4, 1));
        this.addPlayer(w4, moon99DeprecatedMap.at(2, 2));
        this.addPlayer(w5, moon99DeprecatedMap.at(3, 2));
    }

    /**
     * Retrieves the {@link TeleportationTube} at the specified coordinates and
     * registers two cross-map destinations on it.
     *
     * @param sourceMap  map containing the tube
     * @param x          x coordinate of the tube
     * @param y          y coordinate of the tube
     * @param destMapA   first destination map
     * @param labelA     display label for destination A
     * @param destMapB   second destination map
     * @param labelB     display label for destination B
     */
    private void wireTube(GameMap sourceMap, int x, int y,
                          GameMap destMapA, String labelA,
                          GameMap destMapB, String labelB) {
        TeleportationTube tube = sourceMap.at(x, y).getGroundAs(TeleportationTube.class);
        if (tube == null) {
            return;
        }
        tube.addDestination(firstFloor(destMapA), labelA);
        tube.addDestination(firstFloor(destMapB), labelB);
    }

    /**
     * Returns the first passable {@link Floor} tile found in the given map.
     * Falls back to coordinate (1, 1) if no floor tile is unoccupied.
     *
     * @param map the map to search
     * @return a passable floor location
     */
    private Location firstFloor(GameMap map) {
        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location loc = map.at(x, y);
                if (loc.getGround() instanceof Floor && !loc.containsAnActor()) {
                    return loc;
                }
            }
        }
        return map.at(1, 1);
    }
}