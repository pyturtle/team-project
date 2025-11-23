package use_case.subgoal.show_subgoal;

import entity.subgoal.Subgoal;
import java.util.List;  // Add this import

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

    // ADD THESE FILTER METHODS:

    /**
     * Returns all subgoals for a specific plan and user.
     *
     * @param planId the plan ID to filter by
     * @param userId the user ID to filter by
     * @return list of subgoals matching the plan and user
     */
    List<Subgoal> getSubgoalsByPlan(String planId, String userId);

    /**
     * Returns all priority subgoals for a specific user.
     *
     * @param userId the user ID to filter by
     * @return list of priority subgoals for the user
     */
    List<Subgoal> getPrioritySubgoals(String userId);

    /**
     * Returns all subgoals for a specific user.
     *
     * @param userId the user ID to filter by
     * @return list of all subgoals for the user
     */
    List<Subgoal> getAllSubgoalsForUser(String userId);

    /**
     * Saves an updated subgoal to the data store.
     *
     * @param subgoal the updated subgoal to save
     */
    void saveUpdatedSubgoal(Subgoal subgoal);
}