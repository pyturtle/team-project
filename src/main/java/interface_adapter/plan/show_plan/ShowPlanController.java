package interface_adapter.plan.show_plan;

import org.json.JSONObject;
import use_case.plan.show_plan.ShowPlanInputBoundary;
import use_case.plan.show_plan.ShowPlanInputData;

/**
 * ShowPlanController receives a plan object from the UI,
 * wraps it into input data, and passes it to the Show Plan use case interactor.
 */
public class ShowPlanController {
    private final ShowPlanInputBoundary showPlanInteractor;

    /**
     * Creates a new ShowPlanController with the given interactor.
     * @param showPlanInteractor the interactor responsible for showing plan details
     */
    public ShowPlanController(ShowPlanInputBoundary showPlanInteractor) {
        this.showPlanInteractor = showPlanInteractor;
    }

    /**
     * Executes the show plan use case by passing the selected plan object to the interactor.
     * @param planObject the JSON object representing the plan to display
     */
    public void execute(JSONObject planObject) {
        final ShowPlanInputData showPlanInputData =
                new ShowPlanInputData(planObject);
        showPlanInteractor.execute(showPlanInputData);
    }
}
