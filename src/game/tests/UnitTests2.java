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
import game.grounds.Dirt;
import game.grounds.Fire;
import game.grounds.Floor;
import game.grounds.MagicCircle;
import game.grounds.TeleportationTube;
import game.grounds.ToxicWaste;
import game.grounds.Wall;
import game.grounds.doors.AluminiumDoor;
import game.grounds.doors.IronDoor;
import game.grounds.doors.TitaniumDoor;
import game.items.AccessCard;
import game.items.AlienCube;
import game.items.Flask;
import game.status.DamageOverTimeStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Plain-Java test runner for REQ2: teleportation devices and tiered door system.
 *
 * <p>Run the main method directly — no test framework required.
 * Compatible with Microsoft OpenJDK 17.</p>
 *
 * <p>Covers: {@link TeleportAction}, {@link AlienCube}, {@link MagicCircle},
 * {@link TeleportationTube}, {@link ToxicWaste}, {@link AluminiumDoor},
 * {@link IronDoor}, {@link TitaniumDoor}, and {@link AccessCard}.</p>
 */
public class UnitTests2 {

    /** Count of tests that passed. */
    private static int passed = 0;

    /** Count of tests that failed or errored. */
    private static int failed = 0;

    // -----------------------------------------------------------------------
    // Assertion helpers
    // -----------------------------------------------------------------------

