package use_case.plan.generate_plan;

/**
 * GeneratePlanOutputBoundary defines the output boundary for the generate plan use case.
 * Implementations receive structured output data and prepare it for presentation.
 */
public interface GeneratePlanOutputBoundary {

    /**
     * Prepares the view using the provided generate plan output data.
     * @param outputData the data produced by the generate plan use case
     */
    void prepareView(GeneratePlanOutputData outputData);
}
