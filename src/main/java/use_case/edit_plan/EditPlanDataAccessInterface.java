package use_case.edit_plan;

import entity.plan.Plan;

public interface EditPlanDataAccessInterface {
    Plan getPlanById(String planId);
    void updatePlan(Plan plan);
}
