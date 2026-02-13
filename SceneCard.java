import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneCard {
    private final String title;
    private final int budget;
    private final List<OnCardRole> onCardRoles;

    public SceneCard(String title, int budget) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Scene title cannot be null or empty.");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Budget must be non-negative.");
        }
        this.title = title.trim();
        this.budget = budget;
        this.onCardRoles = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public int getBudget() { return budget; }

    public void addOnCardRole(OnCardRole role) {
        if (role == null) throw new IllegalArgumentException("Role cannot be null.");
        onCardRoles.add(role);
    }

    public List<OnCardRole> getOnCardRoles() {
        return Collections.unmodifiableList(onCardRoles);
    }

    public boolean hasOnCardRoleNamed(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) return false;
        String key = roleName.trim();
        for (OnCardRole r : onCardRoles) {
            if (r.getName().equalsIgnoreCase(key)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("SceneCard{%s, budget=%d, onCardRoles=%d}", title, budget, onCardRoles.size());
    }
}
