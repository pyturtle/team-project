package data_access;

import entity.subgoal.Subgoal;
import java.util.List;

public interface SubgoalDataAccessInterface {
    // Change ALL int parameters to String:
    List<Subgoal> getSubgoalsByPlan(String planId, String userId);    // String, not int
    List<Subgoal> getPrioritySubgoals(String userId);
    // String, not int
    List<Subgoal> getAllSubgoalsForUser(String userId);
    List<Subgoal> getSubgoalsByName(String name, String userId);
    List<Subgoal> getCompletedSubgoals(String userId);
    List<Subgoal> getIncompleteSubgoals(String userId);// String, not int
    Subgoal getSubgoalById(String subgoalId);                         // String, not int
    void saveUpdatedSubgoal(Subgoal subgoal);
}