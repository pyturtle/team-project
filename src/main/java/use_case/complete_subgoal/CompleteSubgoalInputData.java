package use_case.complete_subgoal;

public class CompleteSubgoalInputData {
    private final int subgoalId;

    public CompleteSubgoalInputData(int subgoalId) {
        this.subgoalId = subgoalId;
    }

    public int getSubgoalId() {
        return subgoalId;
    }
}
