package interface_adapter.plan.generate_plan;

import use_case.plan.generate_plan.GeneratePlanInputBoundary;
import use_case.plan.generate_plan.GeneratePlanInputData;

/**
 * GeneratePlanController receives user input for plan generation,
 * wraps it into a GeneratePlanInputData object, and passes it to the interactor.
 */
public class GeneratePlanController {
    private final GeneratePlanInputBoundary generatePlanInteractor;

    /**
     * Creates a new GeneratePlanController with the provided interactor.
     * @param generatePlanInteractor the interactor responsible for generating plans
     */
    public GeneratePlanController(GeneratePlanInputBoundary generatePlanInteractor) {
        this.generatePlanInteractor = generatePlanInteractor;
    }

    /**
     * Executes the plan generation use case with a given user message.
     * @param userMessage the text prompt describing the user's goal or plan request
     */
    public void execute(String userMessage) {
        final GeneratePlanInputData generatePlanInputData =
                new GeneratePlanInputData(userMessage);
        generatePlanInteractor.execute(generatePlanInputData);
    }
}
