package use_case.plan.delete_plan;

/**
 * Input Boundary for the Delete Plan Use Case.
 */
public interface DeletePlanInputBoundary {

    /**
     * Executes the delete plan use case.
     *
     * @param inputData the input data containing plan ID and username
     */
    void execute(DeletePlanInputData inputData);
}

