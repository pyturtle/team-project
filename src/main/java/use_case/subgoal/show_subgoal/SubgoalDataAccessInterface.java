package use_case.subgoal.show_subgoal;

import entity.subgoal.Subgoal;

/**
 * Data access interface for Subgoal entities used by the ShowSubgoal use case.
 * Implementations of this interface are responsible for loading and updating
 * subgoals from a persistent data source.
 */
public interface SubgoalDataAccessInterface {

    void save();
    void save(Subgoal subgoal);
    /**
     * Returns the Subgoal with the given ID, or {@code null} if no such subgoal exists.
     *
     * @param id the subgoal ID to look up
     * @return the Subgoal with the given ID, or null if not found
     */
    Subgoal getSubgoalById(String id);

    /**
     * Updates the priority flag of the Subgoal with the given ID.
     *
     * @param id the ID of the subgoal to update
     * @param priority the new priority value
     */
    void updatePriority(String id, boolean priority);

    /**
     * Updates the completion flag of the Subgoal with the given ID.
     *
     * @param id the ID of the subgoal to update
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
     * Deletes the subgoal with the given ID.
     *
     * @param id the ID of the subgoal to delete
     */
    void deleteSubgoal(String id);

    /**
     * Returns all subgoals for a given plan ID.
     *
     * @param planId the plan ID to get subgoals for
     * @return a list of all subgoals for the plan
     */
    java.util.List<Subgoal> getSubgoalsByPlanId(String planId);
}
