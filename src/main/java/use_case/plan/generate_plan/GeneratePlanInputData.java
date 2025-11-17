package use_case.plan.generate_plan;

public class GeneratePlanInputData {
    private final String userMessage;

    public GeneratePlanInputData(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
