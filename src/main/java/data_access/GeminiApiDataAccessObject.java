package data_access;

import okhttp3.*;
import org.json.JSONObject;
import use_case.plan.generate_plan.GeneratePlanDataAccessInterface;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiApiDataAccessObject implements GeneratePlanDataAccessInterface {
    private final String apiKey = "AIzaSyCub87wCtvtm4OBN-BHmFPSoBCaaE4hKT0";
    private final String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" +
            "gemini-pro:generateContent?key=" +  apiKey;
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public JSONObject getPlan(String userMessage) {
        try {
            Client client = Client.builder().apiKey(apiKey).build();
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    "Here is the plan: " + userMessage + ". I want you to come up with 5-10 subgoals that " +
                            "the user needs to complete in chronological order to achieve this goal. Come up with "+
                            "the name for the goal as well. After you do this, I want you to generate a json " +
                            "object with the plan name, as well as the list of subgoals. For each subgoal, there " +
                            "should be keys and values corresponing to subgoal name, description. Keys that " +
                            "correspond to plan name, names and descriptions should be called \"name\" and " +
                            "\"description\"." +
                            "IMPORTANT: Do not include anything else in the response apart from the json object." +
                            "IMPORTANT: Do not include header of type json```` before the json object.",
                    null);
            String rawResult = response.text();
            int first = rawResult.indexOf('\n');
            int last = rawResult.lastIndexOf('\n');

            String responseText = (first >= 0 && last > first)
                    ? rawResult.substring(first + 1, last)
                    : "";
            JSONObject responseObject = new JSONObject(responseText);
            return prepareResponse(responseObject, "Plan generated successfully!", true);
        }
        catch (Exception e) {
            return prepareResponse(new JSONObject(), "Something went wrong. Please, try again.",
                    false);
        }
    }

    private JSONObject prepareResponse(JSONObject responseObject, String responseMessage, boolean success) {
        return new JSONObject()
                .put("success", success)
                .put("responseMessage", responseMessage)
                .put("responseObject", responseObject);
    }
}
