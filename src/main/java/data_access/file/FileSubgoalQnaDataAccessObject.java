package data_access.file;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import data_access.interfaces.file.JsonDataAccess;
import data_access.interfaces.subgoal.SubgoalQnaDataAccessInterface;
import entity.subgoal.SubgoalQuestionAnswer;
import entity.subgoal.SubgoalQuestionAnswerBuilder;

/**
 * FileSubgoalQnaDataAccessObject is a file-backed DAO that stores and retrieves
 * subgoal question–answer history in JSON format.
 */
public class FileSubgoalQnaDataAccessObject extends JsonDataAccess<SubgoalQuestionAnswer>
        implements SubgoalQnaDataAccessInterface {

    private final ArrayList<SubgoalQuestionAnswer> subgoalQuestionAnswers = new ArrayList<>();

    /**
     * Creates a FileSubgoalQnaDataAccessObject and loads existing entries from the
     * given JSON file path if provided.
     *
     * @param qnaFilePath the path to the JSON file used to persist Q/A history,
     *                    or null to start with an empty store
     */
    public FileSubgoalQnaDataAccessObject(String qnaFilePath) {
        super(qnaFilePath);
        if (qnaFilePath != null) {
            try {
                loadFromJson(subgoalQuestionAnswers);
            }
            // -@cs[IllegalCatch] failed Q/A file load should not prevent application startup
            catch (Exception ex) {
                System.err.println("Could not load Q/A history: " + ex.getMessage());
            }
        }
    }

    /**
     * Parses a JSON object into a SubgoalQuestionAnswer entity.
     *
     * @param jsonObject the JSON object representing a Q/A entry
     * @return a SubgoalQuestionAnswer created from the JSON data
     */
    @Override
    public SubgoalQuestionAnswer parseJsonObject(JSONObject jsonObject) {
        final String id = jsonObject.getString("id");
        final String subgoalId = jsonObject.getString("subgoal_id");
        final String question = jsonObject.getString("question");
        final String response = jsonObject.optString("response", "");
        return new SubgoalQuestionAnswer(id, subgoalId, question, response);
    }

    /**
     * Converts a SubgoalQuestionAnswer entity into its JSON representation.
     *
     * @param object the SubgoalQuestionAnswer to convert
     * @return a JSONObject containing the Q/A entry data
     */
    @Override
    public JSONObject convertObjectToJson(SubgoalQuestionAnswer object) {
        final JSONObject obj = new JSONObject();
        obj.put("id", object.getId());
        obj.put("subgoal_id", object.getSubgoalId());
        obj.put("question", object.getQuestionMessage());
        obj.put("response", object.getResponseMessage());
        return obj;
    }

    /**
     * Returns the full question–answer history for a given subgoal ID.
     *
     * @param subgoalId the identifier of the subgoal whose history is requested
     * @return a list of SubgoalQuestionAnswer entries for the subgoal
     */
    @Override
    public synchronized List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
        final ArrayList<SubgoalQuestionAnswer> questionAnswers = new ArrayList<>();
        for (SubgoalQuestionAnswer entry : subgoalQuestionAnswers) {
            if (entry.getSubgoalId().equals(subgoalId)) {
                questionAnswers.add(entry);
            }
        }
        return questionAnswers;
    }

    /**
     * Appends a new question–answer entry for the given subgoal and persists
     * the updated history.
     *
     * @param subgoalId       the identifier of the subgoal the entry belongs to
     * @param questionMessage the question text asked about the subgoal
     * @param responseMessage the answer text associated with the question
     * @return the newly created SubgoalQuestionAnswer entry
     */
    @Override
    public synchronized SubgoalQuestionAnswer appendEntry(String subgoalId,
                                                          String questionMessage,
                                                          String responseMessage) {
        final SubgoalQuestionAnswerBuilder builder = new SubgoalQuestionAnswerBuilder();
        final SubgoalQuestionAnswer entry = builder.generateId()
                .setSubgoalId(subgoalId)
                .setQuestionMessage(questionMessage)
                .setResponseMessage(responseMessage)
                .build();
        subgoalQuestionAnswers.add(entry);
        saveToJson(subgoalQuestionAnswers);
        return entry;
    }
}
