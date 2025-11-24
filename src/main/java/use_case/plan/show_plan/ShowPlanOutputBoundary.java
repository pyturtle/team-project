package use_case.plan.show_plan;

public interface ShowPlanOutputBoundary {
    void prepareSuccessView(ShowPlanOutputData showPlanOutputData);
    void prepareFailureView();
}
