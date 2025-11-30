package interface_adapter.plan.generate_plan;

import interface_adapter.ViewModel;

/**
 * GeneratePlanViewModel holds the state and UI labels for the generate plan view.
 * It manages a GeneratePlanState instance and provides display text constants.
 */
public class GeneratePlanViewModel extends ViewModel<GeneratePlanState> {
    public static final String TITLE_LABEL = "Generate Plan";
    public static final String SEND_BUTTON_LABEL = "Send";
    public static final String TRY_AGAIN_BUTTON_LABEL = "Try Again";
    public static final String SHOW_PLAN_BUTTON_LABEL = "Show Plan";
    public static final String LOADING_LABEL = "Loading...";

    /**
     * Creates a new GeneratePlanViewModel with the default view name
     * and initializes its state with a fresh GeneratePlanState.
     */
    public GeneratePlanViewModel() {
        super("generate plan");
        setState(new GeneratePlanState());
    }
}
