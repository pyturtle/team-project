package use_case.plan.generate_plan;

/**
 * GeneratePlanInputData represents the input required to generate a plan,
 * consisting of a user-provided message describing the desired plan or goal.
 */
public class GeneratePlanInputData {
    private final String userMessage;

    /**
     * Creates a new GeneratePlanInputData instance with the given user message.
     * @param userMessage the text describing the plan or goal the user wants generated
     */
    public GeneratePlanInputData(String userMessage) {
        this.userMessage = userMessage;
    }

    /**
     * Returns the user message describing the desired plan.
     * @return the user message
     */
    public String getUserMessage() {
        return userMessage;
    }
}
