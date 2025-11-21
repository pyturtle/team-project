package use_case.delete_plan;

/**
 * Input Data for the Delete Plan Use Case.
 */
public class DeletePlanInputData {

    private final String planId;
    private final String username;

    /**
     * Creates input data for deleting a plan.
     * @param planId the ID of the plan to delete
     * @param username the username of the plan owner
     */
    public DeletePlanInputData(String planId, String username) {
        this.planId = planId;
        this.username = username;
    }

    public String getPlanId() {
        return planId;
    }

    public String getUsername() {
        return username;
    }
}

