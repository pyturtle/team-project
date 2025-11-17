package use_case.show_subgoal;

/**
 * Input boundary for the ShowSubgoal use case.
 * Controllers call this interface to trigger showing a subgoal,
 * and to update its priority status.
 */
public interface ShowSubgoalInputBoundary {

    /**
     * Executes the use case to load and display a subgoal.
     *
     * @param inputData the input data containing the subgoal ID
     */
    void execute(ShowSubgoalInputData inputData);

    /**
     * Executes the use case to update the priority flag of a subgoal.
     *
     * @param inputData the input data containing the subgoal ID and new priority value
     */
    void setPriority(SetPriorityInputData inputData);
}
