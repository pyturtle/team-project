package use_case.plan.delete_plan;

import data_access.interfaces.plan.DeletePlanDataAccessInterface;
import data_access.interfaces.subgoal.SubgoalDataAccessInterface;
import entity.subgoal.Subgoal;

import java.util.List;

/**
 * The Delete Plan Interactor.
 */
public class DeletePlanInteractor implements DeletePlanInputBoundary {

    private final DeletePlanDataAccessInterface dataAccess;
    private final DeletePlanOutputBoundary presenter;
    private final SubgoalDataAccessInterface subgoalDataAccess;

    /**
     * Creates a new Delete Plan Interactor.
     *
     * @param dataAccess        the data access interface
     * @param presenter         the output boundary
     * @param subgoalDataAccess the subgoal data access interface
     */
    public DeletePlanInteractor(DeletePlanDataAccessInterface dataAccess,
                                DeletePlanOutputBoundary presenter,
                                SubgoalDataAccessInterface subgoalDataAccess) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.subgoalDataAccess = subgoalDataAccess;
    }

    @Override
    public void execute(DeletePlanInputData inputData) {
        try {
            final String planId = inputData.getPlanId();

            if (planId == null || planId.trim().isEmpty()) {
                presenter.prepareFailView("Plan ID cannot be empty");
                return;
            }


            if (subgoalDataAccess != null) {
                final List<Subgoal> relatedSubgoals = subgoalDataAccess.getSubgoalsByPlanId(planId);
                for (Subgoal subgoal : relatedSubgoals) {
                    subgoalDataAccess.deleteSubgoal(subgoal.getId());
                }
                System.out.println("Deleted " + relatedSubgoals.size() + " subgoals for plan " + planId);
            }


            final boolean deleted = dataAccess.deletePlan(planId);

            if (deleted) {
                final DeletePlanOutputData outputData = new DeletePlanOutputData(planId, true);
                presenter.prepareSuccessView(outputData);
            } else {
                presenter.prepareFailView("Plan not found");
            }
        } catch (Exception ex) {
            presenter.prepareFailView("Error deleting plan: " + ex.getMessage());
        }
    }
}

