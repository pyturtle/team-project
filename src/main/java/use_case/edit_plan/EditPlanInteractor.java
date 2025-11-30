package use_case.edit_plan;

import entity.plan.Plan;

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
            final String planId = inputData.getPlanId();
            final Plan existing = planDAO.getPlanById(planId);

            if (existing == null) {
                presenter.prepareFailView("Plan with id " + planId + " was not found.");
                return;
            }

            final String name = inputData.getNewName();
            final String desc = inputData.getNewDescription();

            if (name == null || name.trim().isEmpty()) {
                presenter.prepareFailView("Plan name cannot be empty.");
                return;
            }
            if (desc == null || desc.trim().isEmpty()) {
                presenter.prepareFailView("Description cannot be empty.");
                return;
            }

            // Keep same id + username, change name/description
            Plan updated = new Plan(
                    existing.getId(),
                    name.trim(),
                    desc.trim(),
                    existing.getUsername()
            );

            planDAO.updatePlan(updated);

            EditPlanOutputData outputData = new EditPlanOutputData(
                    updated.getId(),
                    updated.getName(),
                    updated.getDescription()
            );

            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Failed to update plan: " + e.getMessage());
        }
    }
}
