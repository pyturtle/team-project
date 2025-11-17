package use_case.show_subgoal;

/**
 * Input data for updating the completion flag of a subgoal.
 */
public class SetCompletedInputData {

    private final int subgoalId;
    private final boolean completed;

    /**
     * Constructs a new SetCompletedInputData.
     *
     * @param subgoalId the ID of the subgoal whose completion should be updated
     * @param completed the new completed value
     */
    public SetCompletedInputData(int subgoalId, boolean completed) {
        this.subgoalId = subgoalId;
        this.completed = completed;
    }

    /** @return the ID of the subgoal whose completion should be updated */
    public int getSubgoalId() { return subgoalId; }

    /** @return the new completed value */
    public boolean isCompleted() { return completed; }
}
