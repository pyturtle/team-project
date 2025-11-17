package use_case.plan.generate_plan;

import org.json.JSONObject;

public interface GeneratePlanDataAccessInterface {
    JSONObject getPlan(String userMessage);
}
