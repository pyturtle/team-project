package data_access.file;

import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.subgoal.SubgoalQnaDataAccessInterface;
import entity.subgoal.SubgoalQuestionAnswer;
import entity.subgoal.SubgoalQuestionAnswerBuilder;
import org.json.JSONObject;

import java.util.*;

/**
 * File-backed DAO for Subgoal Q/A history.
 * Stores all entries in a single JSON file.
 */
public class FileSubgoalQnaDataAccessObject extends JsonDataAccess<SubgoalQuestionAnswer>
        implements SubgoalQnaDataAccessInterface {

    private final ArrayList<SubgoalQuestionAnswer> subgoalQuestionAnswers = new ArrayList<>();

    public FileSubgoalQnaDataAccessObject(String qnaFilePath) {
        super(qnaFilePath);
        if (qnaFilePath != null) {
            try {
                loadFromJson(subgoalQuestionAnswers);
            } catch (Exception e) {
                System.err.println("Could not load Q/A history: " + e.getMessage());
            }
        }
    }

    @Override
    public SubgoalQuestionAnswer parseJsonObject(JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String subgoalId = jsonObject.getString("subgoal_id");
        String question = jsonObject.getString("question");
        String response = jsonObject.optString("response", "");
        return new SubgoalQuestionAnswer(id, subgoalId, question, response);
    }

    @Override
    public JSONObject convertObjectToJson(SubgoalQuestionAnswer object) {
        JSONObject obj = new JSONObject();
        obj.put("id", object.getId());
        obj.put("subgoal_id", object.getSubgoalId());
        obj.put("question", object.getQuestionMessage());
        obj.put("response", object.getResponseMessage());
        return obj;
    }

    @Override
    public synchronized List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
        ArrayList<SubgoalQuestionAnswer> questionAsnwers = new ArrayList<>();
        for (SubgoalQuestionAnswer entry : subgoalQuestionAnswers) {
            if (entry.getSubgoalId().equals(subgoalId)) {
                questionAsnwers.add(entry);
            }
        }
        return questionAsnwers;
    }

    @Override
    public synchronized SubgoalQuestionAnswer appendEntry(String subgoalId,
                                                          String questionMessage,
                                                          String responseMessage) {
        SubgoalQuestionAnswerBuilder builder = new SubgoalQuestionAnswerBuilder();
        SubgoalQuestionAnswer entry = builder.generateId()
                .setSubgoalId(subgoalId)
                .setQuestionMessage(questionMessage)
                .setResponseMessage(responseMessage)
                .build();
        subgoalQuestionAnswers.add(entry);
        saveToJson(subgoalQuestionAnswers);
        return entry;
    }
}
