package use_case.filter_subgoals;

public class FilterSubgoalsInputData {
    private final String planId;
    private final boolean priorityOnly;
    private final String userId;
    private final String subgoalName;

    public FilterSubgoalsInputData(String userId, String planId, String subgoalName, boolean priorityOnly) {
        this.userId = userId;
        this.planId = planId;
        this.subgoalName = subgoalName;
        this.priorityOnly = priorityOnly;

    }

    public String getPlanId() { return planId; }
    public boolean isPriorityOnly() { return priorityOnly; }
    public String getUserId() { return userId; }
    public String getSubgoalName() { return subgoalName; }
}