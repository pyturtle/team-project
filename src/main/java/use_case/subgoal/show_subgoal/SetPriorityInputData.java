package use_case.subgoal.show_subgoal;

/**
 * Input data for updating the priority flag of a subgoal.
 */
public class SetPriorityInputData {

    private final String subgoalId;
    private final boolean priority;

    /**
     * Constructs a new SetPriorityInputData.
     *
     * @param subgoalId the ID of the subgoal whose priority should be updated
     * @param priority the new priority value
     */
    public SetPriorityInputData(String subgoalId, boolean priority) {
        this.subgoalId = subgoalId;
        this.priority = priority;
    }

    /**
     * @return the ID of the subgoal whose priority should be updated
     */
    public String getSubgoalId() {
        return subgoalId;
    }

    /**
     * @return the new priority value
     */
    public boolean isPriority() {
        return priority;
    }
}
