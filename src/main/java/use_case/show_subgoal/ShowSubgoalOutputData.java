package use_case.show_subgoal;

/**
 * Output data for the ShowSubgoal use case.
 * Contains the information needed by the Presenter/ViewModel
 * to display the subgoal popup.
 */
public class ShowSubgoalOutputData {

    private final String name;
    private final String description;
    private final boolean priority;

    /**
     * Constructs a new ShowSubgoalOutputData.
     *
     * @param name the subgoal's name/title
     * @param description the description of the subgoal
     * @param priority whether the subgoal is marked as high priority
     */
    public ShowSubgoalOutputData(String name,
                                 String description,
                                 boolean priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    /** @return the subgoal's name/title */
    public String getName() {
        return name;
    }

    /** @return the subgoal's description */
    public String getDescription() {
        return description;
    }

    /** @return whether the subgoal is marked as priority */
    public boolean isPriority() {
        return priority;
    }
}
