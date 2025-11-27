package entity.plan;

import entity.subgoal.Subgoal;
import java.util.List;

public class PlanFactory {
    public Plan create(String id, String name, String description, String username, List<Subgoal> subgoals) {
        Plan newPlan = new Plan(id, name, description, username, subgoals);
        return newPlan;
    }
}
