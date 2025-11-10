package entity;
import java.util.UUID;

public class Plan {
    private final String id;
    private final String name;
    private final String description;
    private final Integer userid;

    /**
     * Creates a new plan with an id, given name, description, and an associated userid.
     * @param name the plan's name
     * @param description the description of the plan.
     * @throws IllegalArgumentException if the name or description are empty
     */
    public Plan(String name, String description, Integer userid) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.userid = userid;

    }

    public String getId() { return id; };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUserid() { return userid; }



}
