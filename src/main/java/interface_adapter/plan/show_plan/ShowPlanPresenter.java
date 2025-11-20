package interface_adapter.plan.show_plan;

import interface_adapter.DialogManagerModel;
import use_case.plan.show_plan.ShowPlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanOutputData;

public class ShowPlanPresenter implements ShowPlanOutputBoundary {
    private final ShowPlanViewModel showPlanViewModel;
    private final DialogManagerModel dialogManagerModel;

    public ShowPlanPresenter(ShowPlanViewModel showPlanViewModel, DialogManagerModel dialogManagerModel) {
        this.showPlanViewModel = showPlanViewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    @Override
    public void prepareView(ShowPlanOutputData showPlanOutputData) {
        final ShowPlanState showPlanState = showPlanViewModel.getState();
        showPlanState.setPlanName(showPlanOutputData.getPlanName());
        showPlanState.setPlanDescription(showPlanOutputData.getPlanDescription());
        showPlanState.setSubgoalList(showPlanOutputData.getSubgoalList());
        showPlanViewModel.firePropertyChange();

        dialogManagerModel.setState(showPlanViewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }
}
