package use_case.edit_plan;

public class EditPlanOutputData {
    private final String planId;
    private final String name;
    private final String description;
    private final String colour;

    public EditPlanOutputData(String planId, String name, String description, String colour) {
        this.planId = planId;
        this.name = name;
        this.description = description;
        this.colour = colour;
    }

    public String getPlanId() { return planId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getColour() { return colour; }
}
