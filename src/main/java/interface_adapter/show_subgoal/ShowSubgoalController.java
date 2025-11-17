package interface_adapter.show_subgoal;

import use_case.show_subgoal.SetPriorityInputData;
import use_case.show_subgoal.ShowSubgoalInputBoundary;
import use_case.show_subgoal.ShowSubgoalInputData;

/**
 * Controller for the ShowSubgoal use case.
 * Called by the view when the user interacts with the subgoal popup.
 */
public class ShowSubgoalController {

    private final ShowSubgoalInputBoundary interactor;

    /**
     * Constructs a ShowSubgoalController.
     *
     * @param interactor the input boundary for the ShowSubgoal use case
     */
    public ShowSubgoalController(ShowSubgoalInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the subgoal with the given ID.
     *
     * @param subgoalId the ID of the subgoal to display
     */
    public void execute(int subgoalId) {
        ShowSubgoalInputData data = new ShowSubgoalInputData(subgoalId);
        interactor.execute(data);
    }

    /**
     * Updates the priority flag for the given subgoal.
     *
     * @param subgoalId the ID of the subgoal to update
     * @param priority the new priority value
     */
    public void setPriority(int subgoalId, boolean priority) {
        SetPriorityInputData data = new SetPriorityInputData(subgoalId, priority);
        interactor.setPriority(data);
    }
}
