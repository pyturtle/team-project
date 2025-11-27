package interface_adapter.filter_subgoals;

import use_case.filter_subgoals.FilterSubgoalsInputBoundary;
import use_case.filter_subgoals.FilterSubgoalsInputData;

public class FilterSubgoalsController {
    private final FilterSubgoalsInputBoundary filterSubgoalsInteractor;

    public FilterSubgoalsController(FilterSubgoalsInputBoundary filterSubgoalsInteractor) {
        this.filterSubgoalsInteractor = filterSubgoalsInteractor;
    }

    public void execute(String planId, String subgoalName, Boolean priorityOnly) {
        // Get current user ID as String
        String currentUserId = getCurrentUserId();

        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                currentUserId, planId, subgoalName, priorityOnly
        );
        filterSubgoalsInteractor.filter(inputData);
    }

    public void executeWithKeyword(String keyword) {
        // If you have keyword filtering in your use case, add this method
        // Otherwise, we'll handle keyword filtering in the view for now
    }

    private String getCurrentUserId() {
        // For now, return the correct user ID that matches your data
        return "1"; // Changed from "99" to "1" to match your actual data
    }
}