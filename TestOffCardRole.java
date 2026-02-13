public class TestOffCardRole {
    public static void main(String[] args) {
        System.out.println("=== TestOffCardRole ===");

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

        System.out.println("=== End TestOffCardRole ===");
    }
}
