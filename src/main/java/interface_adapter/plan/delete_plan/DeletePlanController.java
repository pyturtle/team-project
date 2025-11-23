package interface_adapter.plan.delete_plan;

import use_case.plan.delete_plan.DeletePlanInputBoundary;
import use_case.plan.delete_plan.DeletePlanInputData;

/**
 * The Controller for the Delete Plan Use Case.
 */
public class DeletePlanController {

    private final DeletePlanInputBoundary deletePlanInteractor;

    /**
     * Creates a new Delete Plan Controller.
     * @param deletePlanInteractor the interactor for delete plan use case
     */
    public DeletePlanController(DeletePlanInputBoundary deletePlanInteractor) {
        this.deletePlanInteractor = deletePlanInteractor;
    }

    /**
     * Executes the delete plan use case.
     * @param planId the ID of the plan to delete
     * @param username the username of the plan owner
     */
    public void execute(String planId, String username) {
        final DeletePlanInputData inputData = new DeletePlanInputData(planId, username);
        deletePlanInteractor.execute(inputData);
    }
}

