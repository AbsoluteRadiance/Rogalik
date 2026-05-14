package game.tests;

import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actions.TeleportAction;
import game.actions.UnlockDoorAction;
import game.actors.BasicInventory;
import game.actors.ContractedWorker;
import game.actors.Undead;
import game.actors.WeightLimitedInventory;
import game.grounds.*;
import game.grounds.doors.AluminiumDoor;
import game.grounds.doors.IronDoor;
import game.grounds.doors.TitaniumDoor;
import game.items.AccessCard;
import game.items.AlienCube;
import game.items.Flask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ2: teleportation devices and tiered door system.
 *
 * <p>GameMap.actorLocations is injected by World.addGameMap(), so every test
 * that places actors must register the map with a World first.</p>
 */
class UnitTests {

    /** Minimal World subclass — overrides stillRunning() so run() is never needed. */
    private static class TestWorld extends World {
        TestWorld() {
            super(new Display() {
                @Override public void println(String s) {}
                @Override public void print(String s) {}
                @Override public void endLine() {}
            });
        }
        @Override protected boolean stillRunning() { return false; }
    }

    private TestWorld world;
    private GameMap map;
    private ContractedWorker worker;

    @BeforeEach
    void setUp() throws Exception {
        DefaultGroundCreator gc = new DefaultGroundCreator();
        gc.registerGround('_', Floor::new);
        gc.registerGround('#', Wall::new);
        gc.registerGround('.', Dirt::new);
        gc.registerGround('=', AluminiumDoor::new);
        gc.registerGround('N', IronDoor::new);
        gc.registerGround('M', TitaniumDoor::new);
        gc.registerGround('≈', ToxicWaste::new);

        List<String> layout = Arrays.asList(
                "_____",
                "_____",
                "_____",
                "_____",
                "_____"
        );

        world = new TestWorld();
        map = new GameMap("Test Map", gc, layout);
        world.addGameMap(map);  // injects actorLocations — must happen before addActor

        worker = new ContractedWorker("Tester", '@', 20, new WeightLimitedInventory(50));
        world.addPlayer(worker, map.at(2, 2));
    }

    // ── TeleportAction ──────────────────────────────────────────────────────

    /** Core contract: actor ends up at the destination. */
    @Test
    void teleportAction_movesActorToDestination() {
        Location dest = map.at(4, 4);
        new TeleportAction(dest, "go", (a, d, s) -> {}).execute(worker, map);
        assertEquals(dest, map.locationOf(worker));
    }

    /** Effect receives the pre-move location as source, not the destination. */
    @Test
    void teleportAction_effectReceivesPreMoveSourceLocation() {
        Location expectedSource = map.at(2, 2);
        Location[] captured = {null};
        new TeleportAction(map.at(0, 0), "go", (a, d, s) -> captured[0] = s)
                .execute(worker, map);
        assertEquals(expectedSource, captured[0]);
    }

    /** Effect is still called when source == destination (degenerate teleport). */
    @Test
    void teleportAction_effectFires_whenSourceEqualsDestination() {
        boolean[] fired = {false};
        Location here = map.at(2, 2);
        new TeleportAction(here, "stay", (a, d, s) -> fired[0] = true)
                .execute(worker, map);
        assertTrue(fired[0]);
    }

    // ── AlienCube ───────────────────────────────────────────────────────────

    /** Exactly 3 actions offered — no more, no less. */
    @Test
    void alienCube_offersExactlyThreeTeleportActions() {
        AlienCube cube = new AlienCube();
        long count = cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertEquals(3, count);
    }

    /** All 3 destinations are distinct locations. */
    @Test
    void alienCube_threeDestinationsAreDistinct() {
        AlienCube cube = new AlienCube();
        List<String> labels = cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .map(a -> a.menuDescription(worker))
                .toList();
        assertEquals(3, labels.stream().distinct().count(),
                "All three offered destinations should be different");
    }

    /** Using the cube corrupts every adjacent tile at source to ToxicWaste. */
    @Test
    void alienCube_corruptsAllAdjacentSourceTiles() {
        Location source = map.at(2, 2);
        AlienCube cube = new AlienCube();

        cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        source.getExits().forEach(exit ->
                assertInstanceOf(ToxicWaste.class, exit.getDestination().getGround(),
                        "Exit " + exit.getName() + " from source should be ToxicWaste")
        );
    }

    /** Non-adjacent tiles are NOT corrupted — only immediate neighbours. */
    @Test
    void alienCube_doesNotCorruptNonAdjacentTiles() {
        Location source = map.at(2, 2);
        AlienCube cube = new AlienCube();
        cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        // (0,0) is not adjacent to (2,2)
        assertFalse(map.at(0, 0).getGround() instanceof ToxicWaste,
                "Non-adjacent tile should not be corrupted");
    }

    /** onSell spawns exactly one Undead adjacent to the worker. */
    @Test
    void alienCube_onSell_spawnsOneUndeadAdjacent() {
        Location workerLoc = map.locationOf(worker);
        new AlienCube().onSell(worker, workerLoc);

        long undeadCount = workerLoc.getExits().stream()
                .map(exit -> exit.getDestination().getActor())
                .filter(a -> a instanceof Undead)
                .count();

        assertEquals(1, undeadCount);
    }

