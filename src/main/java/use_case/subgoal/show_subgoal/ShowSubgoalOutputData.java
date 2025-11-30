package use_case.subgoal.show_subgoal;

/**
 * Output data for the ShowSubgoal use case.
 * Contains the information needed by the Presenter/ViewModel
 * to display the subgoal popup.
 */
public class ShowSubgoalOutputData {

    private final String id;
    private final String name;
    private final String description;
    private final boolean priority;
    private final boolean completed;

    /**
     * Constructs a new ShowSubgoalOutputData.
     *
     * @param name        the subgoal's name/title
     * @param description the description of the subgoal
     * @param priority    whether the subgoal is marked as high priority
     * @param completed   whether the subgoal is completed
     */
    public ShowSubgoalOutputData(String id, String name,
                                 String description,
                                 boolean priority,
                                 boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
    }

    /**
     * @return the subgoal's name/title
     */
    public String getName() {
        return name;
    }

    /**
     * @return the subgoal's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return whether the subgoal is marked as priority
     */
    public boolean isPriority() {
        return priority;
    }

    /**
     * @return whether the subgoal is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    public String getId() {
        return id;
    }
}
