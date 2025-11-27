package interface_adapter.plan.save_plan;

import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import use_case.plan.save_plan.SavePlanOutputBoundary;
import use_case.plan.save_plan.SavePlanOutputData;

public class SavePlanPresenter implements SavePlanOutputBoundary {
    private SavePlanViewModel savePlanViewModel;
    private DialogManagerModel dialogManagerModel;
    private CalendarViewModel calendarViewModel;

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

        // Refresh the calendar view to show newly created subgoals
        if (calendarViewModel != null) {
            calendarViewModel.firePropertyChanged();
        }
    }

}
