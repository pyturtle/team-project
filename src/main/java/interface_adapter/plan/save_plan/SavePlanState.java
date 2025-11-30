package interface_adapter.plan.save_plan;

/**
 * SavePlanState stores the state produced by the save plan use case,
 * specifically the status or confirmation message to display to the user.
 */
public class SavePlanState {
    private String message;

    /**
     * Returns the message describing the result of the save plan operation.
     * @return the save plan result message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message describing the result of the save plan operation.
     * @param message the message to store
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
