package use_case.show_plans;

import entity.Plan;
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

    /**
     * Creates output data for showing plans.
     * @param plans the list of plans for the current page
     * @param currentPage the current page number
     * @param totalPages the total number of pages
     * @param hasNextPage whether there is a next page
     * @param hasPreviousPage whether there is a previous page
     */
    public ShowPlansOutputData(List<Plan> plans, int currentPage, int totalPages,
                               boolean hasNextPage, boolean hasPreviousPage) {
        this.plans = plans;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
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
}

