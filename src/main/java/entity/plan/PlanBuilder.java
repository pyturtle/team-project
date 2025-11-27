package entity.plan;

import entity.subgoal.Subgoal;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class PlanBuilder {
    private String id = "";
    private String name = "";
    private String description = "";
    private String username = "";
    private List<Subgoal> subgoals = new ArrayList<>();

    public PlanBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    public PlanBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PlanBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlanBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PlanBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public PlanBuilder setSubgoals(List<Subgoal> subgoals) {
        this.subgoals = subgoals;
        return this;
    }

    public Plan build() {
        return new Plan(this.id, this.name, this.description, this.username, this.subgoals);
    }
}
