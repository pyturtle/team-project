package interface_adapter.plan.show_plan;

import interface_adapter.ViewModel;

public class ShowPlanViewModel extends ViewModel<ShowPlanState> {
    public static final String TITLE_LABEL = "Generated Plan";
    public static final String CREATE_BUTTON_LABEL = "Create";

    public ShowPlanViewModel() {
        super("show plan");
        this.setState(new ShowPlanState());
    }
}
