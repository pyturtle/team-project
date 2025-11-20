package use_case.plan.save_plan;

import entity.plan.Plan;
import entity.plan.PlanBuilder;
import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalBuilder;
import use_case.plan.PlanDataAccessInterface;
import use_case.plan.save_subgoal.SaveSubgoalInputData;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

public class SavePlanInteractor implements SavePlanInputBoundary{
    private final SavePlanOutputBoundary savePlanPresenter;
    private final PlanDataAccessInterface planDataAccess;
    private final SubgoalDataAccessInterface subgoalDataAccess;
    private final SubgoalBuilder subgoalBuilder = new SubgoalBuilder();
    private final PlanBuilder planBuilder = new PlanBuilder();

    public SavePlanInteractor(SavePlanOutputBoundary savePlanPresenter,
                              PlanDataAccessInterface planDataAccess,
                              SubgoalDataAccessInterface subgoalDataAccess) {
        this.savePlanPresenter = savePlanPresenter;
        this.planDataAccess = planDataAccess;
        this.subgoalDataAccess = subgoalDataAccess;
    }

    public void execute(SavePlanInputData savePlanInputData) {
        boolean success;
        String message;
        try {
            Plan newPlan = planBuilder
                    .generateId()
                    .setName(savePlanInputData.getName())
                    .setDescription(savePlanInputData.getDescription())
                    .setUsername(savePlanInputData.getUsername())
                    .build();
            planDataAccess.save(newPlan);
            for (SaveSubgoalInputData subgoalInputData : savePlanInputData.getSubgoals()) {
                Subgoal subgoal = subgoalBuilder
                        .generateId()
                        .setPlanId(newPlan.getId())
                        .setName(subgoalInputData.getName())
                        .setDescription(subgoalInputData.getDescription())
                        .setDeadline(subgoalInputData.getDeadline())
                        .build();
                subgoalDataAccess.save(subgoal);
            }
            success = true;
            message = "Plan was created successfully";

        }
        catch (Exception e) {
            message = "Something went wrong";
            success = false;
        }
        SavePlanOutputData savePlanOutputData = new SavePlanOutputData(success, message);
        savePlanPresenter.prepareView(savePlanOutputData);
    }
}
