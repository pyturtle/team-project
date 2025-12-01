package data_access.interfaces.subgoal;

import entity.subgoal.Subgoal;

import java.util.List;

public interface SubgoalDataAccessInterface {
    List<Subgoal> getSubgoalsByPlan(String planId, String userId);

    List<Subgoal> getPrioritySubgoals(String userId);

    List<Subgoal> getSubgoalsByName(String name, String userId);

    List<Subgoal> getCompletedSubgoals(String userId);

    List<Subgoal> getIncompleteSubgoals(String userId);

    Subgoal getSubgoalById(String subgoalId);
    List<Subgoal> getSubgoalsByPlanId(String planId);

    void saveSubgoal(Subgoal subgoal);

    /**
     * Updates the priority flag of the Subgoal with the given ID.
     *
     * @param id       the ID of the subgoal to update
     * @param priority the new priority value
     */
    void updatePriority(String id, boolean priority);

    /**
     * Updates the completion flag of the Subgoal with the given ID.
     *
     * @param id        the ID of the subgoal to update
     * @param completed the new completed value
     */
    void updateCompleted(String id, boolean completed);

    /**
     * Returns all subgoals for a given username.
     *
     * @param username the username to get subgoals for
     * @return a list of all subgoals for the user
     */
    java.util.List<Subgoal> getSubgoalsByUsername(String username);

    /**
     * Returns all subgoals for a specific user.
     *
     * @param userId the user ID to filter by
     * @return list of all subgoals for the user
     */
    List<Subgoal> getAllSubgoalsForUser(String userId);

    /**
     * Deletes the subgoal with the given ID.
     *
     * @param id the ID of the subgoal to delete
     */
    void deleteSubgoal(String id);
}