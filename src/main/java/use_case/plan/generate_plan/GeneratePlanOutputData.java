package use_case.plan.generate_plan;

import org.json.JSONObject;

/**
 * GeneratePlanOutputData stores the results of the generate plan use case,
 * including the generated plan object, success flag, a response message,
 * and the original user message.
 */
public class GeneratePlanOutputData {
    private final JSONObject responseObject;
    private final boolean success;
    private final String responseMessage;
    private final String userMessage;

    /**
     * Creates a new GeneratePlanOutputData instance with all required fields.
     * @param responseObject the JSON object containing the generated plan
     * @param success whether the generation was successful
     * @param responseMessage a descriptive message about the result
     * @param userMessage the original message provided by the user
     */
    public GeneratePlanOutputData(JSONObject responseObject, boolean success,
                                  String responseMessage, String userMessage) {
        this.responseObject = responseObject;
        this.success = success;
        this.responseMessage = responseMessage;
        this.userMessage = userMessage;
    }

    /**
     * Returns the message describing the result of the plan generation.
     * @return the response message
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Returns the original user message that triggered plan generation.
     * @return the user message
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Returns the generated plan represented as a JSON object.
     * @return the plan JSON object
     */
    public JSONObject getResponseObject() {
        return responseObject;
    }

    /**
     * Returns whether the plan generation process succeeded.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
}
