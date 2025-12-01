package data_access.interfaces.file;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public abstract class JsonDataAccess<T> {

    protected String filePath = "";

    protected JsonDataAccess(String filePath) {
        this.filePath = filePath;
    }

    protected void saveToJson(Collection<T> collection) {
        if (filePath == null) {
            return;
        }
        try {
            JSONArray dataArray = new JSONArray();
            for (T entry : collection) {
                dataArray.put(convertObjectToJson(entry));
            }
            String jsonContent = dataArray.toString(2);
            Files.write(Paths.get(filePath), jsonContent.getBytes());
            System.out.println("Data saved to file: " + filePath);
        } catch (Exception e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    protected void loadFromJson(Collection<T> collection) throws IOException {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(this.filePath)));
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                collection.add(parseJsonObject(jsonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }
    public abstract T parseJsonObject(JSONObject jsonObject);
    public abstract JSONObject convertObjectToJson(T object);
}