package use_case.show_subgoal;

import entity.Subgoal;

/**
 * Data access interface for Subgoal entities used by the ShowSubgoal use case.
 * Implementations of this interface are responsible for loading and updating
 * subgoals from a persistent data source.
 */
public interface SubgoalDataAccessInterface {

    /**
     * Returns the Subgoal with the given ID, or {@code null} if no such subgoal exists.
     *
     * @param id the subgoal ID to look up
     * @return the Subgoal with the given ID, or null if not found
     */
    Subgoal getSubgoalById(int id);

    /**
     * Updates the priority flag of the Subgoal with the given ID.
     *
     * @param id the ID of the subgoal to update
     * @param priority the new priority value
     */
    void updatePriority(int id, boolean priority);
}