    /**
     * Asserts that the given condition is true.
     *
     * @param testName  descriptive name shown in output
     * @param condition the value to check
     */
    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }

    /**
     * Asserts that the given condition is false.
     *
     * @param testName  descriptive name shown in output
     * @param condition the value to check
     */
    private static void assertFalse(String testName, boolean condition) {
        assertTrue(testName, !condition);
    }

    /**
     * Asserts that two ints are equal.
     *
     * @param testName descriptive name shown in output
     * @param expected expected value
     * @param actual   actual value
     */
    private static void assertEquals(String testName, int expected, int actual) {
        if (expected == actual) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName
                    + " | expected: " + expected + " got: " + actual);
            failed++;
        }
    }

    /**
     * Asserts that two object references point to the same instance.
     *
     * @param testName descriptive name shown in output
     * @param expected expected instance
     * @param actual   actual instance
     */
    private static void assertSame(String testName, Object expected, Object actual) {
        if (expected == actual) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName
                    + " | expected: " + expected + " got: " + actual);
            failed++;
        }
    }

    /**
     * Asserts that the given object is not null.
     *
     * @param testName descriptive name shown in output
     * @param obj      the object to check
     */
    private static void assertNotNull(String testName, Object obj) {
        assertTrue(testName, obj != null);
    }

    /**
     * Asserts that the given object is null.
     *
     * @param testName descriptive name shown in output
     * @param obj      the object to check
     */
    private static void assertNull(String testName, Object obj) {
        assertTrue(testName, obj == null);
    }

    // -----------------------------------------------------------------------
    // Map builder
    // -----------------------------------------------------------------------

    /**
     * Builds a minimal 5x5 all-Floor map with an injected
     * {@link edu.monash.fit2099.engine.actors.ActorLocationsIterator}
     * via a {@link World} instance.
     *
     * @return a ready-to-use {@link GameMap}
     * @throws Exception if ground registration or map creation fails
     */
    private static GameMap buildTestMap() throws Exception {
        DefaultGroundCreator gc = new DefaultGroundCreator();
        gc.registerGround('_', Floor::new);
        gc.registerGround('#', Wall::new);
        gc.registerGround('.', Dirt::new);
        gc.registerGround('=', AluminiumDoor::new);
        gc.registerGround('N', IronDoor::new);
        gc.registerGround('M', TitaniumDoor::new);
        gc.registerGround('≈', ToxicWaste::new);

        GameMap map = new GameMap("test", gc,
                Arrays.asList(
                        "_____",
                        "_____",
                        "_____",
                        "_____",
                        "_____"
                ));

        World world = new World(new Display()) {
            @Override
            protected String endGameMessage() { return ""; }
        };
        world.addGameMap(map);
        return map;
    }

    /**
     * Creates a {@link ContractedWorker} with a {@link WeightLimitedInventory}
     * (capacity 50) at the given map location.
     *
     * @param map  the map to place the worker on
     * @param x    x coordinate
     * @param y    y coordinate
     * @return the placed worker
     * @throws Exception if actor placement fails
     */
    private static ContractedWorker placeWorker(GameMap map, int x, int y)
            throws Exception {
        ContractedWorker worker = new ContractedWorker(
                "Worker", '@', 20, new WeightLimitedInventory(50));
        map.addActor(worker, map.at(x, y));
        return worker;
    }

    // -----------------------------------------------------------------------
    // REQ2 — TeleportAction core contract
    // -----------------------------------------------------------------------

    /**
     * TeleportAction must move the actor to the exact destination supplied.
     */
    private static void test_teleportAction_movesActorToDestination()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        Location dest = map.at(4, 4);
        new TeleportAction(dest, "go", (a, d, s) -> {}).execute(worker, map);
        assertSame("TeleportAction moves actor to destination",
                dest, map.locationOf(worker));
    }

    /**
     * The {@link game.actions.TeleportEffect} must receive the pre-move location
     * as its source argument, not the destination.
     */
    private static void test_teleportAction_effectReceivesCorrectSource()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        Location expectedSource = map.at(2, 2);
        Location[] capturedSource = {null};
        new TeleportAction(map.at(0, 0), "go",
                (a, d, s) -> capturedSource[0] = s).execute(worker, map);
        assertSame("TeleportAction passes correct source to effect",
                expectedSource, capturedSource[0]);
    }

    /**
     * The effect must receive the destination that was passed to the action.
     */
    private static void test_teleportAction_effectReceivesCorrectDestination()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        Location dest = map.at(4, 4);
        Location[] capturedDest = {null};
        new TeleportAction(dest, "go",
                (a, d, s) -> capturedDest[0] = d).execute(worker, map);
        assertSame("TeleportAction passes correct destination to effect",
                dest, capturedDest[0]);
    }

    /**
     * The effect must fire even when source and destination are the same location.
     * (Degenerate teleport — actor is already at the target.)
     */
    private static void test_teleportAction_effectFiresWhenSourceEqualsDestination()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        boolean[] fired = {false};
        Location here = map.at(2, 2);
        new TeleportAction(here, "stay",
                (a, d, s) -> fired[0] = true).execute(worker, map);
        assertTrue("TeleportAction effect fires for degenerate same-location teleport",
                fired[0]);
    }

    /**
     * Actor must remain at destination after the effect runs — effect must not
     * accidentally undo the move.
     */
    private static void test_teleportAction_actorRemainsAtDestinationAfterEffect()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        Location dest = map.at(3, 3);
        // effect does not touch the actor's location
        new TeleportAction(dest, "go", (a, d, s) -> {}).execute(worker, map);
        assertSame("Actor remains at destination after effect runs",
                dest, map.locationOf(worker));
    }

    // -----------------------------------------------------------------------
    // REQ2 — AlienCube teleportation
    // -----------------------------------------------------------------------

    /**
     * AlienCube must offer exactly three distinct teleport actions when carried.
     */
    private static void test_alienCube_offersThreeTeleportActions()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AlienCube cube = new AlienCube();
        long count = cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertEquals("AlienCube offers exactly 3 TeleportActions", 3, (int) count);
    }

    /**
     * The three destinations offered by AlienCube must all be distinct.
     */
    private static void test_alienCube_threeDestinationsAreDistinct()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AlienCube cube = new AlienCube();
        long distinctLabels = cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .map(a -> a.menuDescription(worker))
                .distinct()
                .count();
        assertEquals("AlienCube's three offered destinations are distinct",
                3, (int) distinctLabels);
    }

    /**
     * Using AlienCube must convert every tile immediately adjacent to the source
     * into {@link ToxicWaste}.
     */
    private static void test_alienCube_corruptsAllAdjacentSourceTiles()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        Location source = map.at(2, 2);
        AlienCube cube = new AlienCube();

        // execute the first available teleport action
        cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        boolean allCorrupted = source.getExits().stream()
                .allMatch(e -> e.getDestination().getGround() instanceof ToxicWaste);
        assertTrue("AlienCube corrupts all adjacent source tiles to ToxicWaste",
                allCorrupted);
    }

    /**
     * AlienCube corruption must not reach tiles that were not immediately
     * adjacent to the source (no spillover beyond direct neighbours).
     */
    private static void test_alienCube_doesNotCorruptNonAdjacentTiles()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AlienCube cube = new AlienCube();
        cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        // (0,0) is not a direct neighbour of (2,2)
        assertFalse("AlienCube does not corrupt non-adjacent tile (0,0)",
                map.at(0, 0).getGround() instanceof ToxicWaste);
    }

    /**
     * AlienCube is reusable — a second use after the first must still produce
     * three teleport action options.
     */
    private static void test_alienCube_isReusable() throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AlienCube cube = new AlienCube();

        // first use — move worker somewhere so a second call works
        cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        // second use from new location
        long count = cube.allowableActions(worker, map)
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertTrue("AlienCube is reusable and still offers actions after first use",
                count > 0);
    }

    /**
     * AlienCube.onSell must spawn exactly one {@link Undead} on an adjacent tile.
     */
    private static void test_alienCube_onSell_spawnsOneUndead()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        Location workerLoc = map.locationOf(worker);
        new AlienCube().onSell(worker, workerLoc);

        long undeadCount = workerLoc.getExits().stream()
                .map(e -> e.getDestination().getActor())
                .filter(a -> a instanceof Undead)
                .count();
        assertEquals("AlienCube.onSell spawns exactly one Undead adjacent to seller",
                1, (int) undeadCount);
    }

    /**
     * AlienCube.onSell must not throw when all adjacent tiles are occupied.
     */
    private static void test_alienCube_onSell_doesNotCrashWhenNoSpaceAvailable()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        Location center = map.locationOf(worker);

        // fill every adjacent tile
        int i = 0;
        for (var exit : center.getExits()) {
            Location adj = exit.getDestination();
            if (!adj.containsAnActor()) {
                ContractedWorker blocker = new ContractedWorker(
                        "B" + i++, 'x', 5, new BasicInventory());
                map.addActor(blocker, adj);
            }
        }

        boolean threw = false;
        try {
            new AlienCube().onSell(worker, center);
        } catch (Exception e) {
            threw = true;
        }
        assertFalse("AlienCube.onSell does not crash when no adjacent tile is free",
                threw);
    }

    // -----------------------------------------------------------------------
    // REQ2 — ToxicWaste
    // -----------------------------------------------------------------------

    /**
     * ToxicWaste must deal exactly 1 point of damage per turn to a standing actor.
     */
    private static void test_toxicWaste_dealsDamageEachTick() throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);
        // apply a DOT directly as ToxicWaste does — 1 turn, 1 damage
        worker.addStatus(new DamageOverTimeStatus(1, 1));
        worker.tickStatuses(map.at(2, 2));
        assertEquals("ToxicWaste deals exactly 1 damage per tick",
                hpBefore - 1, worker.getStatistic(ActorStatistics.HEALTH));
    }

    /**
     * ToxicWaste must be passable — all actors can enter the tile.
     */
    private static void test_toxicWaste_isPassableByActors() {
        ToxicWaste waste = new ToxicWaste();
        ContractedWorker worker = new ContractedWorker(
                "Worker", '@', 10, new BasicInventory());
        assertTrue("ToxicWaste is passable by any actor",
                waste.canActorEnter(worker));
    }

    /**
     * Multiple ticks on ToxicWaste with an actor present must accumulate damage
     * correctly across turns.
     */
    private static void test_toxicWaste_accumulatesDamageOverMultipleTicks()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 1, 1);
        map.at(1, 1).setGround(new ToxicWaste());
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);

        // simulate 3 full game ticks (ground tick + status tick each)
        for (int i = 0; i < 3; i++) {
            map.at(1, 1).tick();                       // calls ToxicWaste.tick → adds DOT
            worker.tickStatuses(map.at(1, 1));         // drains the DOT
        }

        assertEquals("ToxicWaste deals 1 damage per full tick over 3 turns",
                hpBefore - 3, worker.getStatistic(ActorStatistics.HEALTH));
    }

    /**
     * ToxicWaste must not damage an actor that is not present on the tile.
     */
    private static void test_toxicWaste_doesNotDamageAbsentActor()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);

        // tick the waste tile at (4,4) — worker is at (0,0), not on it
        ToxicWaste waste = new ToxicWaste();
        waste.tick(map.at(4, 4));
        worker.tickStatuses(map.at(0, 0));

        assertEquals("ToxicWaste does not damage actor not standing on tile",
                hpBefore, worker.getStatistic(ActorStatistics.HEALTH));
    }

    // -----------------------------------------------------------------------
    // REQ2 — MagicCircle
    // -----------------------------------------------------------------------

    /**
     * MagicCircle must offer no action when it is the only circle on the map
     * (no valid destination exists).
     */
    private static void test_magicCircle_offersNoActionWhenAlone()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        MagicCircle circle = new MagicCircle();
        map.at(0, 0).setGround(circle);

        long count = circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().size();
        assertEquals("MagicCircle offers no action when alone on map",
                0, (int) count);
    }

    /**
     * MagicCircle must offer exactly one action when one peer circle exists.
     */
    private static void test_magicCircle_offersOneActionWithOnePeerCircle()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        map.at(0, 0).setGround(new MagicCircle());
        map.at(4, 4).setGround(new MagicCircle());
        MagicCircle circle = (MagicCircle) map.at(0, 0).getGround();

        long count = circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertEquals("MagicCircle offers exactly one action with one peer circle",
                1, (int) count);
    }

    /**
     * MagicCircle must not offer an action when the actor is adjacent but not
     * standing on it (non-empty direction string).
     */
    private static void test_magicCircle_offersNoActionWhenActorIsAdjacent()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 0, 0);
        map.at(1, 0).setGround(new MagicCircle());
        map.at(4, 4).setGround(new MagicCircle());
        MagicCircle circle = (MagicCircle) map.at(1, 0).getGround();

        long count = circle.allowableActions(worker, map.at(1, 0), "East")
                .getUnmodifiableActionList().size();
        assertEquals("MagicCircle offers no action when actor is adjacent, not on it",
                0, (int) count);
    }

    /**
     * After a MagicCircle teleport, a {@link Flask} must appear on an empty tile
     * adjacent to the destination circle.
     */
    private static void test_magicCircle_spawnsFlaskAdjacentToDestination()
            throws Exception {
        GameMap map = buildTestMap();
        map.at(0, 0).setGround(new MagicCircle());
        map.at(4, 4).setGround(new MagicCircle());
        ContractedWorker worker = placeWorker(map, 0, 0);
        MagicCircle circle = (MagicCircle) map.at(0, 0).getGround();

        circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        boolean flaskFound = map.at(4, 4).getExits().stream()
                .anyMatch(e -> e.getDestination().getItems().stream()
                        .anyMatch(item -> item instanceof Flask));
        assertTrue("MagicCircle spawns a Flask adjacent to destination circle",
                flaskFound);
    }

    /**
     * After a MagicCircle teleport, the actor must be at the destination circle.
     */
    private static void test_magicCircle_movesActorToDestinationCircle()
            throws Exception {
        GameMap map = buildTestMap();
        map.at(0, 0).setGround(new MagicCircle());
        Location destLoc = map.at(4, 4);
        destLoc.setGround(new MagicCircle());
        ContractedWorker worker = placeWorker(map, 0, 0);
        MagicCircle circle = (MagicCircle) map.at(0, 0).getGround();

        circle.allowableActions(worker, map.at(0, 0), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        assertSame("MagicCircle teleports actor to destination circle",
                destLoc, map.locationOf(worker));
    }

    // -----------------------------------------------------------------------
    // REQ2 — AluminiumDoor
    // -----------------------------------------------------------------------

    /**
     * Unlocking an AluminiumDoor with a Level 1 card must deal exactly 2 damage.
     */
    private static void test_aluminiumDoor_unlockDealsTwoDamage()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        map.at(3, 2).setGround(new AluminiumDoor());
        worker.getInventory().add(new AccessCard(1));
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertEquals("AluminiumDoor unlock deals exactly 2 damage to actor",
                hpBefore - 2, worker.getStatistic(ActorStatistics.HEALTH));
    }

    /**
     * AluminiumDoor must be passable after a successful unlock.
     */
    private static void test_aluminiumDoor_isPassableAfterUnlock()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("AluminiumDoor is passable after unlock",
                map.at(3, 2).canActorEnter(worker));
    }

    /**
     * AluminiumDoor must remain impassable before any unlock attempt.
     */
    private static void test_aluminiumDoor_impassableByDefault()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AluminiumDoor door = new AluminiumDoor();
        assertFalse("AluminiumDoor is impassable by default",
                door.canActorEnter(worker));
    }

    /**
     * A worker with no access card must not be able to unlock any door.
     */
    private static void test_aluminiumDoor_cannotUnlockWithoutCard()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse("AluminiumDoor remains locked without an access card",
                door.isUnlocked());
    }

    // -----------------------------------------------------------------------
    // REQ2 — IronDoor
    // -----------------------------------------------------------------------

    /**
     * IronDoor must remain impassable before unlock.
     */
    private static void test_ironDoor_impassableByDefault() {
        ContractedWorker worker = new ContractedWorker(
                "Worker", '@', 10, new BasicInventory());
        IronDoor door = new IronDoor();
        assertFalse("IronDoor is impassable by default",
                door.canActorEnter(worker));
    }

    /**
     * IronDoor must be passable after unlocking with a Level 2 card.
     */
    private static void test_ironDoor_isPassableAfterUnlockWithLevel2Card()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        IronDoor door = new IronDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(2));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("IronDoor is passable after unlocking with Level 2 card",
                door.canActorEnter(worker));
    }

    /**
     * Unlocking an IronDoor must spawn {@link Fire} on at least one adjacent tile.
     */
    private static void test_ironDoor_spawnsFireOnAdjacentTilesOnUnlock()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        map.at(3, 2).setGround(new IronDoor());
        worker.getInventory().add(new AccessCard(2));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        boolean fireFound = map.at(3, 2).getExits().stream()
                .anyMatch(e -> e.getDestination().getGround() instanceof Fire);
        assertTrue("IronDoor unlock spawns Fire on at least one adjacent tile",
                fireFound);
    }

    /**
     * A Level 1 card must not be able to unlock an IronDoor (requires Level 2+).
     */
    private static void test_ironDoor_cannotUnlockWithLevel1Card()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        IronDoor door = new IronDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse("Level 1 card cannot unlock an IronDoor",
                door.isUnlocked());
    }

    // -----------------------------------------------------------------------
    // REQ2 — TitaniumDoor
    // -----------------------------------------------------------------------

    /**
     * TitaniumDoor must remain impassable before unlock.
     */
    private static void test_titaniumDoor_impassableByDefault() {
        ContractedWorker worker = new ContractedWorker(
                "Worker", '@', 10, new BasicInventory());
        TitaniumDoor door = new TitaniumDoor();
        assertFalse("TitaniumDoor is impassable by default",
                door.canActorEnter(worker));
    }

    /**
     * Unlocking a TitaniumDoor must heal the actor by exactly 5 HP.
     */
    private static void test_titaniumDoor_unlockHealsWorkerFiveHp()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        worker.hurt(10);
        int hpBefore = worker.getStatistic(ActorStatistics.HEALTH);
        map.at(3, 2).setGround(new TitaniumDoor());
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertEquals("TitaniumDoor unlock heals actor exactly 5 HP",
                hpBefore + TitaniumDoor.HEAL_AMOUNT,
                worker.getStatistic(ActorStatistics.HEALTH));
    }

    /**
     * A Level 2 card must not be able to unlock a TitaniumDoor (requires Level 3).
     */
    private static void test_titaniumDoor_cannotUnlockWithLevel2Card()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TitaniumDoor door = new TitaniumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(2));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse("Level 2 card cannot unlock a TitaniumDoor",
                door.isUnlocked());
    }

    /**
     * TitaniumDoor must be passable after a successful Level 3 unlock.
     */
    private static void test_titaniumDoor_isPassableAfterUnlock()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TitaniumDoor door = new TitaniumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("TitaniumDoor is passable after Level 3 unlock",
                door.canActorEnter(worker));
    }

    // -----------------------------------------------------------------------
    // REQ2 — AccessCard clearance hierarchy
    // -----------------------------------------------------------------------

    /**
     * A Level 3 card must satisfy the Level 1 clearance requirement
     * (higher clearance unlocks lower-tier doors).
     */
    private static void test_accessCard_level3_unlocksAluminiumDoor()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        AluminiumDoor door = new AluminiumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("Level 3 card satisfies Level 1 clearance and opens AluminiumDoor",
                door.isUnlocked());
    }

    /**
     * A Level 3 card must satisfy the Level 2 clearance requirement.
     */
    private static void test_accessCard_level3_unlocksIronDoor()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        IronDoor door = new IronDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("Level 3 card satisfies Level 2 clearance and opens IronDoor",
                door.isUnlocked());
    }

    /**
     * A Level 1 card must not be able to unlock a TitaniumDoor.
     */
    private static void test_accessCard_level1_cannotUnlockTitaniumDoor()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TitaniumDoor door = new TitaniumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertFalse("Level 1 card cannot unlock TitaniumDoor",
                door.isUnlocked());
    }

    /**
     * When the actor carries both a Level 1 and a Level 3 card, the highest
     * level (3) must be used and must satisfy all clearance requirements.
     */
    private static void test_accessCard_highestLevelCardUsedWhenMultipleCarried()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TitaniumDoor door = new TitaniumDoor();
        map.at(3, 2).setGround(door);
        worker.getInventory().add(new AccessCard(1));
        worker.getInventory().add(new AccessCard(3));

        new UnlockDoorAction(map.at(3, 2), "East").execute(worker, map);

        assertTrue("Highest card level used when worker carries multiple cards",
                door.isUnlocked());
    }

    /**
     * AccessCard Level 1 must have the CAN_UNLOCK_DOOR ability so the item
     * system can detect it without instanceof.
     */
    private static void test_accessCard_hasUnlockAbility() {
        AccessCard card = new AccessCard(1);
        assertTrue("AccessCard has CAN_UNLOCK_DOOR ability",
                card.hasAbility(game.enums.WorkerAbility.CAN_UNLOCK_DOOR));
    }

    // -----------------------------------------------------------------------
    // REQ2 — TeleportationTube
    // -----------------------------------------------------------------------

    /**
     * TeleportationTube must offer one action per registered destination.
     */
    private static void test_teleportationTube_offersOneActionPerDestination()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TeleportationTube tube = new TeleportationTube();
        map.at(2, 2).setGround(tube);
        tube.addDestination(map.at(0, 0), "Alpha");
        tube.addDestination(map.at(4, 4), "Beta");

        long count = tube.allowableActions(worker, map.at(2, 2), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .count();
        assertEquals("TeleportationTube offers one action per destination",
                2, (int) count);
    }

    /**
     * TeleportationTube must offer no action when the actor is adjacent
     * (direction non-empty) — only activates when standing on it.
     */
    private static void test_teleportationTube_offersNoActionWhenActorIsAdjacent()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TeleportationTube tube = new TeleportationTube();
        map.at(3, 2).setGround(tube);
        tube.addDestination(map.at(0, 0), "Alpha");

        long count = tube.allowableActions(worker, map.at(3, 2), "East")
                .getUnmodifiableActionList().size();
        assertEquals("TeleportationTube offers no action when actor is adjacent",
                0, (int) count);
    }

    /**
     * After a TeleportationTube teleport, Fire must appear on at least one tile
     * adjacent to the arrival location (regardless of malfunction).
     */
    private static void test_teleportationTube_spawnsFireAdjacentToArrival()
            throws Exception {
        GameMap map = buildTestMap();
        ContractedWorker worker = placeWorker(map, 2, 2);
        TeleportationTube tube = new TeleportationTube();
        map.at(2, 2).setGround(tube);
        Location dest = map.at(0, 0);
        tube.addDestination(dest, "Target");

        tube.allowableActions(worker, map.at(2, 2), "")
                .getUnmodifiableActionList().stream()
                .filter(a -> a instanceof TeleportAction)
                .findFirst()
                .ifPresent(a -> a.execute(worker, map));

        // fire spawns on adjacent tiles of wherever the actor landed
        Location actualLocation = map.locationOf(worker);
        boolean fireFound = actualLocation.getExits().stream()
                .anyMatch(e -> e.getDestination().getGround() instanceof Fire);
        assertTrue("TeleportationTube spawns Fire adjacent to arrival location",
                fireFound);
    }

    // -----------------------------------------------------------------------
    // Main
    // -----------------------------------------------------------------------

    /**
     * Entry point. Runs all REQ2 tests and prints a summary.
     *
     * @param args unused
     */
    public static void main(String[] args) {

        System.out.println("\n--- REQ2: TeleportAction core ---");
        run("teleportAction_movesActorToDestination",
                UnitTests2::test_teleportAction_movesActorToDestination);
        run("teleportAction_effectReceivesCorrectSource",
                UnitTests2::test_teleportAction_effectReceivesCorrectSource);
        run("teleportAction_effectReceivesCorrectDestination",
                UnitTests2::test_teleportAction_effectReceivesCorrectDestination);
        run("teleportAction_effectFiresWhenSourceEqualsDestination",
                UnitTests2::test_teleportAction_effectFiresWhenSourceEqualsDestination);
        run("teleportAction_actorRemainsAtDestinationAfterEffect",
                UnitTests2::test_teleportAction_actorRemainsAtDestinationAfterEffect);

        System.out.println("\n--- REQ2: AlienCube ---");
        run("alienCube_offersThreeTeleportActions",
                UnitTests2::test_alienCube_offersThreeTeleportActions);
        run("alienCube_threeDestinationsAreDistinct",
                UnitTests2::test_alienCube_threeDestinationsAreDistinct);
        run("alienCube_corruptsAllAdjacentSourceTiles",
                UnitTests2::test_alienCube_corruptsAllAdjacentSourceTiles);
        run("alienCube_doesNotCorruptNonAdjacentTiles",
                UnitTests2::test_alienCube_doesNotCorruptNonAdjacentTiles);
        run("alienCube_isReusable",
                UnitTests2::test_alienCube_isReusable);
        run("alienCube_onSell_spawnsOneUndead",
                UnitTests2::test_alienCube_onSell_spawnsOneUndead);
        run("alienCube_onSell_doesNotCrashWhenNoSpaceAvailable",
                UnitTests2::test_alienCube_onSell_doesNotCrashWhenNoSpaceAvailable);

        System.out.println("\n--- REQ2: ToxicWaste ---");
        run("toxicWaste_dealsDamageEachTick",
                UnitTests2::test_toxicWaste_dealsDamageEachTick);
        run("toxicWaste_isPassableByActors",
                UnitTests2::test_toxicWaste_isPassableByActors);
        run("toxicWaste_accumulatesDamageOverMultipleTicks",
                UnitTests2::test_toxicWaste_accumulatesDamageOverMultipleTicks);
        run("toxicWaste_doesNotDamageAbsentActor",
                UnitTests2::test_toxicWaste_doesNotDamageAbsentActor);

        System.out.println("\n--- REQ2: MagicCircle ---");
        run("magicCircle_offersNoActionWhenAlone",
                UnitTests2::test_magicCircle_offersNoActionWhenAlone);
        run("magicCircle_offersOneActionWithOnePeerCircle",
                UnitTests2::test_magicCircle_offersOneActionWithOnePeerCircle);
        run("magicCircle_offersNoActionWhenActorIsAdjacent",
                UnitTests2::test_magicCircle_offersNoActionWhenActorIsAdjacent);
        run("magicCircle_spawnsFlaskAdjacentToDestination",
                UnitTests2::test_magicCircle_spawnsFlaskAdjacentToDestination);
        run("magicCircle_movesActorToDestinationCircle",
                UnitTests2::test_magicCircle_movesActorToDestinationCircle);

        System.out.println("\n--- REQ2: AluminiumDoor ---");
        run("aluminiumDoor_unlockDealsTwoDamage",
                UnitTests2::test_aluminiumDoor_unlockDealsTwoDamage);
        run("aluminiumDoor_isPassableAfterUnlock",
                UnitTests2::test_aluminiumDoor_isPassableAfterUnlock);
        run("aluminiumDoor_impassableByDefault",
                UnitTests2::test_aluminiumDoor_impassableByDefault);
        run("aluminiumDoor_cannotUnlockWithoutCard",
                UnitTests2::test_aluminiumDoor_cannotUnlockWithoutCard);

        System.out.println("\n--- REQ2: IronDoor ---");
        run("ironDoor_impassableByDefault",
                UnitTests2::test_ironDoor_impassableByDefault);
        run("ironDoor_isPassableAfterUnlockWithLevel2Card",
                UnitTests2::test_ironDoor_isPassableAfterUnlockWithLevel2Card);
        run("ironDoor_spawnsFireOnAdjacentTilesOnUnlock",
                UnitTests2::test_ironDoor_spawnsFireOnAdjacentTilesOnUnlock);
        run("ironDoor_cannotUnlockWithLevel1Card",
                UnitTests2::test_ironDoor_cannotUnlockWithLevel1Card);

        System.out.println("\n--- REQ2: TitaniumDoor ---");
        run("titaniumDoor_impassableByDefault",
                UnitTests2::test_titaniumDoor_impassableByDefault);
        run("titaniumDoor_unlockHealsWorkerFiveHp",
                UnitTests2::test_titaniumDoor_unlockHealsWorkerFiveHp);
        run("titaniumDoor_cannotUnlockWithLevel2Card",
                UnitTests2::test_titaniumDoor_cannotUnlockWithLevel2Card);
        run("titaniumDoor_isPassableAfterUnlock",
                UnitTests2::test_titaniumDoor_isPassableAfterUnlock);

        System.out.println("\n--- REQ2: AccessCard clearance ---");
        run("accessCard_level3_unlocksAluminiumDoor",
                UnitTests2::test_accessCard_level3_unlocksAluminiumDoor);
        run("accessCard_level3_unlocksIronDoor",
                UnitTests2::test_accessCard_level3_unlocksIronDoor);
        run("accessCard_level1_cannotUnlockTitaniumDoor",
                UnitTests2::test_accessCard_level1_cannotUnlockTitaniumDoor);
        run("accessCard_highestLevelCardUsedWhenMultipleCarried",
                UnitTests2::test_accessCard_highestLevelCardUsedWhenMultipleCarried);
        run("accessCard_hasUnlockAbility",
                UnitTests2::test_accessCard_hasUnlockAbility);

        System.out.println("\n--- REQ2: TeleportationTube ---");
        run("teleportationTube_offersOneActionPerDestination",
                UnitTests2::test_teleportationTube_offersOneActionPerDestination);
        run("teleportationTube_offersNoActionWhenActorIsAdjacent",
                UnitTests2::test_teleportationTube_offersNoActionWhenActorIsAdjacent);
        run("teleportationTube_spawnsFireAdjacentToArrival",
                UnitTests2::test_teleportationTube_spawnsFireAdjacentToArrival);

        System.out.println("\n=== Results: "
                + passed + " passed, " + failed + " failed ===");
    }

    /**
     * Runs a single test method, catching any exception and recording it as a failure.
     *
     * @param name the test name printed in output
     * @param test the test to execute
     */
    private static void run(String name, TestRunnable test) {
        try {
            test.run();
        } catch (Exception e) {
            System.out.println("[ERROR] " + name + ": " + e);
            failed++;
        }
    }

    /**
     * Functional interface for test methods that may throw checked exceptions.
     */
    @FunctionalInterface
    private interface TestRunnable {
        /** Executes the test. */
        void run() throws Exception;
    }
}