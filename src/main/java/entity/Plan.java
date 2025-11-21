package entity;

import java.util.UUID;

public class Plan {
    private final String planId;
    private final String name;
    private final String description;
    private final String userId;
    private final String colour;

    /**
     * Creates a new plan with a unique planId, name, description, and associated userId.
     *
     * @param name        the plan's name
     * @param description the description of the plan
     * @param userId      the user ID (username) of the user who owns this plan
     * @param colour      the colour associated with this plan
     * @throws IllegalArgumentException if the name, description, userId, or colour are empty
     */
    public Plan(String name, String description, String userId, String colour) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Plan name cannot be empty");
        } else if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        } else if ("".equals(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        } else if ("".equals(colour)) {
            throw new IllegalArgumentException("Colour cannot be empty");
        }

        this.planId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.colour = colour;
    }

    /**
     * Creates a plan with a specific planId (for loading from JSON / persistence).
     *
     * @param planId      the unique plan ID
     * @param name        the plan's name
     * @param description the description of the plan
     * @param userId      the user ID (username) of the user who owns this plan
     * @param colour      the colour associated with this plan
     * @throws IllegalArgumentException if any string is empty
     */
    public Plan(String planId, String name, String description, String userId, String colour) {
        if ("".equals(planId)) {
            throw new IllegalArgumentException("Plan ID cannot be empty");
        } else if ("".equals(name)) {
            throw new IllegalArgumentException("Plan name cannot be empty");
        } else if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        } else if ("".equals(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        } else if ("".equals(colour)) {
            throw new IllegalArgumentException("Colour cannot be empty");
        }

        this.planId = planId;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.colour = colour;
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

    public String getColour() {
        return colour;
    }
}
