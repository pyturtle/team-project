package use_case.filter_subgoals;

public class FilterSubgoalsInputData {
    private final Integer planId;
    private final boolean priorityOnly;
    private final int userId;

    public FilterSubgoalsInputData(int userId, Integer planId, boolean priorityOnly) {
        this.userId = userId;
        this.planId = planId;
        this.priorityOnly = priorityOnly;

    }

    public Integer getPlanId() { return planId; }
    public boolean isPriorityOnly() { return priorityOnly; }
    public int getUserId() { return userId; }
}
