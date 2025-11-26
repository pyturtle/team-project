package use_case.subgoal.qna;

import entity.SubgoalQuestionAnswer;

import java.util.List;

/**
 * Interactor for Subgoal Q/A.
 * Currently uses a stubbed "answer" – Gemini integration comes later.
 */
public class SubgoalQnaInteractor implements SubgoalQnaInputBoundary {

    private final SubgoalQnaDataAccessInterface qnaDAO;
    private final SubgoalQnaOutputBoundary presenter;

    public SubgoalQnaInteractor(SubgoalQnaDataAccessInterface qnaDAO,
                                SubgoalQnaOutputBoundary presenter) {
        this.qnaDAO = qnaDAO;
        this.presenter = presenter;
    }

    @Override
    public void open(SubgoalQnaOpenInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        List<SubgoalQuestionAnswer> history = qnaDAO.getHistory(subgoalId);
        SubgoalQnaOutputData out = new SubgoalQnaOutputData(subgoalId, history);
        presenter.presentInitial(out);
    }

    @Override
    public void ask(SubgoalQnaAskInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        String question = inputData.getQuestionMessage();

        if (question == null || question.trim().isEmpty()) {
            presenter.presentError("Question cannot be empty.");
            return;
        }

        // TODO: later we replace this with a real Gemini call.
        String answer = "This is a placeholder answer. Integrate Gemini here.";

        qnaDAO.appendEntry(subgoalId, question, answer);
        List<SubgoalQuestionAnswer> history = qnaDAO.getHistory(subgoalId);

        SubgoalQnaOutputData out = new SubgoalQnaOutputData(subgoalId, history);
        presenter.presentUpdate(out);
    }
}
