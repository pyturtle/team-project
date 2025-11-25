package use_case.subgoal.subgoal_qa;

public interface SubgoalQAInputBoundary {
    void loadHistory(SubgoalQALoadInputData inputData);
    void askQuestion(SubgoalQAInputData inputData);
}
