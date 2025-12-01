package interface_adapter.plan.show_plan;

import interface_adapter.DialogManagerModel;
import use_case.plan.show_plan.ShowPlanOutputBoundary;
import use_case.plan.show_plan.ShowPlanOutputData;

/**
 * ShowPlanPresenter updates the ShowPlanViewModel with the details of a plan
 * and triggers the dialog system to open the show-plan dialog.
 */
public class ShowPlanPresenter implements ShowPlanOutputBoundary {
    private final ShowPlanViewModel showPlanViewModel;
    private final DialogManagerModel dialogManagerModel;

    /**
     * Creates a new ShowPlanPresenter with the given view model and dialog manager.
     * @param showPlanViewModel the view model for displaying plan details
     * @param dialogManagerModel the model that controls which dialog is visible
     */
    public ShowPlanPresenter(ShowPlanViewModel showPlanViewModel, DialogManagerModel dialogManagerModel) {
        this.showPlanViewModel = showPlanViewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    /**
     * Updates the view model with plan details, then opens the show plan dialog.
     * @param showPlanOutputData the data containing plan fields and subgoal list
     */
    @Override
    public void prepareView(ShowPlanOutputData showPlanOutputData) {
        final ShowPlanState showPlanState = showPlanViewModel.getState();
        showPlanState.setPlanName(showPlanOutputData.getPlanName());
        showPlanState.setPlanDescription(showPlanOutputData.getPlanDescription());
        showPlanState.setSubgoalList(showPlanOutputData.getSubgoalList());
        showPlanState.setPlanExists(showPlanOutputData.isCreated());
        showPlanViewModel.firePropertyChange();
        dialogManagerModel.setState(showPlanViewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }
}
