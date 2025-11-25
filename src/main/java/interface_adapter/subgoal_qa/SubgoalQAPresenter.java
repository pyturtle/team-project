package interface_adapter.subgoal_qa;

import interface_adapter.DialogManagerModel;
import use_case.subgoal.subgoal_qa.SubgoalQAOutputBoundary;
import use_case.subgoal.subgoal_qa.SubgoalQAOutputData;

/**
 * Presenter for Subgoal Q/A use case.
 * Updates SubgoalQAViewModel and triggers the Q/A dialog via DialogManagerModel.
 */
public class SubgoalQAPresenter implements SubgoalQAOutputBoundary {

    private final SubgoalQAViewModel viewModel;
    private final DialogManagerModel dialogManagerModel;

    public SubgoalQAPresenter(SubgoalQAViewModel viewModel,
                              DialogManagerModel dialogManagerModel) {
        this.viewModel = viewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    @Override
    public void present(SubgoalQAOutputData outputData) {
        SubgoalQAState state = viewModel.getState();
        state.setSubgoalId(outputData.getSubgoalId());
        state.setHistory(outputData.getHistory());
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChange();

        // open the Q/A dialog
        dialogManagerModel.setState(viewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        SubgoalQAState state = viewModel.getState();
        state.setErrorMessage(message);

        viewModel.setState(state);
        viewModel.firePropertyChange();

        // still open dialog so user sees the error
        dialogManagerModel.setState(viewModel.getViewName());
        dialogManagerModel.firePropertyChange();
    }
}
