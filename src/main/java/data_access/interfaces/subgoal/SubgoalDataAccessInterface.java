package data_access.interfaces.subgoal;

import entity.subgoal.Subgoal;

import java.util.List;

public interface SubgoalDataAccessInterface {
    List<Subgoal> getSubgoalsByPlan(String planId, String userId);

    List<Subgoal> getPrioritySubgoals(String userId);

    List<Subgoal> getAllSubgoalsForUser(String userId);

    List<Subgoal> getSubgoalsByName(String name, String userId);

    List<Subgoal> getCompletedSubgoals(String userId);

    List<Subgoal> getIncompleteSubgoals(String userId);

    Subgoal getSubgoalById(String subgoalId);

    void saveUpdatedSubgoal(Subgoal subgoal);

    List<Subgoal> getSubgoalsByPlanId(String planId);
}