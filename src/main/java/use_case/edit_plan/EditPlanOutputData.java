package use_case.edit_plan;

public class EditPlanOutputData {
    private final String planId;
    private final String name;
    private final String description;

    public EditPlanOutputData(String planId, String name, String description) {
        this.planId = planId;
        this.name = name;
        this.description = description;
    }

    public String getPlanId() { return planId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
