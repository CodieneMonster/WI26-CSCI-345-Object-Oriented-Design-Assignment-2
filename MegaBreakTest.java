
public class MegaBreakTest {

    private static int total = 0;
    private static int passed = 0;

    public static void main(String[] args) {
        System.out.println("=== MEGA BREAK TEST: Player + Location + Role (+ Set) ===\n");

        testLocationBasics();
        testPlayerConstructorValidation();
        testRoleConstructorValidation();
        testMovementRules();
        testRoleOccupancyAndClearing();
        testPlayerTakeRoleRules();
        testRehearseRules();
        // testSetAssignRoleRules();
        // stressRandomMovesAndRoleTakes();

        System.out.println("\n=== SUMMARY ===");
        System.out.println("Passed " + passed + " / " + total + " checks.");

        System.out.println("=== MANUAL TESTS: Player, Location, Role ===\n");

        testAdjacency();
        testIllegalMove();
        testMoveWhileWorking();
        testTakeRoleRankCheck();
        testRoleOccupancy();
        testRehearse();
        testDropRole();
        testStatusString();

        System.out.println("\n=== TEST SUMMARY ===");
        System.out.println("Passed " + passed + " / " + total + " tests.");
        System.out.println("\n");
        System.out.println("=== TestOffCardRole ===");
        TestOffCardRole();
        System.out.println("=== End TestOffCardRole ===");
        System.out.println("\n");
        System.out.println("=== TestOnCardRole ===");
        TestOnCardRole();
        System.out.println("=== End TestOnCardRole ===");
        System.out.println("\n");
        System.out.println("=== Quick Test ===");
        QuickTest();
        System.out.println("=== End Quick Test ===");
    }

