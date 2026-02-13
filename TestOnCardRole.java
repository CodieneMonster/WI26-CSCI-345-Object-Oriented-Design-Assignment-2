public class TestOnCardRole {
    public static void main(String[] args) {
        System.out.println("=== TestOnCardRole ===");

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

        System.out.println("=== End TestOnCardRole ===");
    }
}
