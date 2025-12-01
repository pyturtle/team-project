package use_case.plan.delete_plan;

/**
 * Output Boundary for the Delete Plan Use Case.
 */
public interface DeletePlanOutputBoundary {

    /**
     * Prepares the success view.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(DeletePlanOutputData outputData);

    /**
     * Prepares the fail view.
     *
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}

