package use_case.filter_subgoals;

public class FilterSubgoalsInputData {
    private final String planId;
    private final boolean priorityOnly;
    private final String userId;

    public FilterSubgoalsInputData(String userId, String planId, boolean priorityOnly) {
        this.userId = userId;
        this.planId = planId;
        this.priorityOnly = priorityOnly;
    }

    public String getPlanId() { return planId; }
    public boolean isPriorityOnly() { return priorityOnly; }
    public String getUserId() { return userId; }
}