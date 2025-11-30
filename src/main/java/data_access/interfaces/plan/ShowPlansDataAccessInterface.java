package data_access.interfaces.plan;

import entity.plan.Plan;

import java.util.List;

/**
 * Data Access Interface for the Show Plans Use Case.
 */
public interface ShowPlansDataAccessInterface {

    /**
     * Returns all plans for the given username.
     *
     * @param username the username to look up
     * @return list of plans belonging to the user
     */
    List<Plan> getPlansByUsername(String username);

    /**
     * Returns a paginated list of plans for the given username.
     *
     * @param username the username to look up
     * @param page     the page number (0-indexed)
     * @param pageSize the number of plans per page
     * @return list of plans for the specified page
     */
    List<Plan> getPlansByUsername(String username, int page, int pageSize);

    /**
     * Returns the total number of plans for the given username.
     *
     * @param username the username to look up
     * @return the total count of plans
     */
    int getPlansCount(String username);
}

