package use_case.plan.show_plan;

import org.json.JSONObject;

/**
 * ShowPlanInputData wraps the JSON representation of a plan
 * for use by the show plan interactor.
 */
public class ShowPlanInputData {
    private final JSONObject planObject;

    /**
     * Creates a new ShowPlanInputData instance containing the given plan object.
     * @param planObject the JSON object representing the plan to display
     */
    public ShowPlanInputData(JSONObject planObject) {
        this.planObject = planObject;
    }

    /**
     * Returns the JSON object representing the plan to display.
     * @return the plan JSON object
     */
    public JSONObject getPlanObject() {
        return planObject;
    }
}
