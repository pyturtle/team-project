package use_case.plan.show_plan;

/**
 * ShowPlanInputBoundary defines the input boundary for the show plan use case.
 * It accepts structured input data describing which plan should be displayed.
 */
public interface ShowPlanInputBoundary {

    /**
     * Executes the show plan use case using the provided input data.
     * @param showPlanInputData the data containing the plan information to display
     */
    void execute(ShowPlanInputData showPlanInputData);
}
