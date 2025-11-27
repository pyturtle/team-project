package use_case.subgoal.qna;

public class SubgoalQnaAskInputData {
    private final String subgoalId;
    private final String questionMessage;

    public SubgoalQnaAskInputData(String subgoalId, String questionMessage) {
        this.subgoalId = subgoalId;
        this.questionMessage = questionMessage;
    }

    public String getSubgoalId() {
        return subgoalId;
    }

    public String getQuestionMessage() {
        return questionMessage;
    }
}
