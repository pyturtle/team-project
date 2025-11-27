package use_case.complete_subgoal;

public class CompleteSubgoalOutputData {
    private final String subgoalId;

    public CompleteSubgoalOutputData(String subgoalId) {
        this.subgoalId = subgoalId;
    }

    public String getSubgoalId() {
        return subgoalId;
    }
}
