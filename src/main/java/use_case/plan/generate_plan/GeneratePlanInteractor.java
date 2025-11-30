package use_case.plan.generate_plan;

import data_access.interfaces.plan.GeneratePlanDataAccessInterface;
import org.json.JSONObject;

public class GeneratePlanInteractor implements GeneratePlanInputBoundary {
    private final GeneratePlanDataAccessInterface generatePlanDataAccessObject;
    private final GeneratePlanOutputBoundary generatePlanPresenter;

    public GeneratePlanInteractor(GeneratePlanDataAccessInterface generatePlanDataAccessObject,
                                  GeneratePlanOutputBoundary generatePlanPresenter) {
        this.generatePlanDataAccessObject = generatePlanDataAccessObject;
        this.generatePlanPresenter = generatePlanPresenter;
    }

    @Override
    public void execute(GeneratePlanInputData generatePlanInputData) {
        String userMessage = generatePlanInputData.getUserMessage();
        JSONObject response = generatePlanDataAccessObject.getPlan(userMessage);
        boolean success = response.getBoolean("success");
        String responseMessage = response.getString("responseMessage");
        JSONObject responseObject = response.getJSONObject("responseObject");
        final GeneratePlanOutputData generatePlanOutputData = new GeneratePlanOutputData(responseObject, success,
                responseMessage, userMessage);
        generatePlanPresenter.prepareView(generatePlanOutputData);
    }
}
