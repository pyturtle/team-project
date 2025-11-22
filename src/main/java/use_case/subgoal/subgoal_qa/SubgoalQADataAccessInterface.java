package use_case.subgoal.subgoal_qa;

import org.json.JSONObject;

/**
 * Data access interface for Subgoal Q/A.
 * Responsible for sending a question to Gemini and returning a wrapped response.
 */
public interface SubgoalQADataAccessInterface {

    /**
     * Ask Gemini how to complete a specific subgoal.
     *
     * @param subgoalName subgoal title
     * @param subgoalDescription subgoal description
     * @param userQuestion the user's question
     * @return JSONObject with keys: success (boolean), responseMessage (String), responseObject (JSONObject)
     */
    JSONObject askGemini(String subgoalName, String subgoalDescription, String userQuestion);
}
