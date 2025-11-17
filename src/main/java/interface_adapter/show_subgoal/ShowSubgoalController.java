package interface_adapter.show_subgoal;

import use_case.show_subgoal.*;

/**
 * Controller for the ShowSubgoal use case.
 * Called by the view in response to user actions.
 */
public class ShowSubgoalController {

    private final ShowSubgoalInputBoundary interactor;

    public ShowSubgoalController(ShowSubgoalInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(int subgoalId) {
        interactor.execute(new ShowSubgoalInputData(subgoalId));
    }

    public void setPriority(int subgoalId, boolean priority) {
        interactor.setPriority(new SetPriorityInputData(subgoalId, priority));
    }

    public void setCompleted(int subgoalId, boolean completed) {
        interactor.setCompleted(new SetCompletedInputData(subgoalId, completed));
    }
}
