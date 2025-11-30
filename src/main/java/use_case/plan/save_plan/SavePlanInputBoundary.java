package use_case.plan.save_plan;

/**
 * SavePlanInputBoundary defines the input boundary for the save plan use case.
 * It accepts structured plan input data and triggers the saving process.
 */
public interface SavePlanInputBoundary {

    /**
     * Executes the save plan use case using the provided input data.
     * @param savePlanInputData the data required to save a plan and its subgoals
     */
    void execute(SavePlanInputData savePlanInputData);
}
