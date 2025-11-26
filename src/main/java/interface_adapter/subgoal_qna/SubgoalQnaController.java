package interface_adapter.subgoal_qna;

import use_case.subgoal.qna.SubgoalQnaAskInputData;
import use_case.subgoal.qna.SubgoalQnaInputBoundary;
import use_case.subgoal.qna.SubgoalQnaOpenInputData;

/**
 * Controller for Subgoal Q/A.
 */
public class SubgoalQnaController {

    private final SubgoalQnaInputBoundary interactor;

    public SubgoalQnaController(SubgoalQnaInputBoundary interactor) {
        this.interactor = interactor;
    }

    /** Open the Q/A dialog for a given subgoal (load history). */
    public void open(String subgoalId) {
        interactor.open(new SubgoalQnaOpenInputData(subgoalId));
    }

    /** Ask a new question about the given subgoal. */
    public void askQuestion(String subgoalId, String question) {
        interactor.ask(new SubgoalQnaAskInputData(subgoalId, question));
    }
}
