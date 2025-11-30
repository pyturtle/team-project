package interface_adapter.plan.generate_plan;

import use_case.plan.generate_plan.GeneratePlanOutputBoundary;
import use_case.plan.generate_plan.GeneratePlanOutputData;

/**
 * GeneratePlanPresenter updates the GeneratePlanViewModel based on the
 * results produced by the GeneratePlan use case.
 */
public class GeneratePlanPresenter implements GeneratePlanOutputBoundary {
    private final GeneratePlanViewModel generatePlanViewModel;

    /**
     * Creates a new GeneratePlanPresenter with the provided view model.
     * @param generatePlanViewModel the view model to update when output is received
     */
    public GeneratePlanPresenter(GeneratePlanViewModel generatePlanViewModel) {
        this.generatePlanViewModel = generatePlanViewModel;
    }

    /**
     * Updates the view model state using the provided output data and notifies listeners.
     * @param outputData the output data from the plan generation use case
     */
    @Override
    public void prepareView(GeneratePlanOutputData outputData) {
        final GeneratePlanState generatePlanState = generatePlanViewModel.getState();
        generatePlanState.setResponseMessage(outputData.getResponseMessage());
        generatePlanState.setResponseObject(outputData.getResponseObject());
        generatePlanState.setUserMessage(outputData.getUserMessage());
        generatePlanState.setSuccess(outputData.isSuccess());
        generatePlanViewModel.firePropertyChange();
    }
}