    // ---------------- Helper: PASS/FAIL wrapper ----------------
    private static void check(String name, Runnable test) {
        total++;
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            System.out.println("[FAIL] " + name);
            System.out.println("       -> " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private static void expectThrow(String name, Class<? extends Throwable> exType, Runnable action) {
        total++;
        try {
            action.run();
            System.out.println("[FAIL] " + name);
            System.out.println("       -> expected " + exType.getSimpleName() + " but nothing was thrown");
        } catch (Throwable t) {
            if (exType.isInstance(t)) {
                passed++;
                System.out.println("[PASS] " + name + " (threw " + exType.getSimpleName() + ")");
            } else {
                System.out.println("[FAIL] " + name);
                System.out.println("       -> expected " + exType.getSimpleName() + " but got " + t.getClass().getSimpleName());
                System.out.println("       -> " + t.getMessage());
            }
        }
    }

    // ---------------- Tests ----------------

    private static void testLocationBasics() {
        System.out.println("\n--- Location basics ---");

        check("addNeighbor makes adjacency bidirectional", () -> {
            Location a = new SimpleLocation("A");
            Location b = new SimpleLocation("B");
            a.addNeighbor(b);

            if (!a.isAdjacent(b)) throw new RuntimeException("A should be adjacent to B");
            if (!b.isAdjacent(a)) throw new RuntimeException("B should be adjacent to A");
        });

        check("getNeighborByName ignores case", () -> {
            Location a = new SimpleLocation("Trailers");
            Location b = new SimpleLocation("Train Station");
            a.addNeighbor(b);

            Location found = a.getNeighborByName("train station");
            if (found == null) throw new RuntimeException("Neighbor should be found by name");
            if (!found.getName().equals("Train Station")) throw new RuntimeException("Wrong neighbor returned");
        });

        check("non-neighbors are not adjacent", () -> {
            Location a = new SimpleLocation("A");
            Location b = new SimpleLocation("B");
            if (a.isAdjacent(b)) throw new RuntimeException("Should not be adjacent");
        });

        expectThrow("Location name cannot be blank", IllegalArgumentException.class, () -> {
            new SimpleLocation("   ");
        });
    }

    private static void testPlayerConstructorValidation() {
        System.out.println("\n--- Player constructor validation ---");

        Location trailers = new SimpleLocation("Trailers");

        expectThrow("Player name cannot be blank", IllegalArgumentException.class, () -> {
            new Player("  ", 1, 0, 0, trailers);
        });

        expectThrow("Player rank must be >= 1", IllegalArgumentException.class, () -> {
            new Player("Blue", 0, 0, 0, trailers);
        });

        expectThrow("Player dollars cannot be negative", IllegalArgumentException.class, () -> {
            new Player("Blue", 1, -1, 0, trailers);
        });

        expectThrow("Player credits cannot be negative", IllegalArgumentException.class, () -> {
            new Player("Blue", 1, 0, -5, trailers);
        });

        expectThrow("Player start location cannot be null", IllegalArgumentException.class, () -> {
            new Player("Blue", 1, 0, 0, null);
        });
    }

    private static void testRoleConstructorValidation() {
        System.out.println("\n--- Role constructor validation ---");

        expectThrow("Role name cannot be blank", IllegalArgumentException.class, () -> {
            new SimpleRole("   ", 1);
        });

        expectThrow("Role rankRequired must be >= 1", IllegalArgumentException.class, () -> {
            new SimpleRole("Talking Mule", 0);
        });
    }

    private static void testMovementRules() {
        System.out.println("\n--- Movement rules ---");

        Location trailers = new SimpleLocation("Trailers");
        Location train = new SimpleLocation("Train Station");
        Location jail = new SimpleLocation("Jail");
        trailers.addNeighbor(train);
        // jail not connected

        Player p = new Player("Blue", 1, 0, 0, trailers);

        check("Legal move to adjacent works", () -> {
            p.moveTo(train);
            if (!p.getLocation().getName().equals("Train Station")) throw new RuntimeException("Move failed");
        });

        expectThrow("Illegal move to non-adjacent throws", IllegalArgumentException.class, () -> {
            p.moveTo(jail);
        });

        expectThrow("Move to null destination throws", IllegalArgumentException.class, () -> {
            p.moveTo(null);
        });
    }

    private static void testRoleOccupancyAndClearing() {
        System.out.println("\n--- Role occupancy & clearing ---");

        Location trailers = new SimpleLocation("Trailers");
        Player p1 = new Player("Blue", 2, 0, 0, trailers);
        Player p2 = new Player("Red", 2, 0, 0, trailers);

        Role r = new SimpleRole("Talking Mule", 1);

        check("Role starts available", () -> {
            if (!r.isAvailable()) throw new RuntimeException("Role should start available");
        });

        check("Assigning role occupies it", () -> {
            r.assign(p1);
            if (r.isAvailable()) throw new RuntimeException("Role should be occupied");
            if (r.getOccupiedBy() != p1) throw new RuntimeException("OccupiedBy should be p1");
        });

        expectThrow("Assigning occupied role throws", IllegalStateException.class, () -> {
            r.assign(p2);
        });

        check("Clearing role makes it available", () -> {
            r.clear();
            if (!r.isAvailable()) throw new RuntimeException("Role should be available after clear");
            if (r.getOccupiedBy() != null) throw new RuntimeException("OccupiedBy should be null after clear");
        });
    }

    private static void testPlayerTakeRoleRules() {
        System.out.println("\n--- Player.takeRole rules ---");

        Location trailers = new SimpleLocation("Trailers");

        Player lowRank = new Player("Low", 1, 0, 0, trailers);
        Player highRank = new Player("High", 3, 0, 0, trailers);

        Role roleRank3 = new SimpleRole("Lead Cowboy", 3);

        expectThrow("Player cannot take role above rank", IllegalStateException.class, () -> {
            lowRank.takeRole(roleRank3);
        });

        check("Qualified player can take role", () -> {
            highRank.takeRole(roleRank3);
            if (!highRank.isWorking()) throw new RuntimeException("Player should be working");
            if (highRank.getRole() != roleRank3) throw new RuntimeException("Player role mismatch");
        });

        expectThrow("Player cannot take second role while working", IllegalStateException.class, () -> {
            Role another = new SimpleRole("Another", 1);
            highRank.takeRole(another);
        });
    }

    private static void testRehearseRules() {
        System.out.println("\n--- Rehearse rules ---");

        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 2, 0, 0, trailers);

        expectThrow("Cannot rehearse when not working", IllegalStateException.class, p::rehearse);

        Role r = new SimpleRole("Talking Mule", 1);
        p.takeRole(r);

        check("Rehearse increments chips", () -> {
            int before = p.getRehearsalChips();
            p.rehearse();
            p.rehearse();
            if (p.getRehearsalChips() != before + 2) throw new RuntimeException("Chips didn't increment correctly");
        });

        check("Dropping role resets rehearsal chips to 0", () -> {
            p.dropRole();
            if (p.getRehearsalChips() != 0) throw new RuntimeException("Rehearsal chips not reset");
            if (p.isWorking()) throw new RuntimeException("Player should not be working");
        });
    }

    // private static void testSetAssignRoleRules() {
    //     System.out.println("\n--- Set.assignRoleToPlayer rules ---");

    //     Set train = new Set("Train Station");
    //     Set jail = new Set("Jail");
    //     train.addNeighbor(jail);

    //     // Add roles to train
    //     train.addRole(new SimpleRole("Talking Mule", 1));
    //     train.addRole(new SimpleRole("Lead Cowboy", 3));

    //     Player p = new Player("Blue", 2, 0, 0, train);


    //     try {
    //         train.setScene(null);
    //     } catch (Exception ignored) {
           
    //     }

    //     check("Player can take a qualifying role at their current set", () -> {
    //         train.assignRoleToPlayer(p, "Talking Mule");
    //         if (!p.isWorking()) throw new RuntimeException("Player should be working after assignment");
    //         if (!p.getRole().getName().equalsIgnoreCase("Talking Mule")) throw new RuntimeException("Wrong role assigned");
    //     });

    //     expectThrow("Cannot assign role that does not exist", IllegalArgumentException.class, () -> {
    //         train.assignRoleToPlayer(p, "NotARealRole");
    //     });

    //     // already working
    //     expectThrow("Cannot assign another role while already working", IllegalStateException.class, () -> {
    //         train.assignRoleToPlayer(p, "Lead Cowboy");
    //     });

    //     // drop role and test rank too low
    //     p.dropRole();
    //     expectThrow("Cannot assign role when rank is too low", IllegalStateException.class, () -> {
    //         train.assignRoleToPlayer(p, "Lead Cowboy");
    //     });

    //     // player not at this set
    //     Player p2 = new Player("Red", 4, 0, 0, jail);
    //     expectThrow("Player must be at set to take role there", IllegalStateException.class, () -> {
    //         train.assignRoleToPlayer(p2, "Talking Mule");
    //     });
    // }

    // private static void stressRandomMovesAndRoleTakes() {
    //     System.out.println("\n--- Stress test: random actions ---");

    //     // Build a small connected graph
    //     Set a = new Set("A");
    //     Set b = new Set("B");
    //     Set c = new Set("C");
    //     Set d = new Set("D");

    //     a.addNeighbor(b);
    //     b.addNeighbor(c);
    //     c.addNeighbor(d);
    //     d.addNeighbor(a); // loop

    //     // roles
    //     a.addRole(new SimpleRole("R1", 1));
    //     a.addRole(new SimpleRole("R2", 2));

    //     Player p = new Player("Blue", 2, 0, 0, a);

    //     Random rng = new Random(0);

    //     check("Stress run doesn't crash for 200 random steps", () -> {
    //         for (int i = 0; i < 200; i++) {
    //             int action = rng.nextInt(4);

    //             if (action == 0) {
    //                 // attempt random move
    //                 if (!p.isWorking()) {
    //                     // try moving to some neighbor by name
    //                     Location loc = p.getLocation();
    //                     // pick one of the known locations
    //                     Location dest = (rng.nextBoolean()) ? b : d;
    //                     if (loc.isAdjacent(dest)) {
    //                         p.moveTo(dest);
    //                     }
    //                 }
    //             } else if (action == 1) {
    //                 // attempt take role if at A and not working
    //                 if (!p.isWorking() && p.getLocation() == a) {
    //                     try {
    //                         a.assignRoleToPlayer(p, "R2");
    //                     } catch (Exception ignored) { }
    //                 }
    //             } else if (action == 2) {
    //                 // rehearse if working
    //                 if (p.isWorking()) {
    //                     p.rehearse();
    //                 }
    //             } else {
    //                 // drop role sometimes
    //                 if (p.isWorking() && rng.nextInt(5) == 0) {
    //                     p.dropRole();
    //                 }
    //             }
    //         }
    //     });
    // }

    // -------------------- Helper --------------------
    private static void check2(boolean condition, String testName) {
        total++;
        if (condition) {
            passed++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }

     // -------------------- Tests --------------------

    private static void testAdjacency() {
        Location a = new SimpleLocation("A");
        Location b = new SimpleLocation("B");

        a.addNeighbor(b);

        check2(a.isAdjacent(b), "Adjacency is bidirectional (A -> B)");
        check2(b.isAdjacent(a), "Adjacency is bidirectional (B -> A)");
    }

    private static void testIllegalMove() {
        Location trailers = new SimpleLocation("Trailers");
        Location train = new SimpleLocation("Train Station");
        Location jail = new SimpleLocation("Jail");

        trailers.addNeighbor(train);

        Player p = new Player("Blue", 1, 0, 0, trailers);

        boolean threw = false;
        try {
            p.moveTo(jail);
        } catch (Exception e) {
            threw = true;
        }

        check2(threw, "Player cannot move to non-adjacent location");
    }

    private static void testMoveWhileWorking() {
        Location trailers = new SimpleLocation("Trailers");
        Location train = new SimpleLocation("Train Station");

        trailers.addNeighbor(train);

        Player p = new Player("Blue", 2, 0, 0, trailers);
        Role role = new SimpleRole("Talking Mule", 1);

        p.takeRole(role);

        boolean threw = false;
        try {
            p.moveTo(train);
        } catch (Exception e) {
            threw = true;
        }

        check2(threw, "Player cannot move while working on a role");
    }

    private static void testTakeRoleRankCheck() {
        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 1, 0, 0, trailers);

        Role role = new SimpleRole("High Rank Role", 3);

        boolean threw = false;
        try {
            p.takeRole(role);
        } catch (Exception e) {
            threw = true;
        }

        check2(threw, "Player cannot take role above rank");
    }

    private static void testRoleOccupancy() {
        Location trailers = new SimpleLocation("Trailers");

        Player p1 = new Player("Blue", 2, 0, 0, trailers);
        Player p2 = new Player("Red", 2, 0, 0, trailers);

        Role role = new SimpleRole("Talking Mule", 1);

        p1.takeRole(role);

        boolean threw = false;
        try {
            p2.takeRole(role);
        } catch (Exception e) {
            threw = true;
        }

        check2(threw, "Role cannot be occupied by two players");
    }

    private static void testRehearse() {
        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 2, 0, 0, trailers);
        Role role = new SimpleRole("Talking Mule", 1);

        p.takeRole(role);
        p.rehearse();
        p.rehearse();

        check2(p.getRehearsalChips() == 2, "Rehearsal chips increment correctly");
    }

