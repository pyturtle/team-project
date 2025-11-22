package use_case.plan.delete_plan;

/**
 * The Delete Plan Interactor.
 */
public class DeletePlanInteractor implements DeletePlanInputBoundary {

    private final DeletePlanDataAccessInterface dataAccess;
    private final DeletePlanOutputBoundary presenter;

    /**
     * Creates a new Delete Plan Interactor.
     * @param dataAccess the data access interface
     * @param presenter the output boundary
     */
    public DeletePlanInteractor(DeletePlanDataAccessInterface dataAccess,
                                DeletePlanOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeletePlanInputData inputData) {
        try {
            final String planId = inputData.getPlanId();

            if (planId == null || planId.trim().isEmpty()) {
                presenter.prepareFailView("Plan ID cannot be empty");
                return;
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

