package use_case.show_plans;

/**
 * Input Boundary for the Show Plans Use Case.
 */
public interface ShowPlansInputBoundary {

    /**
     * Executes the show plans use case.
     * @param showPlansInputData the input data
     */
    void execute(ShowPlansInputData showPlansInputData);

    /**
     * Executes the show plans use case to switch to the show plans view.
     */
    void switchToShowPlansView();

    /**
     * Switches back to the logged in view.
     */
    void switchToLoggedInView();
}
