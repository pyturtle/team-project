package use_case.subgoal.subgoal_qa;

public interface SubgoalQAOutputBoundary {
    void present(SubgoalQAOutputData outputData);
    void presentError(String message);
}
