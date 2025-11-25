package interface_adapter.show_subgoal;

import interface_adapter.DialogManagerModel;
import use_case.subgoal.show_subgoal.ShowSubgoalOutputBoundary;
import use_case.subgoal.show_subgoal.ShowSubgoalOutputData;

/**
 * Presenter for the ShowSubgoal use case.
 * Maps output data to the ShowSubgoalViewModel.
 */
public class ShowSubgoalPresenter implements ShowSubgoalOutputBoundary {

    private final ShowSubgoalViewModel viewModel;
    private final DialogManagerModel dialogManagerModel;

    /**
     * Constructs a ShowSubgoalPresenter.
     *
     * @param viewModel the view model to update with subgoal data
     * @param dialogManagerModel the dialog manager model used to trigger popups
     */
    public ShowSubgoalPresenter(ShowSubgoalViewModel viewModel,
                                DialogManagerModel dialogManagerModel) {
        this.viewModel = viewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    @Override
    public void present(ShowSubgoalOutputData outputData) {
        ShowSubgoalState state = viewModel.getState();
        state.setName(outputData.getName());
        state.setDescription(outputData.getDescription());
        state.setPriority(outputData.isPriority());
        state.setCompleted(outputData.isCompleted());
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChange();  // update Swing bindings

        // Tell the DialogManager to open the 'subgoal' dialog
        dialogManagerModel.setState(viewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        ShowSubgoalState state = viewModel.getState();
        state.setErrorMessage(message);

        viewModel.setState(state);
        viewModel.firePropertyChange();

        dialogManagerModel.setState(viewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }
}