package interface_adapter.plan.show_plan;

import use_case.plan.show_plan.ShowPlanInputData;
import use_case.plan.show_plan.ShowPlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanOutputData;

public class ShowPlanPresenter implements ShowPlanOutputBoundary {
    private final ShowPlanViewModel showPlanViewModel;

    public ShowPlanPresenter(ShowPlanViewModel showPlanViewModel) {
        this.showPlanViewModel = showPlanViewModel;
    }

    @Override
    public void prepareView(ShowPlanOutputData showPlanOutputData) {
        final ShowPlanState showPlanState = showPlanViewModel.getState();
        showPlanState.setPlanName(showPlanOutputData.getPlanName());
        showPlanState.setPlanDescription(showPlanOutputData.getPlanDescription());
        showPlanState.setSubgoalList(showPlanOutputData.getSubgoalList());
        showPlanViewModel.firePropertyChange();
    }
}
