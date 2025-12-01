package use_case.plan.generate_plan;

import org.json.JSONObject;

import data_access.interfaces.plan.GeneratePlanDataAccessInterface;

/**
 * GeneratePlanInteractor implements the generate plan use case.
 * It retrieves a generated plan from the data access layer, constructs output data,
 * and sends it to the presenter for display.
 */
public class GeneratePlanInteractor implements GeneratePlanInputBoundary {

    private final GeneratePlanDataAccessInterface generatePlanDataAccessObject;
    private final GeneratePlanOutputBoundary generatePlanPresenter;

    /**
     * Creates a new GeneratePlanInteractor with the given data access object and presenter.
     *
     * @param generatePlanDataAccessObject the data access object that communicates
     *                                     with the AI model
     * @param generatePlanPresenter        the presenter that prepares the view after processing
     */
    public GeneratePlanInteractor(
            GeneratePlanDataAccessInterface generatePlanDataAccessObject,
            GeneratePlanOutputBoundary generatePlanPresenter) {
        this.generatePlanDataAccessObject = generatePlanDataAccessObject;
        this.generatePlanPresenter = generatePlanPresenter;
    }

    /**
     * Executes the use case by sending the user message to the data access object,
     * extracting the response fields, packaging the result into output data,
     * and delegating to the presenter.
     *
     * @param generatePlanInputData the input containing the user's message
     */
    @Override
    public void execute(GeneratePlanInputData generatePlanInputData) {
        final String userMessage = generatePlanInputData.getUserMessage();
        final JSONObject response = generatePlanDataAccessObject.getPlan(userMessage);
        final boolean success = response.getBoolean("success");
        final String responseMessage = response.getString("responseMessage");
        final JSONObject responseObject = response.getJSONObject("responseObject");

        final GeneratePlanOutputData generatePlanOutputData =
                new GeneratePlanOutputData(
                        responseObject,
                        success,
                        responseMessage,
                        userMessage
                );

        generatePlanPresenter.prepareView(generatePlanOutputData);
    }
}
