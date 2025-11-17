package interface_adapter.plan.show_plan;

import interface_adapter.ViewModel;

public class ShowPlanViewModel extends ViewModel<ShowPlanState> {
    public ShowPlanViewModel(String viewName) {
        super("show plan");
        this.setState(new ShowPlanState());
    }
}
