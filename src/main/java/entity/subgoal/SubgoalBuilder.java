package entity.subgoal;

import java.time.LocalDate;
import java.util.UUID;

/**
 * SubgoalBuilder constructs Subgoal objects using a fluent builder pattern.
 * It allows setting individual fields, copying values from an existing subgoal,
 * and generating a new unique ID.
 */
public class SubgoalBuilder {
    private String id = "";
    private String planId = "";
    private String username = "";
    private String name = "";
    private String description = "";
    private LocalDate deadline = LocalDate.now();
    private boolean isCompleted;
    private boolean priority;

    /**
     * Copies all fields from an existing Subgoal into this builder.
     * @param subgoal the subgoal whose values should be copied
     * @return this builder with copied values
     */
    public SubgoalBuilder copyFromSubgoal(Subgoal subgoal) {
        id = subgoal.getId();
        planId = subgoal.getPlanId();
        username = subgoal.getUsername();
        name = subgoal.getName();
        description = subgoal.getDescription();
        deadline = subgoal.getDeadline();
        isCompleted = subgoal.isCompleted();
        priority = subgoal.isPriority();
        return this;
    }

    /**
     * Generates a new unique ID for the subgoal.
     * @return this builder with a generated ID
     */
    public SubgoalBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Sets the ID of the subgoal.
     * @param id the subgoal ID
     * @return this builder
     */
    public SubgoalBuilder setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the ID of the plan this subgoal belongs to.
     * @param planId the plan ID
     * @return this builder
     */
    public SubgoalBuilder setPlanId(String planId) {
        this.planId = planId;
        return this;
    }

    /**
     * Sets the username associated with this subgoal.
     * @param username the username
     * @return this builder
     */
    public SubgoalBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the subgoal name.
     * @param name the name of the subgoal
     * @return this builder
     */
    public SubgoalBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the subgoal description.
     * @param description the description of the subgoal
     * @return this builder
     */
    public SubgoalBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the deadline for the subgoal.
     * @param deadline the deadline date
     * @return this builder
     */
    public SubgoalBuilder setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    /**
     * Sets whether the subgoal is completed.
     * @param isCompleted true if completed, false otherwise
     * @return this builder
     */
    public SubgoalBuilder setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    /**
     * Sets the priority flag of the subgoal.
     * @param priority true if high priority, false otherwise
     * @return this builder
     */
    public SubgoalBuilder setPriority(boolean priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Builds and returns a Subgoal object using the configured fields.
     * @return a new Subgoal instance
     */
    public Subgoal build() {
        return new Subgoal(id, planId, username, name, description, deadline, isCompleted, priority);
    }
}
