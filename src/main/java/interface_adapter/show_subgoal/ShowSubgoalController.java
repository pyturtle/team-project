package interface_adapter.show_subgoal;
import entity.subgoal.Subgoal;
import use_case.subgoal.show_subgoal.SetCompletedInputData;
import use_case.subgoal.show_subgoal.SetPriorityInputData;
import use_case.subgoal.show_subgoal.ShowSubgoalInputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalInputData;

import java.util.List;

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

    public List<Subgoal> getSubgoalsForPlan(String planId, String username) {
        return interactor.getSubgoalsByPlan(planId, username);
    }

    public void showSubgoalForPlan(String planId, String username) {
        List<Subgoal> subgoals = interactor.getSubgoalsByPlan(planId, username);
        for (Subgoal s : subgoals) {
            execute(s.getId());
        }
    }


}
