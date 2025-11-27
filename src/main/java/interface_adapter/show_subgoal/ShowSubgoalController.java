package interface_adapter.show_subgoal;
import use_case.subgoal.show_subgoal.SetCompletedInputData;
import use_case.subgoal.show_subgoal.SetPriorityInputData;
import use_case.subgoal.show_subgoal.ShowSubgoalInputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalInputData;

/**
 * Controller for the ShowSubgoal use case.
 * Called by the view in response to user actions.
 */
public class ShowSubgoalController {

    private final ShowSubgoalInputBoundary interactor;

    public ShowSubgoalController(ShowSubgoalInputBoundary interactor) {
        this.interactor = interactor;}

    public void execute(String subgoalId) {
        interactor.execute(new ShowSubgoalInputData(subgoalId));
    }

    public void setPriority(String subgoalId, boolean priority) {
        interactor.setPriority(new SetPriorityInputData(subgoalId, priority));
    }

    public void setCompleted(String subgoalId, boolean completed) {
        interactor.setCompleted(new SetCompletedInputData(subgoalId, completed));
    }
}
