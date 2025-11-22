package entity.plan;

public class PlanFactory {
    public Plan create(String id, String name, String description, String username) {
        Plan newPlan = new Plan(id, name, description, username);
        return  newPlan;
    }
}
