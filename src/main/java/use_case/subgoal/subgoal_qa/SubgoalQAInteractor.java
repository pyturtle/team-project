package use_case.subgoal.subgoal_qa;

import entity.SubgoalQuestionAnswer;
import java.util.List;

public class SubgoalQAInteractor implements SubgoalQAInputBoundary {

    private final SubgoalQuestionDataAccessInterface historyDAO;
    private final GeminiQADataAccessInterface geminiDAO;
    private final SubgoalQAOutputBoundary presenter;

    public SubgoalQAInteractor(SubgoalQuestionDataAccessInterface historyDAO,
                               GeminiQADataAccessInterface geminiDAO,
                               SubgoalQAOutputBoundary presenter) {
        this.historyDAO = historyDAO;
        this.geminiDAO = geminiDAO;
        this.presenter = presenter;
    }

    @Override
    public void loadHistory(SubgoalQALoadInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        List<SubgoalQuestionAnswer> history =
                historyDAO.getQuestionsForSubgoal(subgoalId);

        presenter.present(new SubgoalQAOutputData(subgoalId, history));
    }

    @Override
    public void askQuestion(SubgoalQAInputData inputData) {
        String subgoalId = inputData.getSubgoalId();
        String question = inputData.getQuestion();

        if (question == null || question.trim().isEmpty()) {
            presenter.presentError("Question cannot be empty.");
            return;
        }

        String prompt = "Subgoal ID: " + subgoalId + "\n"
                + "User question: " + question + "\n"
                + "Answer with clear steps to complete this subgoal.";

        try {
            String answer = geminiDAO.getAnswerForQuestion(prompt);
            historyDAO.saveQuestion(subgoalId, question, answer);

            List<SubgoalQuestionAnswer> history =
                    historyDAO.getQuestionsForSubgoal(subgoalId);

            presenter.present(new SubgoalQAOutputData(subgoalId, history));
        } catch (Exception e) {
            presenter.presentError("Failed to get Gemini response.");
        }
    }
}
