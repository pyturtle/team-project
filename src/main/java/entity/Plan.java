package entity;
import java.util.UUID;

public class Plan {
    private final String planId;
    private final String name;
    private final String description;
    private final String userId;

    /**
     * Creates a new plan with a unique planId, name, description, and associated userId.
     * @param name the plan's name
     * @param description the description of the plan
     * @param userId the user ID (username) of the user who owns this plan
     * @throws IllegalArgumentException if the name, description, or userId are empty
     */
    public Plan(String name, String description, String userId) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Plan name cannot be empty");
        }
        if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if ("".equals(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        this.planId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    /**
     * Creates a plan with a specific planId (for loading from JSON).
     * @param planId the unique plan ID
     * @param name the plan's name
     * @param description the description of the plan
     * @param userId the user ID (username) of the user who owns this plan
     */
    public Plan(String planId, String name, String description, String userId) {
        if ("".equals(planId)) {
            throw new IllegalArgumentException("Plan ID cannot be empty");
        }
        if ("".equals(name)) {
            throw new IllegalArgumentException("Plan name cannot be empty");
        }
        if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if ("".equals(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        this.planId = planId;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public String getId() {
        return planId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }
}

