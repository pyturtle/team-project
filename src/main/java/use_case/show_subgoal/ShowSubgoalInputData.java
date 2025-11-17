package use_case.show_subgoal;

/**
 * Input data for the ShowSubgoal use case.
 * Contains the ID of the subgoal that should be displayed.
 */
public class ShowSubgoalInputData {

    private final int subgoalId;

    /**
     * Constructs a new ShowSubgoalInputData.
     *
     * @param subgoalId the ID of the subgoal to load
     */
    public ShowSubgoalInputData(int subgoalId) {
        this.subgoalId = subgoalId;
    }

    /**
     * @return the ID of the subgoal to load
     */
    public int getSubgoalId() {
        return subgoalId;
    }
}
