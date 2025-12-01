package interface_adapter.plan.save_plan;

import interface_adapter.ViewModel;

/**
 * SavePlanViewModel manages the state associated with saving a plan
 * and identifies the view name used by the dialog system.
 */
public class SavePlanViewModel extends ViewModel<SavePlanState> {

    /**
     * Creates a new SavePlanViewModel with the default view name
     * and initializes it with a fresh SavePlanState.
     */
    public SavePlanViewModel() {
        super("save plan");
        this.setState(new SavePlanState());
    }
}
