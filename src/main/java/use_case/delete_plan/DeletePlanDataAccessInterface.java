package use_case.delete_plan;

/**
 * Data Access Interface for the Delete Plan Use Case.
 */
public interface DeletePlanDataAccessInterface {

    /**
     * Deletes a plan by its ID.
     * @param planId the ID of the plan to delete
     * @return true if the plan was deleted, false otherwise
     */
    boolean deletePlan(String planId);
}

