package use_case.complete_subgoal;

public interface CompleteSubgoalOutputBoundary {
    void present(CompleteSubgoalOutputData outputData);

    void presentFailure(String s);
}
