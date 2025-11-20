package use_case.subgoal.show_subgoal;

/**
 * Input data for the ShowSubgoal use case.
 * Contains the ID of the subgoal that should be displayed.
 */
public class ShowSubgoalInputData {

    private final String subgoalId;

    /**
     * Constructs a new ShowSubgoalInputData.
     *
     * @param subgoalId the ID of the subgoal to load
     */
    public ShowSubgoalInputData(String subgoalId) {
        this.subgoalId = subgoalId;
    }

    /**
     * @return the ID of the subgoal to load
     */
    public String getSubgoalId() {
        return subgoalId;
    }
}
