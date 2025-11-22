package entity.plan;
import java.util.UUID;

public class Plan {
    private final String id;
    private final String name;
    private final String description;
    private final String username;

    /**
     * Creates a new plan with an id, given name, description, and an associated username.
     * @param name the plan's name
     * @param description the description of the plan.
     * @throws IllegalArgumentException if the name or description are empty
     */

    public Plan(String id, String name, String description, String username) {
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
    }

    public String getId() { return id; };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() { return username; }



}
