package use_case.edit_plan;

public interface EditPlanOutputBoundary {
    void prepareSuccessView(EditPlanOutputData outputData);
    void prepareFailView(String errorMessage);
}
