package data_access.interfaces.plan;

import org.json.JSONObject;

public interface GeneratePlanDataAccessInterface {
    JSONObject getPlan(String userMessage);
}
