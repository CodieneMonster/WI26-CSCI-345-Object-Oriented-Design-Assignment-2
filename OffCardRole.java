public class OffCardRole extends Role {
    private final int successDollars;
    private final int successCredits;
    private final int failDollars;
    private final int failCredits;
    private final String line;
    
    public OffCardRole(String name, int rankRequired,
                       int successDollars, int successCredits,
                       int failDollars, int failCredits,
                       String line) {
        super(name, rankRequired);

        if (successDollars < 0 || successCredits < 0 || failDollars < 0 || failCredits < 0) {
            throw new IllegalArgumentException("Payout values must be non-negative.");
        }

        this.successDollars = successDollars;
        this.successCredits = successCredits;
        this.failDollars = failDollars;
        this.failCredits = failCredits;
        this.line = (line == null) ? "" : line.trim();
    }

    public OffCardRole(String name, int rankRequired,
                       int successDollars, int successCredits,
                       int failDollars, int failCredits) {
        this(name, rankRequired, successDollars, successCredits, failDollars, failCredits, "");
    }

    public int getSuccessDollars() { return successDollars; }
    public int getSuccessCredits() { return successCredits; }
    public int getFailDollars() { return failDollars; }
    public int getFailCredits() { return failCredits; }
    public String getLine() { return line; }

    /**
     * Applies payout for a successful act for THIS off-card role.
     */
    public void applySuccessPayout(Player p) {
        if (p == null) throw new IllegalArgumentException("Player cannot be null.");
        if (successDollars > 0) p.addDollars(successDollars);
        if (successCredits > 0) p.addCredits(successCredits);
    }

    /**
     * Applies payout for a failed act for THIS off-card role.
     */
    public void applyFailPayout(Player p) {
        if (p == null) throw new IllegalArgumentException("Player cannot be null.");
        if (failDollars > 0) p.addDollars(failDollars);
        if (failCredits > 0) p.addCredits(failCredits);
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" [OffCard: success +$%d +%dcr, fail +$%d +%dcr]",
                        successDollars, successCredits, failDollars, failCredits);
    }
}
