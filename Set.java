import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Set extends Location {
    private final List<Role> roles = new ArrayList<>();

    // Optional fields (useful later, safe to keep now)
    private SceneCard currentScene;     // can be null if no active scene
    private boolean wrapped;            // simple wrapped flag for console testing

    public Set(String name) {
        super(name);
        this.currentScene = null;
        this.wrapped = false;
    }

    // ---------------- Scene state (minimal) ----------------
    public void setScene(SceneCard scene) {
        this.currentScene = scene;
        this.wrapped = false;
        // When a new scene is set, clear all roles (no one should be on roles yet)
        clearAllRoles();
    }

    public SceneCard getScene() {
        return currentScene;
    }

    public boolean hasActiveScene() {
        return currentScene != null && !wrapped;
    }

    public void wrapScene() {
        // For now, wrapping just prevents further work on roles until new scene is set.
        this.wrapped = true;
        clearAllRoles();
        this.currentScene = null;
    }

    // ---------------- Role management ----------------
    public void addRole(Role role) {
        if (role == null) throw new IllegalArgumentException("Role cannot be null.");
        roles.add(role);
    }

    public List<Role> getAllRoles() {
        return Collections.unmodifiableList(roles);
    }

    public List<Role> getAvailableRolesForPlayer(Player p) {
        if (p == null) throw new IllegalArgumentException("Player cannot be null.");

        List<Role> available = new ArrayList<>();
        for (Role r : roles) {
            if (r.isAvailable() && r.isPlayerQualified(p)) {
                available.add(r);
            }
        }
        return available;
    }

    public Role getRoleByName(String roleName) {
        if (roleName == null || roleName.isBlank()) return null;

        for (Role r : roles) {
            if (r.getName().equalsIgnoreCase(roleName.trim())) {
                return r;
            }
        }
        return null;
    }

    public boolean hasRoleNamed(String roleName) {
        return getRoleByName(roleName) != null;
    }

    // ---------------- Work / assignment (core for Assignment 2) ----------------
    /**
     * Assigns the player to a role in THIS set. This supports the "work <role>" command.
     * This method enforces:
     * - player must be at this set
     * - set must have an active scene (optional rule; you can relax it if needed)
     * - role must exist, be available, and player must qualify
     */
    public Role assignRoleToPlayer(Player player, String roleName) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null.");
        if (roleName == null || roleName.isBlank()) throw new IllegalArgumentException("Role name required.");

        // Must be in this set location
        if (player.getLocation() != this) {
            throw new IllegalStateException("Player must be at this set to work a role here.");
        }

        // Optional rule check (good design): must have an active scene to work
        if (!hasActiveScene() && roles.size() > 0) {
            // If you want to allow working even without scene during early milestones, comment this out.
            throw new IllegalStateException("No active scene here. Cannot work a role.");
        }

        Role role = getRoleByName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("No role named '" + roleName + "' at set " + getName());
        }

        // Let Player and Role enforce working/rank/availability
        player.takeRole(role);
        return role;
    }

    /**
     * Clears all role occupancy at this set.
     * Useful for end-day reset or wrap logic.
     */
    public void clearAllRoles() {
        for (Role r : roles) {
            r.clear();
        }
    }

    // ---------------- Console-friendly info ----------------
    public String rolesSummary(Player viewer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Roles at ").append(getName()).append(":\n");

        for (Role r : roles) {
            String status = r.isAvailable() ? "OPEN" : "TAKEN";
            String qual = (viewer != null && r.isPlayerQualified(viewer)) ? "QUALIFY" : "NO";
            sb.append(" - ").append(r.getName())
              .append(" (rank ").append(r.getRankRequired()).append(") ")
              .append(status).append(", ").append(qual)
              .append("\n");
        }
        return sb.toString();
    }
}
