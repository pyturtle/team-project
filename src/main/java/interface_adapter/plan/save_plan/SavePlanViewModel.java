package interface_adapter.plan.save_plan;

import interface_adapter.ViewModel;

public class SavePlanViewModel extends ViewModel<SavePlanState> {
    public SavePlanViewModel() {
        super("save plan");
        this.setState(new SavePlanState());
    }
}
