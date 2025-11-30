package use_case.plan.show_plans;

/**
 * Output Boundary for the Show Plans Use Case.
 */
public interface ShowPlansOutputBoundary {

    /**
     * Prepares the success view for the Show Plans Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(ShowPlansOutputData outputData);

    /**
     * Prepares the failure view for the Show Plans Use Case.
     *
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Show Plans view.
     */
    void switchToShowPlansView();
}
