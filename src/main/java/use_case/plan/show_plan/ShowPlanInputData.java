package use_case.plan.show_plan;

import org.json.JSONObject;

public class ShowPlanInputData {
    private final JSONObject planObject;

    public ShowPlanInputData(JSONObject planObject) {
        this.planObject = planObject;
    }

    public JSONObject getPlanObject() {
        return planObject;
    }
}
