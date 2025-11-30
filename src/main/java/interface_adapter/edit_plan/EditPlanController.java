package interface_adapter.edit_plan;

import use_case.edit_plan.EditPlanInputBoundary;
import use_case.edit_plan.EditPlanInputData;

public class EditPlanController {

    private final EditPlanInputBoundary interactor;

    public EditPlanController(EditPlanInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String planId, String newName, String newDescription) {
        EditPlanInputData data = new EditPlanInputData(planId, newName, newDescription);
        interactor.execute(data);
    }
}
