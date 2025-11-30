package use_case.plan.save_plan;

/**
 * SavePlanOutputBoundary defines the output boundary for the save plan use case.
 * Implementations use the output data to prepare the view.
 */
public interface SavePlanOutputBoundary {

    /**
     * Prepares the view using the output data from the save plan use case.
     * @param savePlanOutputData the result of the save plan operation
     */
    void prepareView(SavePlanOutputData savePlanOutputData);
}
