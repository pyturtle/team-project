package interface_adapter.show_subgoal;

import entity.subgoal.Subgoal;
import interface_adapter.ViewModel;

import java.util.List;

/**
 * ViewModel for the Subgoal popup view.
 * Holds a ShowSubgoalState and notifies listeners when the state changes.
 */
public class ShowSubgoalViewModel extends ViewModel<ShowSubgoalState> {

    /**
     * Constructs a ShowSubgoalViewModel.
     * The view name "subgoal" can be used by the ViewManager.
     */
    public ShowSubgoalViewModel() {
        super("subgoal");
        setState(new ShowSubgoalState());
    }

    public void setSubgoals(List<Subgoal> subgoals) {
        ShowSubgoalState newState = getState();
        newState.setSubgoals(subgoals);
        newState.setCurrentIndex(0);
        setState(newState);
    }

    public void setCurrentIndex(int index) {
        ShowSubgoalState state = getState();
        List<Subgoal> subgoals = state.getSubgoals();
        if (subgoals != null && index >= 0 && index < subgoals.size()) {
            state.setCurrentIndex(index);
            state.setId(subgoals.get(index).getId());
            setState(state);
        }
    }

}
