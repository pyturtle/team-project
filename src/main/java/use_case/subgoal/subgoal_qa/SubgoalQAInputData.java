package use_case.subgoal.subgoal_qa;

public class SubgoalQAInputData {
    private final String subgoalId;
    private final String question;

    public SubgoalQAInputData(String subgoalId, String question) {
        this.subgoalId = subgoalId;
        this.question = question;
    }

    public String getSubgoalId() {
        return subgoalId;
    }

    public String getQuestion() {
        return question;
    }
}
