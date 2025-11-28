package interface_adapter.show_subgoal;

import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.calendar.CalendarState;
import interface_adapter.filter_subgoals.FilterSubgoalsController;
import use_case.subgoal.show_subgoal.ShowSubgoalOutputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalOutputData;

/**
 * Presenter for the ShowSubgoal use case.
 * Maps output data to the ShowSubgoalViewModel.
 */
public class ShowSubgoalPresenter implements ShowSubgoalOutputBoundary {

    private final ShowSubgoalViewModel viewModel;
    private final DialogManagerModel dialogManagerModel;
    private final CalendarViewModel calendarViewModel;
    private FilterSubgoalsController filterSubgoalsController;

    public ShowSubgoalPresenter(ShowSubgoalViewModel viewModel, DialogManagerModel dialogManagerModel,
                                CalendarViewModel calendarViewModel) {
        this.viewModel = viewModel;
        this.dialogManagerModel = dialogManagerModel;
        this.calendarViewModel = calendarViewModel;
    }

    public void setFilterSubgoalsController(FilterSubgoalsController filterSubgoalsController) {
        this.filterSubgoalsController = filterSubgoalsController;
    }

    @Override
    public void present(ShowSubgoalOutputData outputData) {
        updateState(outputData);

        // Open the dialog when called from execute() method
        dialogManagerModel.setState("show subgoal");
        dialogManagerModel.firePropertyChange();

    }

    /**
     * Update the state without opening a new dialog.
     * Used when updating priority or completion of an already-open dialog.
     */
    public void presentUpdate(ShowSubgoalOutputData outputData) {
        updateState(outputData);
        // Don't fire dialogManagerModel - dialog is already open
    }

    private void updateState(ShowSubgoalOutputData outputData) {
        ShowSubgoalState state = viewModel.getState();
        state.setId(outputData.getId());
        state.setName(outputData.getName());
        state.setDescription(outputData.getDescription());
        state.setPriority(outputData.isPriority());
        state.setCompleted(outputData.isCompleted());
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChange();

        // Refresh the calendar view to reflect subgoal changes
        if (calendarViewModel != null) {
            CalendarState calendarState = calendarViewModel.getCalendarState();

            // If a filter is active, re-apply it to show updated results
            if (calendarState.isFilterActive()) {
                System.out.println("ShowSubgoalPresenter: Filter is active, re-applying filter");
                if (filterSubgoalsController != null) {
                    String username = calendarState.getUsername();
                    String planId = calendarState.getFilterPlanId();
                    String subgoalName = calendarState.getFilterSubgoalName();
                    Boolean priorityOnly = calendarState.getFilterPriorityOnly();

                    System.out.println("ShowSubgoalPresenter: Re-applying filter with criteria - planId: " + planId +
                                     ", name: " + subgoalName + ", priority: " + priorityOnly);

                    if (username != null) {
                        filterSubgoalsController.reapplyFilter(username, planId, subgoalName, priorityOnly);
                    } else {
                        System.out.println("ShowSubgoalPresenter: ERROR - username is null!");
                    }
                } else {
                    System.out.println("ShowSubgoalPresenter: ERROR - filterSubgoalsController is null!");
                    calendarViewModel.firePropertyChanged();
                }
            } else {
                System.out.println("ShowSubgoalPresenter: No filter active, normal refresh");
                // No filter active, just refresh normally
                calendarViewModel.firePropertyChanged();
            }
        }
    }

    @Override
    public void presentError(String message) {
        ShowSubgoalState state = viewModel.getState();
        state.setErrorMessage(message);
        viewModel.setState(state);
        viewModel.firePropertyChange();

        dialogManagerModel.setState("show subgoal");
        dialogManagerModel.firePropertyChange();
    }
}
