package interface_adapter.plan.show_plans;

import use_case.plan.show_plans.ShowPlansInputBoundary;
import use_case.plan.show_plans.ShowPlansInputData;

import interface_adapter.show_subgoal.ShowSubgoalController;

/**
 * The Controller for the Show Plans Use Case.
 */
public class ShowPlansController {

    private final ShowPlansInputBoundary showPlansInteractor;

    private ShowSubgoalController showSubgoalController;

    public void setShowSubgoalController(ShowSubgoalController controller) {
        this.showSubgoalController = controller;
    }
    public ShowSubgoalController getShowSubgoalController() {
        return showSubgoalController;
    }

    /**
     * Creates a new Show Plans Controller.
     * @param showPlansInteractor the interactor for show plans use case
     */
    public ShowPlansController(ShowPlansInputBoundary showPlansInteractor) {
        this.showPlansInteractor = showPlansInteractor;
    }


    /**
     * Executes the show plans use case.
     * @param username the username whose plans to show
     * @param page the page number (0-indexed)
     * @param pageSize the number of plans per page
     */
    public void execute(String username, int page, int pageSize) {
        final ShowPlansInputData inputData = new ShowPlansInputData(username, page, pageSize);
        showPlansInteractor.execute(inputData);
    }

    /**
     * Switches to the Show Plans view.
     */
    public void switchToShowPlansView() {
        showPlansInteractor.switchToShowPlansView();
    }
}
