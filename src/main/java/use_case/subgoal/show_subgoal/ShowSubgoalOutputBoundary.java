package use_case.subgoal.show_subgoal;

/**
 * Output boundary for the ShowSubgoal use case.
 * Implemented by the Presenter.
 */
public interface ShowSubgoalOutputBoundary {

    /**
     * Presents the loaded subgoal information to the user.
     *
     * @param outputData the data to present
     */
    void present(ShowSubgoalOutputData outputData);

    /**
     * Presents an error that occurred while loading or updating a subgoal.
     *
     * @param message the error message to show
     */
    void presentError(String message);
}
