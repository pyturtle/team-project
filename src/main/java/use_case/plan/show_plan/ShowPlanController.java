package use_case.plan.show_plan;

import org.json.JSONObject;

public class ShowPlanController {
    private final ShowPlanInputBoundary showPlanInteractor;

    public ShowPlanController(ShowPlanInputBoundary showPlanInteractor) {
        this.showPlanInteractor = showPlanInteractor;
    }

    public void execute(JSONObject planObject) {
        final ShowPlanInputData showPlanInputData =
                new ShowPlanInputData(planObject);
        showPlanInteractor.execute(showPlanInputData);
    }
}
