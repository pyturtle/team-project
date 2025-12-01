package interface_adapter.subgoal.subgoal_qna;

import entity.subgoal.SubgoalQuestionAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * State object backing the Subgoal Q/A view.
 */
public class SubgoalQnaState {

    private String subgoalId = "";
    private List<SubgoalQuestionAnswer> history = new ArrayList<>();
    private String errorMessage = "";

    public String getSubgoalId() {
        return subgoalId;
    }

    public void setSubgoalId(String subgoalId) {
        this.subgoalId = subgoalId;
    }

    public List<SubgoalQuestionAnswer> getHistory() {
        return history;
    }

    public void setHistory(List<SubgoalQuestionAnswer> history) {
        this.history = history;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
