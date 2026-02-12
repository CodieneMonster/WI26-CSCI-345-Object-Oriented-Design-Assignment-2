import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneCard {

    private final String title;
    private final int budget;
    private final List<OnCardRole> onCardRoles = new ArrayList<>();

    public SceneCard(String title, int budget) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Scene title required.");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Scene budget must be non-negative.");
        }
        this.title = title.trim();
        this.budget = budget;
    }

    public String getTitle() { return title; }
    public int getBudget() { return budget; }

    public void addOnCardRole(OnCardRole r) {
        if (r == null) throw new IllegalArgumentException("Role cannot be null.");
        onCardRoles.add(r);
    }

    public List<OnCardRole> getOnCardRoles() {
        return Collections.unmodifiableList(onCardRoles);
    }

    @Override
    public String toString() {
        return "SceneCard{" + "title='" + title + '\'' + ", budget=" + budget + ", roles=" + onCardRoles.size() + '}';
    }
}
