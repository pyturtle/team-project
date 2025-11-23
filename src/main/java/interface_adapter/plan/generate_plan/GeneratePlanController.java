package interface_adapter.plan.generate_plan;

import use_case.plan.generate_plan.GeneratePlanInputBoundary;
import use_case.plan.generate_plan.GeneratePlanInputData;

public class GeneratePlanController {
    private final GeneratePlanInputBoundary generatePlanInteractor;

    public GeneratePlanController(GeneratePlanInputBoundary generatePlanInteractor) {
        this.generatePlanInteractor = generatePlanInteractor;
    }

    public void execute(String userMessage) {
        final GeneratePlanInputData generatePlanInputData =
                new GeneratePlanInputData(userMessage);
        generatePlanInteractor.execute(generatePlanInputData);
    }
}
