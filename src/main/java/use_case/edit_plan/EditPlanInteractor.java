package use_case.edit_plan;

import entity.Plan;

public class EditPlanInteractor implements EditPlanInputBoundary {

    private final EditPlanDataAccessInterface planDAO;
    private final EditPlanOutputBoundary presenter;

    public EditPlanInteractor(EditPlanDataAccessInterface planDAO,
                              EditPlanOutputBoundary presenter) {
        this.planDAO = planDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditPlanInputData inputData) {
        try {
            String planId = inputData.getPlanId();
            Plan existing = planDAO.getPlanById(planId);

            if (existing == null) {
                presenter.prepareFailView("Plan with id " + planId + " was not found.");
                return;
            }

            String name = inputData.getNewName();
            String desc = inputData.getNewDescription();
            String colour = inputData.getNewColour();

            if (name == null || name.trim().isEmpty()) {
                presenter.prepareFailView("Plan name cannot be empty.");
                return;
            }
            if (desc == null || desc.trim().isEmpty()) {
                presenter.prepareFailView("Description cannot be empty.");
                return;
            }
            if (colour == null || colour.trim().isEmpty()) {
                presenter.prepareFailView("Colour cannot be empty.");
                return;
            }

            Plan updated = new Plan(
                    existing.getPlanId(),
                    name,
                    desc,
                    existing.getUserId(),
                    colour
            );

            planDAO.updatePlan(updated);

            EditPlanOutputData outputData = new EditPlanOutputData(
                    updated.getPlanId(),
                    updated.getName(),
                    updated.getDescription(),
                    updated.getColour()
            );

            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Failed to update plan: " + e.getMessage());
        }
    }
}
