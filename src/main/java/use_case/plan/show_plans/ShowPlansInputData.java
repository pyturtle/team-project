package use_case.plan.show_plans;

/**
 * Input Data for the Show Plans Use Case.
 */
public class ShowPlansInputData {

    private final String username;
    private final int page;
    private final int pageSize;

    /**
     * Creates input data for showing plans.
     * @param username the username whose plans to show
     * @param page the page number (0-indexed)
     * @param pageSize the number of plans per page
     */
    public ShowPlansInputData(String username, int page, int pageSize) {
        this.username = username;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String getUsername() {
        return username;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}

