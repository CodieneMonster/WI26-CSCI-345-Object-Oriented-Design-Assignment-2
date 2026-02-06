public class TestQuick {
    public static void main(String[] args) {
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
