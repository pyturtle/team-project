package data_access;

import entity.SubgoalQuestionAnswer;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.subgoal.qna.SubgoalQnaDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * File-backed DAO for Subgoal Q/A history.
 * Stores all entries in a single JSON file.
 */
public class FileSubgoalQnaDataAccessObject implements SubgoalQnaDataAccessInterface {

    private final String qnaFilePath;
    private final Map<String, List<SubgoalQuestionAnswer>> historyBySubgoal = new HashMap<>();

    public FileSubgoalQnaDataAccessObject(String qnaFilePath) {
        this.qnaFilePath = qnaFilePath;
        if (qnaFilePath != null) {
            try {
                loadFromJson(qnaFilePath);
            } catch (Exception e) {
                System.err.println("Could not load Q/A history: " + e.getMessage());
            }
        }
    }

    private void loadFromJson(String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            return; // nothing yet
        }

        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        if (jsonContent.trim().isEmpty()) {
            return;
        }

        JSONArray array = new JSONArray(jsonContent);
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.getString("id");
                String subgoalId = obj.getString("subgoal_id");
                String question = obj.getString("question");
                String response = obj.optString("response", "");

                SubgoalQuestionAnswer entry =
                        new SubgoalQuestionAnswer(id, subgoalId, question, response);

                historyBySubgoal
                        .computeIfAbsent(subgoalId, k -> new ArrayList<>())
                        .add(entry);
            } catch (Exception e) {
                System.err.println("Error parsing Q/A entry at index " + i + ": " + e.getMessage());
            }
        }
    }

    private void saveToJson() {
        if (qnaFilePath == null) {
            return;
        }
        try {
            JSONArray array = new JSONArray();
            for (List<SubgoalQuestionAnswer> list : historyBySubgoal.values()) {
                for (SubgoalQuestionAnswer entry : list) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", entry.getId());
                    obj.put("subgoal_id", entry.getSubgoalId());
                    obj.put("question", entry.getQuestionMessage());
                    obj.put("response", entry.getResponseMessage());
                    array.put(obj);
                }
            }
            String jsonContent = array.toString(2);
            Files.write(Paths.get(qnaFilePath), jsonContent.getBytes());
        } catch (Exception e) {
            System.err.println("Could not save Q/A history: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
        return new ArrayList<>(historyBySubgoal.getOrDefault(subgoalId, Collections.emptyList()));
    }

    @Override
    public synchronized SubgoalQuestionAnswer appendEntry(String subgoalId,
                                                          String questionMessage,
                                                          String responseMessage) {
        List<SubgoalQuestionAnswer> history =
                historyBySubgoal.computeIfAbsent(subgoalId, k -> new ArrayList<>());

        String entryId = UUID.randomUUID().toString();
        SubgoalQuestionAnswer entry =
                new SubgoalQuestionAnswer(entryId, subgoalId, questionMessage, responseMessage);

        history.add(entry);
        saveToJson();
        return entry;
    }
}
