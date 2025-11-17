package interface_adapter.show_subgoal;

import use_case.show_subgoal.ShowSubgoalOutputBoundary;
import use_case.show_subgoal.ShowSubgoalOutputData;

/**
 * Presenter for the ShowSubgoal use case.
 * Maps output data to the ShowSubgoalViewModel.
 */
public class ShowSubgoalPresenter implements ShowSubgoalOutputBoundary {

    private final ShowSubgoalViewModel viewModel;

    /**
     * Constructs a ShowSubgoalPresenter.
     *
     * @param viewModel the ViewModel to update with subgoal data
     */
    public ShowSubgoalPresenter(ShowSubgoalViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ShowSubgoalOutputData outputData) {
        ShowSubgoalState state = viewModel.getState();
        state.setName(outputData.getName());
        state.setDescription(outputData.getDescription());
        state.setPriority(outputData.isPriority());
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        ShowSubgoalState state = viewModel.getState();
        state.setErrorMessage(message);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
