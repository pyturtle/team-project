package use_case.subgoal.qna;

import entity.SubgoalQuestionAnswer;
import java.util.List;

/**
 * Output data for Subgoal Q/A.
 */
public class SubgoalQnaOutputData {
    private final String subgoalId;
    private final List<SubgoalQuestionAnswer> history;

    public SubgoalQnaOutputData(String subgoalId,
                                List<SubgoalQuestionAnswer> history) {
        this.subgoalId = subgoalId;
        this.history = history;
    }

    public String getSubgoalId() {
        return subgoalId;
    }

    public List<SubgoalQuestionAnswer> getHistory() {
        return history;
    }
}
