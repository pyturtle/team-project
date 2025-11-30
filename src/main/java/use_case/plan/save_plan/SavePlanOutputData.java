package use_case.plan.save_plan;

/**
 * SavePlanOutputData stores the results of the save plan use case,
 * including whether the operation was successful and a corresponding message.
 */
public class SavePlanOutputData {
    private final Boolean success;
    private final String message;

    /**
     * Creates a new SavePlanOutputData instance with a success flag and message.
     * @param success whether the plan was saved successfully
     * @param message a descriptive message about the save operation
     */
    public SavePlanOutputData(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Returns whether the save plan operation succeeded.
     * @return true if successful, false otherwise
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * Returns the message describing the result of the save plan operation.
     * @return the result message
     */
    public String getMessage() {
        return message;
    }
}
