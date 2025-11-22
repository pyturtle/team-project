package interface_adapter.subgoal_qa;

import use_case.subgoal.subgoal_qa.SubgoalQAOutputBoundary;
import use_case.subgoal.subgoal_qa.SubgoalQAOutputData;

public class SubgoalQAPresenter implements SubgoalQAOutputBoundary {

    private final SubgoalQAViewModel viewModel;

    public SubgoalQAPresenter(SubgoalQAViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SubgoalQAOutputData outputData) {
        SubgoalQAState state = viewModel.getState();
        state.setSubgoalId(outputData.getSubgoalId());
        state.setHistory(outputData.getHistory());
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        SubgoalQAState state = viewModel.getState();
        state.setErrorMessage(message);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
