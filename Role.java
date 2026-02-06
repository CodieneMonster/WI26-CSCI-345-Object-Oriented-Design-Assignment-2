public abstract  class Role {
    private final String name;
    private final int rankRequired;
    private Player occupiedBy;

    protected Role(String name, int rankRequired) {
        if (name == null || name.isBlank()) {throw new IllegalArgumentException("Role name required");}
        if (rankRequired < 1) {throw new IllegalArgumentException("Role rank must be at least 1");}
        this.name = name.trim();
        this.rankRequired = rankRequired;
        this.occupiedBy = null; // start unoccupied
    }

    public String getName() {return name;}
    public int getRankRequired() {return rankRequired;}

    public boolean isAvailable() {return occupiedBy == null;}
    public Player getOccupiedBy() {return occupiedBy;}

    public void assign(Player player) {
        if (player == null) {throw new IllegalArgumentException("Player cannot be null");}
        if (!isAvailable()) {throw new IllegalStateException("Role is already occupied");}
        // if (player.getRank() < rankRequired) {throw new IllegalArgumentException("Player rank too low for this role");}
        occupiedBy = player;
    }

    public void clear() {occupiedBy = null;}
}
