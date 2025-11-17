package interface_adapter.show_plans;

import interface_adapter.ViewManagerModel;
import use_case.show_plans.ShowPlansOutputBoundary;
import use_case.show_plans.ShowPlansOutputData;

/**
 * The Presenter for the Show Plans Use Case.
 */
public class ShowPlansPresenter implements ShowPlansOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final ShowPlansViewModel showPlansViewModel;

    /**
     * Creates a new Show Plans Presenter.
     * @param viewManagerModel the view manager model
     * @param showPlansViewModel the show plans view model
     */
    public ShowPlansPresenter(ViewManagerModel viewManagerModel,
                              ShowPlansViewModel showPlansViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.showPlansViewModel = showPlansViewModel;
    }

    @Override
    public void prepareSuccessView(ShowPlansOutputData outputData) {
        final ShowPlansState state = showPlansViewModel.getState();
        state.setPlans(outputData.getPlans());
        state.setCurrentPage(outputData.getCurrentPage());
        state.setTotalPages(outputData.getTotalPages());
        state.setHasNextPage(outputData.hasNextPage());
        state.setHasPreviousPage(outputData.hasPreviousPage());
        state.setError(null);

        showPlansViewModel.setState(state);
        showPlansViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ShowPlansState state = showPlansViewModel.getState();
        state.setError(errorMessage);
        showPlansViewModel.setState(state);
        showPlansViewModel.firePropertyChange();
    }

    @Override
    public void switchToShowPlansView() {
        viewManagerModel.setState(showPlansViewModel.getViewName());
        viewManagerModel.firePropertyChange();

        // Trigger view update to load plans
        showPlansViewModel.firePropertyChange();
    }
}

