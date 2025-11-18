package interface_adapter.show_plans;

import interface_adapter.ViewModel;

/**
 * The View Model for the Show Plans View.
 */
public class ShowPlansViewModel extends ViewModel<ShowPlansState> {

    public static final String TITLE_LABEL = "My Plans";
    public static final String SUBGOALS_BUTTON_LABEL = "Subgoals";
    public static final String DELETE_BUTTON_LABEL = "Delete";

    public ShowPlansViewModel() {
        super("show plans");
        setState(new ShowPlansState());
    }
}

