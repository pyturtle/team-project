package use_case.plan.generate_plan;

import org.json.JSONObject;

public class GeneratePlanOutputData {
    private final JSONObject responseObject;
    private final boolean success;
    private final String responseMessage;
    private final String userMessage;

    public GeneratePlanOutputData(JSONObject responseObject, boolean success,
                                  String responseMessage, String userMessage) {
        this.responseObject = responseObject;
        this.success = success;
        this.responseMessage = responseMessage;
        this.userMessage = userMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public JSONObject getResponseObject() {
        return responseObject;
    }

    public boolean isSuccess() {
        return success;
    }
}
