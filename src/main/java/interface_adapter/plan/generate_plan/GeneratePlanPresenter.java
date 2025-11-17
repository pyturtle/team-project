package interface_adapter.plan.generate_plan;

import use_case.plan.generate_plan.GeneratePlanOutputBoundary;
import use_case.plan.generate_plan.GeneratePlanOutputData;

public class GeneratePlanPresenter implements GeneratePlanOutputBoundary {
    private final GeneratePlanViewModel generatePlanViewModel;

    public GeneratePlanPresenter(GeneratePlanViewModel generatePlanViewModel) {
        this.generatePlanViewModel = generatePlanViewModel;
    }

    @Override
    public void prepareView(GeneratePlanOutputData outputData) {
        final GeneratePlanState generatePlanState = generatePlanViewModel.getState();
        generatePlanState.setResponseMessage(outputData.getResponseMessage());
        generatePlanState.setUserMessage(outputData.getUserMessage());
        generatePlanState.setSuccess(outputData.isSuccess());
        generatePlanViewModel.firePropertyChange();
    }
}
