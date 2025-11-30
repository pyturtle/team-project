package use_case.plan.show_plan;

import org.json.JSONObject;

/**
 * ShowPlanController receives a plan object from the UI,
 * wraps it in ShowPlanInputData, and forwards it to the interactor.
 */
public class ShowPlanController {
    private final ShowPlanInputBoundary showPlanInteractor;

    /**
     * Creates a ShowPlanController with the given interactor.
     * @param showPlanInteractor the interactor responsible for handling the show plan use case
     */
    public ShowPlanController(ShowPlanInputBoundary showPlanInteractor) {
        this.showPlanInteractor = showPlanInteractor;
    }

    /**
     * Executes the show plan use case by converting the given plan JSON
     * into input data and passing it to the interactor.
     * @param planObject the JSON representation of the selected plan
     */
    public void execute(JSONObject planObject) {
        final ShowPlanInputData showPlanInputData =
                new ShowPlanInputData(planObject);
        showPlanInteractor.execute(showPlanInputData);
    }
}
