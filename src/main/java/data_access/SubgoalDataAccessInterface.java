package data_access;

import entity.Subgoal;
import java.util.List;

public interface SubgoalDataAccessInterface {

    List<Subgoal> getSubgoalsByPlan(int planId, int userId);

    List<Subgoal> getPrioritySubgoals(int userId);

    List<Subgoal> getAllSubgoalsForUser(int userId);

    Subgoal getSubgoalById(int subgoalId);

    void saveUpdatedSubgoal(Subgoal subgoal);
}
