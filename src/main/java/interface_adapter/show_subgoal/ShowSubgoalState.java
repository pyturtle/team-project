package interface_adapter.show_subgoal;

/**
 * State object for the ShowSubgoalViewModel.
 * Stores the values that the Subgoal popup view displays.
 */
public class ShowSubgoalState {

    private String name = "";
    private String description = "";
    private boolean priority;
    private boolean completed;
    private String errorMessage = "";

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
}
