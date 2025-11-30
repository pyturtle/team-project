package interface_adapter.plan.save_plan;

import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import use_case.plan.save_plan.SavePlanOutputBoundary;
import use_case.plan.save_plan.SavePlanOutputData;

public class SavePlanPresenter implements SavePlanOutputBoundary {
    private final SavePlanViewModel savePlanViewModel;
    private final DialogManagerModel dialogManagerModel;
    private final CalendarViewModel calendarViewModel;

    public SavePlanPresenter(SavePlanViewModel savePlanViewModel,
                             DialogManagerModel dialogManagerModel,
                             CalendarViewModel calendarViewModel) {
        this.savePlanViewModel = savePlanViewModel;
        this.dialogManagerModel = dialogManagerModel;
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void prepareView(SavePlanOutputData savePlanOutputData) {
        SavePlanState savePlanState = savePlanViewModel.getState();
        savePlanState.setMessage(savePlanOutputData.getMessage());
        savePlanViewModel.firePropertyChange();

        dialogManagerModel.setState(savePlanViewModel.getViewName());
        dialogManagerModel.firePropertyChange();


        if (calendarViewModel != null) {
            calendarViewModel.firePropertyChanged();
        }
    }

}
