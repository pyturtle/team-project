package interface_adapter.show_subgoal;

import interface_adapter.DialogManagerModel;
import interface_adapter.calendar.CalendarViewModel;
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

    public ShowSubgoalPresenter(ShowSubgoalViewModel viewModel, DialogManagerModel dialogManagerModel,
                                CalendarViewModel calendarViewModel) {
        this.viewModel = viewModel;
        this.dialogManagerModel = dialogManagerModel;
        this.calendarViewModel = calendarViewModel;
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
            calendarViewModel.firePropertyChanged();
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
