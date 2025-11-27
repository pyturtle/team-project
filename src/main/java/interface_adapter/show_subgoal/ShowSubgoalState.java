package interface_adapter.show_subgoal;

import entity.subgoal.Subgoal;
import java.util.ArrayList;
import java.util.List;

/**
 * State object for the ShowSubgoalViewModel.
 * Stores the values that the Subgoal popup view displays.
 */
public class ShowSubgoalState {

    private String id = "";
    private String name = "";
    private String description = "";
    private boolean priority;
    private boolean completed;
    private String errorMessage = "";

    private List<Subgoal> subgoals = new ArrayList<>();
    private int currentIndex = 0;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isPriority() { return priority; }

    public void setPriority(boolean priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Subgoal> getSubgoals() {
        return subgoals;
    }
    public void setSubgoals(List<Subgoal> subgoals) {
        this.subgoals = subgoals;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Subgoal getCurrentSubgoal() {
        if (subgoals.isEmpty()) return null;
        return subgoals.get(currentIndex);
    }
}
