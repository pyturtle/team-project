package use_case.subgoal.subgoal_qa;

import entity.SubgoalQuestionAnswer;
import java.util.List;

public interface SubgoalQuestionDataAccessInterface {
    List<SubgoalQuestionAnswer> getQuestionsForSubgoal(String subgoalId);

    SubgoalQuestionAnswer saveQuestion(String subgoalId, String question, String answer);
}
