package interface_adapter.filter_subgoals;

import use_case.filter_subgoals.FilterSubgoalsInputBoundary;
import use_case.filter_subgoals.FilterSubgoalsInputData;

public class FilterSubgoalsController {
    private final FilterSubgoalsInputBoundary filterSubgoalsInteractor;

    public FilterSubgoalsController(FilterSubgoalsInputBoundary filterSubgoalsInteractor) {
        this.filterSubgoalsInteractor = filterSubgoalsInteractor;
    }

    public void execute(String planId, Boolean priorityOnly) {
        // Get current user ID as String
        String currentUserId = getCurrentUserId();

        FilterSubgoalsInputData inputData = new FilterSubgoalsInputData(
                currentUserId, planId, priorityOnly
        );
        filterSubgoalsInteractor.filter(inputData);
    }

    public void executeWithKeyword(String keyword) {
        // If you have keyword filtering in your use case, add this method
        // Otherwise, we'll handle keyword filtering in the view for now
    }

    private String getCurrentUserId() {
        // You'll need to implement this based on how you handle current user
        // For now, return a default or get from your user system
        return "99"; // Using the test user ID from your example as String
    }
}