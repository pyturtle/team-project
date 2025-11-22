package data_access;

import entity.SubgoalQuestionAnswer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Stores Q/A history per subgoal into a JSON file.
 *
 * JSON structure:
 * {
 *   "<subgoalIdString>": [
 *      { "id": 1, "questionMessage": "...", "responseMessage": "..." },
 *      ...
 *   ],
 *   ...
 * }
 */
public class FileSubgoalQALogDataAccessObject {

    private final String filePath;
    private final Map<String, List<SubgoalQuestionAnswer>> historyBySubgoal = new HashMap<>();

    public FileSubgoalQALogDataAccessObject(String filePath) {
        this.filePath = filePath;
        try {
            load();
        } catch (Exception e) {
            System.err.println("No existing Q/A log found, starting fresh.");
        }
    }

    public List<SubgoalQuestionAnswer> getHistory(String subgoalId) {
        return historyBySubgoal.getOrDefault(subgoalId, new ArrayList<>());
    }

    public void append(String subgoalId, String question, String answer) {
        List<SubgoalQuestionAnswer> list =
                historyBySubgoal.computeIfAbsent(subgoalId, k -> new ArrayList<>());

        int nextId = list.size() + 1;
        int subgoalIntId = subgoalId.hashCode(); // see note above

        list.add(new SubgoalQuestionAnswer(nextId, subgoalIntId, question, answer));
        save();
    }

    private void load() throws IOException {
        if (!Files.exists(Paths.get(filePath))) return;

        String content = Files.readString(Paths.get(filePath));
        if (content.isBlank()) return;

        JSONObject root = new JSONObject(content);

        for (String subgoalId : root.keySet()) {
            JSONArray arr = root.getJSONArray(subgoalId);
            List<SubgoalQuestionAnswer> list = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject qa = arr.getJSONObject(i);
                int id = qa.getInt("id");
                String q = qa.getString("questionMessage");
                String a = qa.optString("responseMessage", "");

                list.add(new SubgoalQuestionAnswer(id, subgoalId.hashCode(), q, a));
            }
            historyBySubgoal.put(subgoalId, list);
        }
    }

    private void save() {
        try {
            JSONObject root = new JSONObject();

            for (Map.Entry<String, List<SubgoalQuestionAnswer>> entry : historyBySubgoal.entrySet()) {
                JSONArray arr = new JSONArray();
                for (SubgoalQuestionAnswer qa : entry.getValue()) {
                    JSONObject obj = new JSONObject()
                            .put("id", qa.getId())
                            .put("questionMessage", qa.getQuestionMessage())
                            .put("responseMessage", qa.getResponseMessage());
                    arr.put(obj);
                }
                root.put(entry.getKey(), arr);
            }

            Files.writeString(Paths.get(filePath), root.toString(2));
        } catch (IOException e) {
            System.err.println("Failed to save Q/A log: " + e.getMessage());
        }
    }
}
