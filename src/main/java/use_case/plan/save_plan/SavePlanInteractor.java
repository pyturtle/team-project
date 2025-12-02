package use_case.plan.save_plan;

import data_access.interfaces.plan.SavePlanDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.plan.Plan;
import entity.plan.PlanBuilder;
import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalBuilder;
import use_case.subgoal.save_subgoal.SaveSubgoalInputData;

/**
 * SavePlanInteractor implements the save plan use case.
 * It creates a new plan and its subgoals, saves them using the data access layer,
 * and sends the result to the presenter.
 */
public class SavePlanInteractor implements SavePlanInputBoundary {

    private final SavePlanOutputBoundary savePlanPresenter;
    private final SavePlanDataAccessInterface planDataAccess;
    private final SubgoalDataAccessInterface subgoalDataAccess;
    private final SubgoalBuilder subgoalBuilder = new SubgoalBuilder();
    private final PlanBuilder planBuilder = new PlanBuilder();

    /**
     * Creates a new SavePlanInteractor with required dependencies.
     *
     * @param savePlanPresenter the presenter to receive the output
     * @param planDataAccess    the data access object used to persist plans
     * @param subgoalDataAccess the data access object used to persist subgoals
     */
    public SavePlanInteractor(SavePlanOutputBoundary savePlanPresenter,
                              SavePlanDataAccessInterface planDataAccess,
                              SubgoalDataAccessInterface subgoalDataAccess) {
        this.savePlanPresenter = savePlanPresenter;
        this.planDataAccess = planDataAccess;
        this.subgoalDataAccess = subgoalDataAccess;
    }

    /**
     * Executes the save plan use case by creating a plan and its subgoals,
     * saving them to persistent storage, and generating output data.
     *
     * @param savePlanInputData the input containing plan and subgoal details
     */
    @Override
    public void execute(SavePlanInputData savePlanInputData) {
        boolean success;
        String message;

        try {
            final Plan newPlan = planBuilder
                    .generateId()
                    .setName(savePlanInputData.getName())
                    .setDescription(savePlanInputData.getDescription())
                    .setUsername(savePlanInputData.getUsername())
                    .build();

            planDataAccess.savePlan(newPlan);

            for (SaveSubgoalInputData subgoalInputData : savePlanInputData.getSubgoals()) {
                final Subgoal subgoal = subgoalBuilder
                        .generateId()
                        .setPlanId(newPlan.getId())
                        .setName(subgoalInputData.getName())
                        .setUsername(savePlanInputData.getUsername())
                        .setDescription(subgoalInputData.getDescription())
                        .setDeadline(subgoalInputData.getDeadline())
                        .build();
                subgoalDataAccess.saveSubgoal(subgoal);
            }

            success = true;
            message = "Plan was created successfully";
        }

        catch (Exception ex) {
            message = "Something went wrong";
            success = false;
        }

        final SavePlanOutputData savePlanOutputData =
                new SavePlanOutputData(success, message);
        savePlanPresenter.prepareView(savePlanOutputData);
    }
}
