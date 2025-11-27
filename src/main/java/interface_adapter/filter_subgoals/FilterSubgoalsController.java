package interface_adapter.filter_subgoals;

import use_case.filter_subgoals.FilterSubgoalsInputBoundary;
import use_case.filter_subgoals.FilterSubgoalsInputData;

public class FilterSubgoalsController {
    private final FilterSubgoalsInputBoundary filterSubgoalsInteractor;

    public FilterSubgoalsController(FilterSubgoalsInputBoundary filterSubgoalsInteractor) {
        this.filterSubgoalsInteractor = filterSubgoalsInteractor;
    }

    public void execute(String username, String planId, String subgoalName, Boolean priorityOnly) {
        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                username, planId, subgoalName, priorityOnly
        );
        filterSubgoalsInteractor.filter(inputData);
    }

    /**
     * Re-applies the current filter with the given criteria.
     * Used to refresh filtered results after changes to subgoals.
     */
    public void reapplyFilter(String username, String planId, String subgoalName, Boolean priorityOnly) {
        execute(username, planId, subgoalName, priorityOnly);
    }
}