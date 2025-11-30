package use_case.plan.generate_plan;

/**
 * GeneratePlanInputBoundary defines the input boundary for the generate plan use case.
 * It receives input data and triggers the plan generation process.
 */
public interface GeneratePlanInputBoundary {

    /**
     * Executes the generate plan use case using the provided input data.
     * @param generatePlanInputData the data required to generate a plan
     */
    void execute(GeneratePlanInputData generatePlanInputData);
}
