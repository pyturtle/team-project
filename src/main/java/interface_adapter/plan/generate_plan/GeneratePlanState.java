package interface_adapter.plan.generate_plan;

import org.json.JSONObject;

public class GeneratePlanState {
    private String userMessage = "";
    private String responseMessage = "";
    private JSONObject responseObject = new JSONObject();
    private boolean success = false;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public JSONObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(JSONObject responseObject) {
        this.responseObject = responseObject;
    }
}
