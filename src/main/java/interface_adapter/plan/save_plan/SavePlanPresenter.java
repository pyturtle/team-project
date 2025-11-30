package interface_adapter.plan.save_plan;

import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import use_case.plan.save_plan.SavePlanOutputBoundary;
import use_case.plan.save_plan.SavePlanOutputData;

/**
 * SavePlanPresenter updates the SavePlanViewModel and triggers dialog
 * and calendar updates based on the output of the save plan use case.
 */
public class SavePlanPresenter implements SavePlanOutputBoundary {
    private final SavePlanViewModel savePlanViewModel;
    private final DialogManagerModel dialogManagerModel;
    private final CalendarViewModel calendarViewModel;

    /**
     * Creates a new SavePlanPresenter with the given view model and manager models.
     * @param savePlanViewModel the view model to update with save plan results
     * @param dialogManagerModel the model responsible for controlling dialog visibility
     * @param calendarViewModel the calendar view model to notify when changes occur
     */
    public SavePlanPresenter(SavePlanViewModel savePlanViewModel,
                             DialogManagerModel dialogManagerModel,
                             CalendarViewModel calendarViewModel) {
        this.savePlanViewModel = savePlanViewModel;
        this.dialogManagerModel = dialogManagerModel;
        this.calendarViewModel = calendarViewModel;
    }

    /**
     * Updates the view model with the message from the save plan result,
     * opens the dialog associated with this presenter, and refreshes the calendar.
     * @param savePlanOutputData the data produced by the save plan use case
     */
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
