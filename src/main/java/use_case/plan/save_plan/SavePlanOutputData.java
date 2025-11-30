package use_case.plan.save_plan;

public class SavePlanOutputData {
    private final Boolean success;
    private final String message;

    public SavePlanOutputData(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
