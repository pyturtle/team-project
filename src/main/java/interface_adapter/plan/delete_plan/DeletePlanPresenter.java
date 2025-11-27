package interface_adapter.plan.delete_plan;

import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.plan.show_plans.ShowPlansViewModel;
import use_case.plan.delete_plan.DeletePlanOutputBoundary;
import use_case.plan.delete_plan.DeletePlanOutputData;

/**
 * The Presenter for the Delete Plan Use Case.
 */
public class DeletePlanPresenter implements DeletePlanOutputBoundary {

    private final ShowPlansViewModel showPlansViewModel;
    private final CalendarViewModel calendarViewModel;

    /**
     * Creates a new Delete Plan Presenter.
     * @param showPlansViewModel the show plans view model
     * @param calendarViewModel the calendar view model
     */
    public DeletePlanPresenter(ShowPlansViewModel showPlansViewModel, CalendarViewModel calendarViewModel) {
        this.showPlansViewModel = showPlansViewModel;
        this.calendarViewModel = calendarViewModel;
    }

    @Override
    public void prepareSuccessView(DeletePlanOutputData outputData) {
        // Trigger a refresh of the Show Plans view
        showPlansViewModel.firePropertyChange();

        // Refresh the calendar view to remove deleted subgoals
        if (calendarViewModel != null) {
            calendarViewModel.firePropertyChanged();
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Trigger error notification (could display via state if needed)
        showPlansViewModel.firePropertyChange();
    }
}

