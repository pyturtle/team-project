package interface_adapter.plan.save_plan;

import interface_adapter.DialogManagerModel;
import use_case.plan.save_plan.SavePlanOutputBoundary;
import use_case.plan.save_plan.SavePlanOutputData;

public class SavePlanPresenter implements SavePlanOutputBoundary {
    private SavePlanViewModel savePlanViewModel;
    private DialogManagerModel dialogManagerModel;

    public SavePlanPresenter(SavePlanViewModel savePlanViewModel,
                             DialogManagerModel dialogManagerModel) {
        this.savePlanViewModel = savePlanViewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    @Override
    public void prepareView(SavePlanOutputData savePlanOutputData) {
        SavePlanState savePlanState = savePlanViewModel.getState();
        savePlanState.setMessage(savePlanOutputData.getMessage());
        savePlanViewModel.firePropertyChange();

        dialogManagerModel.setState(savePlanViewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }

}
