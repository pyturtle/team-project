package use_case.complete_subgoal;

public class CompleteSubgoalOutputData {
    private final int subgoalId;

    public CompleteSubgoalOutputData(int subgoalId) {
        this.subgoalId = subgoalId;
    }

    public int getSubgoalId() {
        return subgoalId;
    }
}
