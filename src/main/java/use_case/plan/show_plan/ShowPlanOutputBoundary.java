package use_case.plan.show_plan;

/**
 * ShowPlanOutputBoundary defines the output boundary for the show plan use case.
 * Implementations receive the processed plan data and prepare it for display.
 */
public interface ShowPlanOutputBoundary {

    /**
     * Prepares the view using the provided output data from the show plan use case.
     * @param showPlanOutputData the data containing plan details and subgoals
     */
    void prepareView(ShowPlanOutputData showPlanOutputData);
}
