package use_case.subgoal.qna;

/**
 * Gateway interface for calling Gemini from the Subgoal Q/A use case.
 */
public interface SubgoalQnaGeminiDataAccessInterface {

    /**
     * Returns an answer from Gemini for the given user question.
     */
    String getAnswerForQuestion(String question);
}
