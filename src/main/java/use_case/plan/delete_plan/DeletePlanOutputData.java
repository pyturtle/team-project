package use_case.plan.delete_plan;

/**
 * Output Data for the Delete Plan Use Case.
 */
public class DeletePlanOutputData {

    private final String deletedPlanId;
    private final boolean success;

    /**
     * Creates output data for deleting a plan.
     *
     * @param deletedPlanId the ID of the deleted plan
     * @param success       whether the deletion was successful
     */
    public DeletePlanOutputData(String deletedPlanId, boolean success) {
        this.deletedPlanId = deletedPlanId;
        this.success = success;
    }

    public String getDeletedPlanId() {
        return deletedPlanId;
    }

    public boolean isSuccess() {
        return success;
    }
}

