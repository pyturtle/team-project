package use_case.plan.show_plans;

import data_access.interfaces.plan.ShowPlansDataAccessInterface;
import entity.plan.Plan;

import java.util.List;

/**
 * The Show Plans Interactor.
 */
public class ShowPlansInteractor implements ShowPlansInputBoundary {

    private final ShowPlansDataAccessInterface dataAccess;
    private final ShowPlansOutputBoundary presenter;

    /**
     * Creates a new Show Plans Interactor.
     *
     * @param dataAccess the data access interface
     * @param presenter  the output boundary
     */
    public ShowPlansInteractor(ShowPlansDataAccessInterface dataAccess,
                               ShowPlansOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ShowPlansInputData inputData) {
        try {
            final String username = inputData.getUsername();
            final int page = inputData.getPage();
            final int pageSize = inputData.getPageSize();

            final List<Plan> plans = dataAccess.getPlansByUsername(username, page, pageSize);
            final int totalCount = dataAccess.getPlansCount(username);
            final int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            final boolean hasNextPage = page < totalPages - 1;
            final boolean hasPreviousPage = page > 0;

            final ShowPlansOutputData outputData = new ShowPlansOutputData(
                    plans, page, totalPages, hasNextPage, hasPreviousPage, username
            );

            presenter.prepareSuccessView(outputData);
        } catch (Exception ex) {
            presenter.prepareFailView("Failed to load plans: " + ex.getMessage());
        }
    }

    @Override
    public void switchToShowPlansView() {
        presenter.switchToShowPlansView();
    }
}
