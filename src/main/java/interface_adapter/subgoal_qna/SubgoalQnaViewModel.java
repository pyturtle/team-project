package interface_adapter.subgoal_qna;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Subgoal Q/A dialog.
 */
public class SubgoalQnaViewModel extends ViewModel<SubgoalQnaState> {

    public SubgoalQnaViewModel() {
        super("subgoal qna");
        setState(new SubgoalQnaState());
    }
}
