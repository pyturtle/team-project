package interface_adapter.subgoal_qna;
import interface_adapter.DialogManagerModel;
import use_case.subgoal.qna.SubgoalQnaOutputBoundary;
import use_case.subgoal.qna.SubgoalQnaOutputData;

/**
 * Presenter for Subgoal Q/A.
 * - Updates the SubgoalQnaViewModel with history / errors.
 * - Asks DialogManagerModel to open the Q/A dialog on first open.
 */
public class SubgoalQnaPresenter implements SubgoalQnaOutputBoundary {

    private final SubgoalQnaViewModel viewModel;
    private final DialogManagerModel dialogManagerModel;

    public SubgoalQnaPresenter(SubgoalQnaViewModel viewModel,
                               DialogManagerModel dialogManagerModel) {
        this.viewModel = viewModel;
        this.dialogManagerModel = dialogManagerModel;
    }

    private void updateState(SubgoalQnaOutputData out) {
        SubgoalQnaState state = viewModel.getState();
        state.setSubgoalId(out.getSubgoalId());
        state.setHistory(out.getHistory());
        state.setErrorMessage("");
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentInitial(SubgoalQnaOutputData out) {
        // 1. Update Q/A state (history, subgoalId)
        updateState(out);
        // 2. Tell DialogManager to open the "subgoal qna" dialog
        dialogManagerModel.setState("subgoal qna");
        dialogManagerModel.firePropertyChange();
    }

    @Override
    public void presentUpdate(SubgoalQnaOutputData out) {
        // Just refresh the content; dialog already open
        updateState(out);
    }

    @Override
    public void presentError(String message) {
        SubgoalQnaState state = viewModel.getState();
        state.setErrorMessage(message);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
