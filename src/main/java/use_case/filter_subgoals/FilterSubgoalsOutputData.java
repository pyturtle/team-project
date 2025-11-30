package use_case.filter_subgoals;

import entity.subgoal.Subgoal;

import java.util.List;

public class FilterSubgoalsOutputData {

    private final List<Subgoal> filteredSubgoals;
    private final String planId;
    private final String subgoalName;
    private final Boolean priorityOnly;

    public FilterSubgoalsOutputData(List<Subgoal> filteredSubgoals, String planId, String subgoalName, Boolean priorityOnly) {
        this.filteredSubgoals = filteredSubgoals;
        this.planId = planId;
        this.subgoalName = subgoalName;
        this.priorityOnly = priorityOnly;
    }

    public List<Subgoal> getFilteredSubgoals() {
        return filteredSubgoals;
    }

    public String getPlanId() {
        return planId;
    }

    public String getSubgoalName() {
        return subgoalName;
    }

    public Boolean getPriorityOnly() {
        return priorityOnly;
    }
}
