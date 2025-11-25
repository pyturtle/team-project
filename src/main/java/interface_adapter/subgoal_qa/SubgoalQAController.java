package interface_adapter.subgoal_qa;

import use_case.subgoal.subgoal_qa.SubgoalQAInputBoundary;
import use_case.subgoal.subgoal_qa.SubgoalQAInputData;
import use_case.subgoal.subgoal_qa.SubgoalQALoadInputData;

public class SubgoalQAController {

    private final SubgoalQAInputBoundary interactor;

    public SubgoalQAController(SubgoalQAInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadHistory(String subgoalId) {
        SubgoalQALoadInputData data = new SubgoalQALoadInputData(subgoalId);
        interactor.loadHistory(data);
    }

    public void askQuestion(String subgoalId, String question) {
        SubgoalQAInputData data = new SubgoalQAInputData(subgoalId, question);
        interactor.askQuestion(data);
    }
}
