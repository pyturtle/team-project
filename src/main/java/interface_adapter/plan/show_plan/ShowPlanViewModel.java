package interface_adapter.plan.show_plan;

import interface_adapter.ViewModel;

/**
 * ShowPlanViewModel manages the state used to display a generated or saved plan,
 * and provides UI label constants for the show plan view.
 */
public class ShowPlanViewModel extends ViewModel<ShowPlanState> {
    public static final String TITLE_LABEL = "Generated Plan";
    public static final String CREATE_BUTTON_LABEL = "Create";

    /**
     * Creates a new ShowPlanViewModel with the default view name
     * and initializes it with a fresh ShowPlanState.
     */
    public ShowPlanViewModel() {
        super("show plan");
        this.setState(new ShowPlanState());
    }
}