    /** onSell does not crash when all adjacent tiles are occupied. */
    @Test
    void alienCube_onSell_doesNotCrash_whenNoAdjacentTileAvailable() throws Exception {
        // Fill every adjacent tile with a worker
        Location center = map.locationOf(worker);
        int i = 0;
        for (var exit : center.getExits()) {
            Location adj = exit.getDestination();
            if (!adj.containsAnActor()) {
                ContractedWorker blocker = new ContractedWorker(
                        "B" + i++, 'x', 5, new BasicInventory());
                world.addPlayer(blocker, adj);
            }
        }
        assertDoesNotThrow(() -> new AlienCube().onSell(worker, center));
    }

    /** ToxicWaste deals exactly 1 damage per tick to a standing actor. */
    @Test
    void toxicWaste_dealsDamageEachTick() {
        map.at(1, 1).setGround(new ToxicWaste());
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);
        // Manually tick the location (simulates one game turn)
        map.at(1, 1).setGround(new ToxicWaste());
        // Move worker onto waste, then tick
        map.moveActor(worker, map.at(1, 1));
        new ToxicWaste().tick(map.at(1, 1)); // call tick directly with actor present
        // Worker is at (1,1); tick the location properly
        map.at(1, 1).tick();
        assertEquals(hpBefore - 1, worker.getStatistic(ActorStatistics.HEALTH));
    }

    // ── MagicCircle ─────────────────────────────────────────────────────────

    /** When only one circle exists, no action is offered (nowhere to go). */
    @Test
    void magicCircle_offersNoAction_whenAloneOnMap() {
        MagicCircle circle = new MagicCircle();
        map.at(0, 0).setGround(circle);
        long count = circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().size();
        assertEquals(0, count);
    }

    /** With two circles, one action is offered pointing to the other circle. */
    @Test
    void magicCircle_offersOneAction_whenAnotherCircleExists() {
        map.at(0, 0).setGround(new MagicCircle());
        map.at(4, 4).setGround(new MagicCircle());
        MagicCircle circle = (MagicCircle) map.at(0, 0).getGround();

        long count = circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertEquals(1, count);
    }

    /** MagicCircle arrival spawns a Flask on an adjacent empty tile. */
    @Test
    void magicCircle_spawnsFaskOnArrival() throws Exception {
        map.at(0, 0).setGround(new MagicCircle());
        Location dest = map.at(4, 4);
        dest.setGround(new MagicCircle());
        map.moveActor(worker, map.at(0, 0));

        MagicCircle circle = (MagicCircle) map.at(0, 0).getGround();
        circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        boolean flaskFound = dest.getExits().stream()
                .anyMatch(e -> e.getDestination().getItems().stream()
                        .anyMatch(item -> item instanceof Flask));
        assertTrue(flaskFound, "A Flask should appear adjacent to destination circle");
    }

    // ── Tiered Doors ────────────────────────────────────────────────────────

    /** AluminiumDoor: unlocking with Level 1 card deals exactly 2 damage. */
    @Test
    void aluminiumDoor_unlock_dealsTwoDamage() {
        map.at(3, 2).setGround(new AluminiumDoor());
        worker.getInventory().add(new AccessCard(1));
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertEquals(hpBefore - 2, worker.getStatistic(ActorStatistics.HEALTH));
    }

    /** AluminiumDoor: door is passable after unlock. */
    @Test
    void aluminiumDoor_isPassable_afterUnlock() {
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue(map.at(3, 2).canActorEnter(worker));
    }

    /** TitaniumDoor: unlock heals worker exactly 5 HP. */
    @Test
    void titaniumDoor_unlock_healsFiveHp() {
        worker.hurt(10);
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);
        map.at(3, 2).setGround(new TitaniumDoor());
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertEquals(hpBefore + TitaniumDoor.HEAL_AMOUNT,
                worker.getStatistic(ActorStatistics.HEALTH));
    }

    /** Level 3 card can open an AluminiumDoor (higher clearance satisfies lower requirement). */
    @Test
    void accessCard_level3_canOpenAluminiumDoor() {
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue(door.isUnlocked(), "Level 3 card should open a Level 1 door");
    }

    /** Level 1 card cannot open IronDoor. */
    @Test
    void accessCard_level1_cannotOpenIronDoor() {
        IronDoor door = new IronDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse(door.isUnlocked());
    }

    /** Level 2 card cannot open TitaniumDoor. */
    @Test
    void accessCard_level2_cannotOpenTitaniumDoor() {
        TitaniumDoor door = new TitaniumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(2));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse(door.isUnlocked());
    }

    /** Worker with no card cannot open any door. */
    @Test
    void noCard_cannotOpenAnyDoor() {
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);
        // No card added to inventory

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse(door.isUnlocked());
    }

    /** IronDoor unlock spawns Fire on adjacent Floor tiles. */
    @Test
    void ironDoor_unlock_spawnsFireOnAdjacentFloorTiles() {
        map.at(3, 2).setGround(new IronDoor());
        worker.getInventory().add(new AccessCard(2));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        boolean fireFound = map.at(3, 2).getExits().stream()
                .anyMatch(e -> e.getDestination().getGround() instanceof Fire);
        assertTrue(fireFound, "Fire should appear on adjacent tiles after IronDoor unlock");
    }
}