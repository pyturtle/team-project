package use_case.plan;

import entity.plan.Plan;

public interface PlanDataAccessInterface {
    void save();
    void save(Plan plan);
    String getPlanNameById(String planId);
    String getPlanDescriptionById(String planId);
}
