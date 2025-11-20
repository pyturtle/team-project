package entity.plan;

public class PlanFactory {
    public Plan create(String id, String name, String description, String userId) {
        Plan newPlan = new Plan(id, name, description, userId);
        return  newPlan;
    }
}
