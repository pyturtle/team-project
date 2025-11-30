package use_case.edit_plan;

public class EditPlanInputData {
    private final String planId;
    private final String newName;
    private final String newDescription;

    public EditPlanInputData(String planId, String newName, String newDescription) {
        this.planId = planId;
        this.newName = newName;
        this.newDescription = newDescription;
    }

    public String getPlanId() {
        return planId;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewDescription() {
        return newDescription;
    }
}