    private static void testDropRole() {
        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 2, 0, 0, trailers);
        Role role = new SimpleRole("Talking Mule", 1);

        p.takeRole(role);
        p.dropRole();

        check2(!p.isWorking(), "Dropping role clears working status");
        check2(role.isAvailable(), "Dropping role frees the role");
    }

    private static void testStatusString() {
        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 1, 5, 3, trailers);

        String s = p.statusString();

        check2(s.contains("Blue"), "Status shows player name");
        check2(s.contains("$5"), "Status shows dollars");
        check2(s.contains("3cr"), "Status shows credits");
        check2(s.toLowerCase().contains("trailers"), "Status shows location");
    }



// tests offcard 
  private static void TestOffCardRole() {
        Location trailers = new SimpleLocation("Trailers");
        Player p = new Player("Blue", 1, 0, 0, trailers);

        OffCardRole r = new OffCardRole(
                "Cowboy", 1,
                1, 0,   // success payout
                0, 1,   // fail payout
                "Yeehaw!"
        );

        System.out.println("Role: " + r);
        System.out.println("Available? (expect true): " + r.isAvailable());
        System.out.println("Qualified? (expect true): " + r.isPlayerQualified(p));

        // Assign
        r.assign(p);
        System.out.println("After assign available? (expect false): " + r.isAvailable());
        System.out.println("OccupiedBy (expect Blue): " + r.getOccupiedBy().getName());

        // Success payout
        int d0 = p.getDollars();
        int c0 = p.getCredits();
        r.applySuccessPayout(p);
        System.out.println("After success dollars (expect " + (d0 + 1) + "): " + p.getDollars());
        System.out.println("After success credits (expect " + c0 + "): " + p.getCredits());

        // Fail payout
        d0 = p.getDollars();
        c0 = p.getCredits();
        r.applyFailPayout(p);
        System.out.println("After fail dollars (expect " + d0 + "): " + p.getDollars());
        System.out.println("After fail credits (expect " + (c0 + 1) + "): " + p.getCredits());

        // Clear
        r.clear();
        System.out.println("After clear available? (expect true): " + r.isAvailable());

        // Negative payout should fail
        try {
            new OffCardRole("Bad", 1, 0, 0, -1, 0);
            System.out.println("ERROR: negative payout should fail.");
        } catch (Exception e) {
            System.out.println("EXPECTED FAIL (negative payout): " + e.getMessage());
        }
    }

    private static void TestOnCardRole() {
         Location trailers = new SimpleLocation("Trailers");
        Player low = new Player("Low", 1, 0, 0, trailers);
        Player high = new Player("High", 3, 0, 0, trailers);

        OnCardRole r = new OnCardRole(
                "Sheriff", 2,
                2, 1,    // success payout
                0, 0,    // fail payout
                "Freeze, punk!"
        );

        System.out.println("Role: " + r);
        System.out.println("Available? (expect true): " + r.isAvailable());
        System.out.println("Low qualified? (expect false): " + r.isPlayerQualified(low));
        System.out.println("High qualified? (expect true): " + r.isPlayerQualified(high));

        // Assign + occupancy
        r.assign(high);
        System.out.println("After assign available? (expect false): " + r.isAvailable());
        System.out.println("OccupiedBy (expect High): " + r.getOccupiedBy().getName());

        // Assign again should fail
        try {
            r.assign(low);
            System.out.println("ERROR: assigning occupied role should fail.");
        } catch (Exception e) {
            System.out.println("EXPECTED FAIL (occupied): " + e.getMessage());
        }

        // Payout success
        int d0 = high.getDollars();
        int c0 = high.getCredits();
        r.applySuccessPayout(high);
        System.out.println("After success dollars (expect " + (d0 + 2) + "): " + high.getDollars());
        System.out.println("After success credits (expect " + (c0 + 1) + "): " + high.getCredits());

        // Payout fail
        d0 = high.getDollars();
        c0 = high.getCredits();
        r.applyFailPayout(high);
        System.out.println("After fail dollars (expect " + d0 + "): " + high.getDollars());
        System.out.println("After fail credits (expect " + c0 + "): " + high.getCredits());

        // Clear
        r.clear();
        System.out.println("After clear available? (expect true): " + r.isAvailable());

        // Negative payout constructor should fail
        try {
            new OnCardRole("Bad", 1, -1, 0, 0, 0);
            System.out.println("ERROR: negative payout should fail.");
        } catch (Exception e) {
            System.out.println("EXPECTED FAIL (negative payout): " + e.getMessage());
        }
    }

    private static void QuickTest() {
        Location trailers = new SimpleLocation("Trailers");
        Location train = new SimpleLocation("Train Station");
        Location jail = new SimpleLocation("Jail");

        trailers.addNeighbor(train);
        train.addNeighbor(jail);

        Player p = new Player("Blue", 1, 0, 0, trailers);

        System.out.println("START: " + p.statusString());

        // Legal move
        p.moveTo(train);
        System.out.println("AFTER MOVE: " + p.statusString());

        // Take role + rehearse
        Role r = new SimpleRole("Talking Mule", 1);
        p.takeRole(r);
        p.rehearse();
        System.out.println("AFTER WORK+REHEARSE: " + p.statusString());

        // Illegal move while working (should throw)
        try {
            p.moveTo(jail);
            System.out.println("ERROR: Should not be able to move while working!");
        } catch (Exception e) {
            System.out.println("EXPECTED FAIL (move while working): " + e.getMessage());
        }

        // Drop role then move (should succeed)
        p.dropRole();
        p.moveTo(jail);
        System.out.println("AFTER DROP+MOVE: " + p.statusString());
    }
}
