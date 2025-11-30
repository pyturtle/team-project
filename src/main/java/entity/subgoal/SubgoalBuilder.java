package entity.subgoal;

import java.time.LocalDate;
import java.util.UUID;

public class SubgoalBuilder {
    private String id = "";
    private String planId = "";
    private String username = "";
    private String name = "";
    private String description = "";
    private LocalDate deadline = LocalDate.now();
    private boolean isCompleted = false;
    private boolean priority = false;

    public SubgoalBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    public SubgoalBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SubgoalBuilder setPlanId(String planId) {
        this.planId = planId;
        return this;
    }

    public SubgoalBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SubgoalBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SubgoalBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SubgoalBuilder setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public SubgoalBuilder setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public SubgoalBuilder setPriority(boolean priority) {
        this.priority = priority;
        return this;
    }

    public Subgoal build() {
        return new Subgoal(id, planId, username, name, description, deadline, isCompleted, priority);
    }

}
