package data_access.api;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import data_access.interfaces.plan.GeneratePlanDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalQnaGeminiDataAccessInterface;

/**
 * GeminiApiDataAccessObject is a data access class that communicates with the Gemini API.
 * It generates structured plans with subgoals and answers user questions related to subgoals.
 * This class implements both GeneratePlanDataAccessInterface
 * and SubgoalQnaGeminiDataAccessInterface.
 */
public class GeminiApiDataAccessObject
        implements GeneratePlanDataAccessInterface, SubgoalQnaGeminiDataAccessInterface {

    private final String apiKey = "INSERT YOUR KEY HERE";
    private final Client client;

    /**
     * Creates a new GeminiApiDataAccessObject and initializes
     * the Gemini client with the API key.
     */
    public GeminiApiDataAccessObject() {
        client = Client.builder().apiKey(apiKey).build();
    }

    /**
     * Requests a plan from the Gemini API based on the user's message.
     * The response is expected to be a JSON object describing a plan with
     * subgoals and deadlines. The returned JSON wrapper contains keys:
     * success, responseMessage, and responseObject.
     *
     * @param userMessage the user's input describing the desired plan or goal
     * @return a JSONObject containing the success flag, a response message,
     *         and the parsed plan object if valid
     */
    @Override
    public JSONObject getPlan(String userMessage) {
        boolean success = true;
        String responseMessage = "Plan generated successfully!";
        JSONObject responseObject = new JSONObject();
        try {
            final GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    "Here is the plan: " + userMessage + ". Come up with 5-10 subgoals that "
                            + "the user needs to complete in chronological order to achieve this goal. "
                            + "If a user provides time frame for plan completion, "
                            + "split the time interval equaly for all "
                            + "the subgoals and assign the completion deadline to each of them. If not, then assign "
                            + "deadlines the way so that the user has enough time to complete each subgoal."
                            + "IMPORTANT: Make sure all deadline dates are later than " + LocalDate.now()
                            + "."
                            + "Generate a json of the following format:"
                            + "For the plan, the key-value pairs should be: "
                            + "1. name - plan name string"
                            + "2. description - plan description string"
                            + "3. subgoals - json array of subgoals"
                            + "For each subgoal, the key-value pairs should be: "
                            + "1. name - subgoal name string"
                            + "2. description - subgoal description string"
                            + "3. deadline - subgoal description YYYY-MM-DD string"
                            + "IMPORTANT: Do not include anything else in the response "
                            + "apart from the json object.",
                    null);
            final String rawResult = response.text();
            final int first = rawResult.indexOf('\n');
            final int last = rawResult.lastIndexOf('\n');

            final String responseText = (first >= 0 && last > first)
                    ? rawResult.substring(first + 1, last)
                    : "";
            responseObject = new JSONObject(responseText);
            final boolean isValid = validateJsonObject(responseObject);
            if (!isValid) {
                responseMessage = "Something went wrong! Please, try again.";
                success = false;
            }
        }
        catch (Exception ex) {
            responseMessage = "Something went wrong! Please, try again.";
            success = false;
        }
        return prepareResponse(responseObject, responseMessage, success);
    }

    /**
     * Requests an answer from the Gemini API for a given user question about a subgoal.
     * Returns a plain text answer or a fallback message if an error occurs
     * or no text is returned.
     *
     * @param userMessage the question or prompt provided by the user
     * @return a plain text answer from Gemini, or an error message if the request fails
     */
    @Override
    public String getAnswerForQuestion(String userMessage) {
        String returnMessage = "";
        final String errorMessage =
                "Sorry, I couldn't get an answer from Gemini right now.";
        try {
            final GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    "You are a helpful assistant helping a user with a subgoal in their plan. "
                            + "Answer the following question clearly and concisely:\n\n"
                            + userMessage
                            + ". Do not apply any formating to your response.",
                    null
            );
            final String text = response.text();
            if (text == null || text.trim().isEmpty()) {
                returnMessage = errorMessage;
            }
            returnMessage = text.trim();
        }
        // -@cs[IllegalCatch] catch generic exception to provide user-friendly error message
        catch (Exception ex) {
            returnMessage = errorMessage;
        }
        return returnMessage;
    }

    /**
     * Validates that the given response JSON object has the expected plan structure.
     * The object must contain name, description, and subgoals fields.
     * Each subgoal must contain name, description, and deadline fields, and the deadline
     * must be a valid date.
     *
     * @param responseObject the JSON object to validate
     * @return true if the JSON object is structurally valid, false otherwise
     */
    private boolean validateJsonObject(JSONObject responseObject) {
        boolean isValid;
        try {
            isValid = responseObject.has("name")
                    && responseObject.has("description")
                    && responseObject.has("subgoals");
            final JSONArray subgoals = responseObject.getJSONArray("subgoals");
            int i = 0;
            while (isValid) {
                final JSONObject subgoal = subgoals.getJSONObject(i);
                isValid = subgoal.has("name")
                        && subgoal.has("description")
                        && subgoal.has("deadline");
                LocalDate.parse(subgoal.getString("deadline"));
                i++;
                if (i == subgoals.length()) {
                    break;
                }
            }
        }
        // -@cs[IllegalCatch] any parsing/JSON error invalidates the structure
        catch (Exception ex) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Wraps a response object, message, and success flag into a standard JSON format.
     * The returned JSON contains keys: success, responseMessage, and responseObject.
     *
     * @param responseObject  the main response payload to include
     * @param responseMessage a human-readable message describing the result
     * @param success         true if the operation succeeded, false otherwise
     * @return a JSONObject containing the success flag, message, and response object
     */
    private JSONObject prepareResponse(JSONObject responseObject,
                                       String responseMessage,
                                       boolean success) {
        return new JSONObject()
                .put("success", success)
                .put("responseMessage", responseMessage)
                .put("responseObject", responseObject);
    }
}
