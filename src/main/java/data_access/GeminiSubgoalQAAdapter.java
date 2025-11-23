package data_access;

import org.json.JSONObject;
import use_case.subgoal.subgoal_qa.GeminiQADataAccessInterface;

/**
 * Adapter so SubgoalQAInteractor can use GeminiSubgoalQADataAccessObject
 * without changing the use case layer.
 */
public class GeminiSubgoalQAAdapter implements GeminiQADataAccessInterface {

    private final GeminiSubgoalQADataAccessObject dao;

    public GeminiSubgoalQAAdapter(GeminiSubgoalQADataAccessObject dao) {
        this.dao = dao;
    }

    @Override
    public String getAnswerForQuestion(String prompt) {
        // prompt already includes everything the model needs,
        // so name/description placeholders are fine.
        JSONObject res = dao.askGemini("", "", prompt);

        if (!res.optBoolean("success")) {
            throw new RuntimeException(res.optString("responseMessage"));
        }

        return res.getJSONObject("responseObject").optString("answer", "");
    }
}
