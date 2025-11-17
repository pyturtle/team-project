package interface_adapter.show_subgoal;

import interface_adapter.ViewModel;

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
}
