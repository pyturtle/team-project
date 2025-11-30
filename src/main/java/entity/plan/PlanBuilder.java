package entity.plan;

import java.util.UUID;

/**
 * PlanBuilder is a builder class used to construct Plan objects.
 * It allows setting individual fields or automatically generating an ID.
 */
public class PlanBuilder {
    private String id = "";
    private String name = "";
    private String description = "";
    private String username = "";

    /**
     * Generates a new unique ID using UUID and assigns it to the plan.
     * @return this builder
     */
    public PlanBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Sets the ID of the plan.
     * @param id the ID to assign
     * @return this builder
     */
    public PlanBuilder setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the name of the plan.
     * @param name the name to assign
     * @return this builder
     */
    public PlanBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the description of the plan.
     * @param description the description to assign
     * @return this builder
     */
    public PlanBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the username of the plan owner.
     * @param username the username to assign
     * @return this builder
     */
    public PlanBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Builds and returns a new Plan instance using the assigned fields.
     * @return a fully constructed Plan object
     */
    public Plan build() {
        return new Plan(this.id, this.name, this.description, this.username);
    }
}
