package use_case.edit_plan;

public class EditPlanInputData {
    private final String planId;
    private final String newName;
    private final String newDescription;
    private final String newColour;

    public EditPlanInputData(String planId, String newName, String newDescription, String newColour) {
        this.planId = planId;
        this.newName = newName;
        this.newDescription = newDescription;
        this.newColour = newColour;
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

    public String getNewColour() {
        return newColour;
    }
}
