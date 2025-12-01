package use_case.plan.show_plans;

import entity.plan.Plan;

import java.util.List;

/**
 * Output Data for the Show Plans Use Case.
 */
public class ShowPlansOutputData {

    private final List<Plan> plans;
    private final int currentPage;
    private final int totalPages;
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private final String username;

    /**
     * Creates output data for showing plans.
     *
     * @param plans           the list of plans for the current page
     * @param currentPage     the current page number
     * @param totalPages      the total number of pages
     * @param hasNextPage     whether there is a next page
     * @param hasPreviousPage whether there is a previous page
     * @param username        the username whose plans are being displayed
     */
    public ShowPlansOutputData(List<Plan> plans, int currentPage, int totalPages,
                               boolean hasNextPage, boolean hasPreviousPage, String username) {
        this.plans = plans;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
        this.username = username;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public String getUsername() {
        return username;
    }
}
