package data_access;

import okhttp3.*;
import org.json.JSONObject;
import use_case.plan.generate_plan.GeneratePlanDataAccessInterface;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import use_case.subgoal.qna.SubgoalQnaGeminiDataAccessInterface;

import java.time.LocalDate;

public class GeminiApiDataAccessObject implements GeneratePlanDataAccessInterface,
        SubgoalQnaGeminiDataAccessInterface {
    private final String apiKey = "AIzaSyCub87wCtvtm4OBN-BHmFPSoBCaaE4hKT0";
    private final Client client;

    public GeminiApiDataAccessObject() {
        client = Client.builder().apiKey(apiKey).build();
    }

    @Override
    public JSONObject getPlan(String userMessage) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    "Here is the plan: " + userMessage + ". Come up with 5-10 subgoals that " +
                            "the user needs to complete in chronological order to achieve this goal. " +
                            "If a user provides time frame for plan completion, " +
                            "split the time interval equaly for all " +
                            "the subgoals and assign the completion deadline to each of them. If not, then assign " +
                            "deadlines the way so that the user has enough time to complete each subgoal." +
                            "IMPORTANT: Make sure all deadline dates are later than " + LocalDate.now().toString()
                            + "." +
                            "Generate a json of the following format:" +
                            "For the plan, the key-value pairs should be: " +
                            "1. id - (generate unique guid)" +
                            "2. name - plan name string" +
                            "3. description - plan description string" +
                            "3. user_email - sample email string" +
                            "4. subgoals - json array of subgoals" +
                            "For each subgoal, the key-value pairs should be: " +
                            "1. id - (generate unique guid)" +
                            "2. name - subgoal name string" +
                            "3. description - subgoal description string" +
                            "4. deadline - subgoal description YYYY-MM-DD string" +
                            "IMPORTANT: Do not include anything else in the response apart from the json object.",
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

    @Override
    public String getAnswerForQuestion(String userMessage) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    "You are a helpful assistant helping a user with a subgoal in their plan. " +
                            "Answer the following question clearly and concisely:\n\n" + userMessage,
                    null
            );
            String text = response.text();
            if (text == null || text.trim().isEmpty()) {
                return "Sorry, I couldn't get a response from Gemini.";
            }
            return text.trim();
        }
        catch (Exception e) {
            return "Sorry, I couldn't get an answer from Gemini right now.";
        }
    }

    private JSONObject prepareResponse(JSONObject responseObject, String responseMessage, boolean success) {
        return new JSONObject()
                .put("success", success)
                .put("responseMessage", responseMessage)
                .put("responseObject", responseObject);
    }
}
