package use_case.subgoal.subgoal_qa;

import entity.SubgoalQuestionAnswer;
import java.util.List;

public class SubgoalQAOutputData {
    private final String subgoalId;
    private final List<SubgoalQuestionAnswer> history;

    public SubgoalQAOutputData(String subgoalId, List<SubgoalQuestionAnswer> history) {
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
