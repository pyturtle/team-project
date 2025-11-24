package entity.plan;
import entity.subgoal.Subgoal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class Plan {
    private final String id;
    private final String name;
    private final String description;
    private final String username;
    private final List<Subgoal> subgoals;

    /**
     * Creates a new plan with an id, given name, description, and an associated username.
     * @param name the plan's name
     * @param description the description of the plan.
     * @throws IllegalArgumentException if the name or description are empty
     */

    public Plan(String id, String name, String description, String username, List<Subgoal> subgoals) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.username = username;
        this.subgoals = subgoals;
    }

    public String getId() { return id; };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() { return username; }

    public List<Subgoal> getSubgoals() { return subgoals; }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("description", this.description);

        JSONArray subgoalsArray = new JSONArray();
        for (Subgoal s : this.subgoals) {
            JSONObject subgoalJson = new JSONObject();
            subgoalJson.put("name", s.getName());
            subgoalJson.put("description", s.getDescription());
            subgoalJson.put("deadline", s.getDeadline());
            subgoalsArray.put(subgoalJson);
        }
        json.put("subgoals", subgoalsArray);

        return json;
    }
}
