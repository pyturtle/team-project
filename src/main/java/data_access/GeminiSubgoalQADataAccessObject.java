package data_access;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.json.JSONObject;
import use_case.subgoal.subgoal_qa.SubgoalQADataAccessInterface;

/**
 * Gemini-backed DAO for Subgoal Q/A.
 */
public class GeminiSubgoalQADataAccessObject implements SubgoalQADataAccessInterface {

    private final String apiKey = "AIzaSyCub87wCtvtm4OBN-BHmFPSoBCaaE4hKT0";
    private final Client client;

    public GeminiSubgoalQADataAccessObject() {
        client = Client.builder().apiKey(apiKey).build();
    }

    @Override
    public JSONObject askGemini(String subgoalName, String subgoalDescription, String userQuestion) {
        try {
            String prompt =
                    "You are helping a user complete a subgoal.\n" +
                            "Subgoal name: " + subgoalName + "\n" +
                            "Subgoal description: " + subgoalDescription + "\n\n" +
                            "User question: " + userQuestion + "\n\n" +
                            "Give a clear, step-by-step answer tailored to this subgoal. " +
                            "Return ONLY a JSON object of the form:\n" +
                            "{ \"answer\": \"...\" }\n" +
                            "No markdown, no extra text.";

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
            );

            String rawResult = response.text();
            int first = rawResult.indexOf('\n');
            int last = rawResult.lastIndexOf('\n');

            String responseText = (first >= 0 && last > first)
                    ? rawResult.substring(first + 1, last)
                    : rawResult;

            JSONObject responseObject = new JSONObject(responseText);

            return prepareResponse(responseObject, "Answer generated successfully!", true);
        } catch (Exception e) {
            return prepareResponse(new JSONObject(), "Something went wrong. Please try again.", false);
        }
    }

    private JSONObject prepareResponse(JSONObject responseObject, String responseMessage, boolean success) {
        return new JSONObject()
                .put("success", success)
                .put("responseMessage", responseMessage)
                .put("responseObject", responseObject);
    }
}
