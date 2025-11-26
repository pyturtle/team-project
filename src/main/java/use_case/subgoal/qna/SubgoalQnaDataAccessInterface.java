package use_case.subgoal.qna;

import entity.SubgoalQuestionAnswer;
import java.util.List;

/**
 * Data access interface for per-subgoal Q/A history.
 */
public interface SubgoalQnaDataAccessInterface {

    /**
     * Returns a copy of the chat history for the given subgoal.
     */
    List<SubgoalQuestionAnswer> getHistory(String subgoalId);

    /**
     * Appends a new question–answer entry to the history of the given subgoal
     * and returns the created entity.
     */
    SubgoalQuestionAnswer appendEntry(String subgoalId,
                                      String questionMessage,
                                      String responseMessage);
}
